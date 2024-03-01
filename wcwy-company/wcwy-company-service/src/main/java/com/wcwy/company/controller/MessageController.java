package com.wcwy.company.controller;

import com.wcwy.common.base.result.R;
import com.wcwy.common.redis.enums.Message;
import com.wcwy.common.redis.enums.Sole;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.system.annotation.Log;
import com.wcwy.system.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * ClassName: MessageController
 * Description:
 * date: 2022/11/25 16:01
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Api(tags = "消息处理")
@RequestMapping("/message")
@RestController
public class MessageController {
    @Autowired
    private CompanyMetadata companyMetadata;

    @Autowired
    private RedisTemplate redisTemplate;
    @GetMapping("/deleteMessage/{putInResumeId}")
    @ApiOperation("删除已浏览的消息消息")
    @Log(title = "删除已浏览的消息消息", businessType = BusinessType.DELETE)
    public R deleteMessage(@PathVariable("putInResumeId") String putInResumeId){
        String userid =companyMetadata.userid();
        String substring = userid.substring(0, 2);
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();//存储
        setOperations.remove(Sole.MESSAGE.getKey() + userid,userid+putInResumeId);
        if ("TC".equals(substring)) {
            redisTemplate.delete(Message.COMPANY_INTERVIEW.getKey() + userid+putInResumeId);
        } else if ("TJ".equals(substring) || "TR".equals(substring)) {
            redisTemplate.delete(Message.REFERRER_INTERVIEW.getKey() +userid+ putInResumeId);
        } else {
            redisTemplate.delete(Sole.MESSAGE.getKey() + userid+putInResumeId);
        }
        return R.success("已删除");
    }
}
