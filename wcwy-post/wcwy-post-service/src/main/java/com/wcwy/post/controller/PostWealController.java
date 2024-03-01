package com.wcwy.post.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.post.entity.PostWeal;
import com.wcwy.post.entity.Work;
import com.wcwy.post.service.PostWealService;
import com.wcwy.post.vo.PostWealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: PostWealController
 * Description:
 * date: 2022/9/14 16:09
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@Api(tags = "职位福利接口")
@RequestMapping("/postWeal")
public class PostWealController {

    @Autowired
    private PostWealService postWealService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void doConstruct() throws Exception {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parebt", 0);
        List<PostWeal> list = postWealService.list(queryWrapper);
        ValueOperations<String, List<PostWeal>> value = redisTemplate.opsForValue();
        redisTemplate.delete(RedisCache.WEAL.getValue());
        if (list.size() > 0) {
            value.set(RedisCache.WEAL.getValue(), list);
        }
    }

    @GetMapping("/selectList")
    @ApiOperation(value = "查询职位福利")
    public R<PostWealVO> selectList() {
        ValueOperations<String, List<PostWeal>> value = redisTemplate.opsForValue();
        List<PostWeal> r = value.get(RedisCache.WEAL.getValue());
        if(r ==null ){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("parebt", 0);
           r = postWealService.list(queryWrapper);
           if(r.size()>0){
               value.set(RedisCache.WEAL.getValue(), r);
           }
        }

        List<PostWealVO> list1 = new ArrayList<>();
        if (r.size() > 0) {
            for (PostWeal postWealVO : r) {
                PostWealVO postWealVO1 = new PostWealVO();
                BeanUtils.copyProperties(postWealVO, postWealVO1);
                QueryWrapper<PostWeal> queryWrapper1 = new QueryWrapper();
                queryWrapper1.eq("parebt", postWealVO.getWealId());
                List<PostWeal> list = postWealService.list(queryWrapper1);
                if (list.size() > 0) {
                    postWealVO1.setList(list);
                }
                list1.add(postWealVO1);
                postWealVO1 = null;
                queryWrapper1 = null;
                list = null;
            }
        }
        return R.success(list1);
    }


}
