package com.wcwy.company.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wcwy.common.base.query.PageQuery;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.Cache;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.company.dto.CooCommentDTO;
import com.wcwy.company.entity.CooComment;
import com.wcwy.company.query.CooCommentQuery;
import com.wcwy.company.service.CooCommentService;
import com.wcwy.company.service.CooTribeService;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.company.vo.CooCommentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ClassName: CooCommentController
 * Description:
 * date: 2024/1/19 14:27
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/comment")
@Api(tags = "coo评论")
public class CooCommentController {
    @Autowired
    private CooTribeService cooTribeService;

    @Autowired
    private CooCommentService cooCommentService;
    @Autowired
    private CompanyMetadata companyMetadata;
    @Autowired
    private RedisUtils redisUtils;
    @PostMapping("/page")
    @ApiOperation("获取评论")
    public R<CooCommentDTO> page(@RequestBody CooCommentQuery pageQuery) {
        IPage<CooCommentDTO> page = cooCommentService.getPage(pageQuery);
        return R.success(page);
    }

    @PostMapping("/add")
    @ApiOperation("发表评论")
    //@Transactional(rollbackFor = Exception.class)
    public R add(@Valid @RequestBody CooCommentVO commentVO) {
        String userid = companyMetadata.userid();
        String substring = userid.substring(0, 2);
        if( ! substring.equals("TR")){
            return R.fail("请使用推荐管账号！");
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String format1 = format.format(new Date());
        redisUtils.incrTime3(Cache.CACHE_COMMENT_TRIBE.getKey()+format1+commentVO.getUserId(),1);
        redisUtils.incr(Cache.CACHE_COMMENT_TRIBE.getKey()+commentVO.getCooTribeId(),1);
        redisUtils.sSet(Cache.CACHE_RECORD_COMMENT_TRIBE.getKey(),commentVO.getCooTribeId());
        CooComment cooComment=new CooComment();
        BeanUtils.copyProperties(commentVO,cooComment);
        cooComment.setCreateUser(companyMetadata.userid());
        boolean save = cooCommentService.save(cooComment);
        if(save){

            return R.success();
        }
        return R.fail();
    }


}
