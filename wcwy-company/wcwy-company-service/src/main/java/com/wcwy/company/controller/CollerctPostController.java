package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.common.base.query.PageQuery;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.common.redis.util.IDGenerator;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.web.annotation.AutoIdempotent;
import com.wcwy.company.dto.CollectPostCompanyDTO;
import com.wcwy.company.entity.CollerctPost;
import com.wcwy.company.entity.TCompany;
import com.wcwy.company.produce.CollectionProduce;
import com.wcwy.company.query.CollectPostQuery;
import com.wcwy.company.service.CollerctPostService;
import com.wcwy.company.service.TCompanyService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.CollerctPostVO;
import com.wcwy.post.api.TCompanyPostApi;
import com.wcwy.post.po.TCompanyPostPO;
import com.wcwy.post.pojo.CollerctPostPOJO;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: CollerctPostController
 * Description:
 * date: 2022/10/14 10:50
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/collerct")
@Api(tags = "岗位收藏接口")
public class CollerctPostController {
    @Resource
    private CollerctPostService collerctPostService;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TCompanyPostApi tCompanyPostApi;
    @Autowired
    private CollectionProduce collectionProduce;
    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private TCompanyService tCompanyService;

    @PostMapping("/save")
    @ApiOperation("收藏岗位")
    @Log(title = "收藏岗位", businessType = BusinessType.SELECT)
    @AutoIdempotent
    public R save(@Valid @RequestBody CollerctPostVO collerctPostVO) {
        List<String> post = collerctPostVO.getPost();
        if(post.size()==0){
            return R.fail("未选择岗位!");
        }
        for (String s : post) {
            //存入数据库保持唯一性
            Boolean member = redisUtils.sHasKey(Sole.INSERT_COLLERCT_POST.getKey() + collerctPostVO.getCollerctUserId(), s);
            if (member) {
                /*   return R.fail("该岗位已收藏");*/
                continue;
            }
            if(!member){
                QueryWrapper queryWrapper=new QueryWrapper();
                queryWrapper.eq("collerct_user_id",companyMetadata.userid());
                queryWrapper.eq("post",s);
                queryWrapper.eq("deleted",0);
                int count = collerctPostService.count(queryWrapper);
                if(count>0){
                    redisUtils.sSetAndTime(Sole.INSERT_COLLERCT_POST.getKey() + collerctPostVO.getCollerctUserId(),3600, s);
                    continue;
                }
            }
            CollerctPost collerctPost = new CollerctPost();
            BeanUtils.copyProperties(collerctPostVO, collerctPost);
            collerctPost.setCollerctPostId(idGenerator.generateCode("CP"));
            collerctPost.setPost(s);
            collerctPost.setDeleted(0);
            collerctPost.setCreateTime(LocalDateTime.now());
            boolean save = collerctPostService.save(collerctPost);
            if (save) {
                //存入redis数据库
                redisUtils.sSetAndTime(Sole.INSERT_COLLERCT_POST.getKey() + collerctPostVO.getCollerctUserId(),3600, s);
                String company = collerctPostService.selectCompany(s);
                if (!StringUtils.isEmpty(company)) {
                    collectionProduce.sendAsyncMessage(company);
                }

            } else {
                return R.fail("收藏失败！");
            }
        }


        return R.success("收藏成功！");

    }


    @GetMapping("/delete/{collerctPostId}")
    @ApiOperation("删除收藏")
    @ApiImplicitParam(name = "collerctPostId", required = true, value = "收藏的岗位")
    @Log(title = "删除收藏", businessType = BusinessType.DELETE)
    @AutoIdempotent
    public R delete(@PathVariable("collerctPostId") List<String> post) {
        if (StringUtils.isEmpty(post)) {
            return R.fail("收藏id不能为空！");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("post", post);
        queryWrapper.eq("collerct_user_id", companyMetadata.userid());
        boolean remove = collerctPostService.remove(queryWrapper);
        if (remove) {
            String[] toBeStored = post.toArray(new String[post.size()]);
            redisUtils.setRemove(Sole.INSERT_COLLERCT_POST.getKey() + companyMetadata.userid(), toBeStored);
            return R.success("取消收藏成功！");
        }
        return R.fail("取消收藏失败！");
    }


    @PostMapping("/selectCollect")
    @ApiOperation("查看收藏的岗位")
    @Log(title = "查看收藏的岗位", businessType = BusinessType.SELECT)
    public R<CollectPostCompanyDTO> selectCollect( @RequestBody CollectPostQuery collectPostQuery) {
        String userid = companyMetadata.userid();
        collectPostQuery.setUserId(userid);
/*        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("collerct_user_id", userid);
        IPage page = collerctPostService.page(pageQuery.createPage(), queryWrapper);
        List<CollerctPost> records = page.getRecords();
        List<CollerctPostPOJO> list = new ArrayList<>();
        for (CollerctPost record : records) {
            CollerctPostPOJO dto = new CollerctPostPOJO();
            dto.setCollerctPostId(record.getCollerctPostId());
            dto.setCollerctUserId(record.getCollerctUserId());
            TCompanyPostPO tCompanyPostPO = tCompanyPostApi.selectTCompanyPostPO(record.getPost());
            BeanUtils.copyProperties(tCompanyPostPO, dto);
            dto.setTCompanyPostPO(tCompanyPostPO);
            list.add(dto);
            dto = null;
        }
        page.setRecords(list);
        return R.success(page);*/
      IPage<CollectPostCompanyDTO> iPage=  collerctPostService.selectCollect(collectPostQuery);
        List<CollectPostCompanyDTO> records = iPage.getRecords();
       // List<String> stringList=new ArrayList<>(records.size());
        for (CollectPostCompanyDTO record : records) {
            String substring = record.getCompanyId().substring(0, 2);
            if(substring.equals("TR")){
                record.setType(1);
                record.setCompanyType(2);
            }else if (substring.equals("TC")){
                record.setType(0);
               // stringList.add(record.getCompanyId());
                TCompany id = tCompanyService.getId(record.getCompanyId());
                if(id !=null){
                    record.setCompanyType(id.getCompanyType());
                }
            }
        }
    //  List<TCompany> list=  tCompanyService.companyLists(stringList);

        iPage.setRecords(records);
        return R.success(iPage);
    }


    @PostConstruct // 构造函数之后执行
    public void init() {
        List<CollerctPost> list = collerctPostService.list();
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        for (CollerctPost collerctPost : list) {
            setOperations.add(Sole.INSERT_COLLERCT_POST.getKey() + collerctPost.getCollerctUserId(), collerctPost.getPost());
        }
    }

    @PostMapping("/selectPost/{postId}")
    @ApiOperation("查看求职者是否收藏")
    @Log(title = "查看求职者是否收藏", businessType = BusinessType.SELECT)
    @ApiImplicitParam(name = "postId", required = true, value = "岗位id")
    public R selectPost(@PathVariable("postId") String postId) {
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        Boolean member = setOperations.isMember(Sole.INSERT_COLLERCT_POST.getKey() + companyMetadata.userid(), postId);
        if (!member) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("collerct_user_id", companyMetadata.userid());
            queryWrapper.eq("post", postId);
            int count = collerctPostService.count(queryWrapper);
            return R.success(count > 0 ? true : false);
        }
        return R.success(member);
    }

    @GetMapping("/isCollect")
    public boolean isCollect(@RequestParam("jobHunter") String jobHunter, @RequestParam("postId") String postId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("post", postId);
        queryWrapper.eq("collerct_user_id", jobHunter);
        int count = collerctPostService.count(queryWrapper);
        return count > 0 ? true : false;
    }

  /*  @GetMapping("/cancelCollect")
    @ApiOperation("取消收藏")
    @ApiImplicitParam(name = "postId", required = true, value = "岗位id")
    public R cancelCollect(@RequestParam("postId") String postId) {
        String userid = companyMetadata.userid();
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("post",postId);
        queryWrapper.eq("collerct_user_id",userid);

    }*/

}
