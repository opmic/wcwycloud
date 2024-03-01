package com.wcwy.company.task;

import com.wcwy.common.redis.enums.QRCode;
import com.wcwy.company.config.CosUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName: DeleteImges
 * Description:
 * date: 2022/12/26 16:35
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Slf4j
@Component
public class DeleteImges {
    @Autowired
    private CosUtils cosUtils;
    @Autowired
    private RedisTemplate redisTemplate;
    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点执行一次
    //删除邀请二维码图片
    public void redisCreateOrderQueue() {
        ListOperations<String,String> listOperations = redisTemplate.opsForList();
        List<String> range = listOperations.range(QRCode.QR_IMAGES.getValue(), 0, listOperations.size(QRCode.QR_IMAGES.getValue()));
        cosUtils.batchDelFile(range);
        redisTemplate.delete(QRCode.QR_IMAGES.getValue());
    }
}
