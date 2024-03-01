package com.wcwy.company.produce;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.wcwy.common.redis.entity.MQEntity;
import com.wcwy.common.redis.util.RedisUtils;
import com.wcwy.common.utils.PostOrder;
import com.wcwy.system.annotation.RocketLog;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Map;
import java.util.Set;

/**
 * ClassName: PostOrderProduce
 * Description:
 * date: 2022/12/9 15:02
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Service
public class PostOrderProduce {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RedisUtils redisUtils;
    @RocketLog(title = "生成订单", businessType =2)
    public void sendOrderlyMessage(String msg){
        if(StringUtils.isEmpty(msg)){
            return;
        }
        rocketMQTemplate.asyncSend(PostOrder.TOPIC, msg,
                new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        System.out.println("发送成功！");
                    }
                    @Override
                    public void onException(Throwable throwable) {
                        redisUtils.sendSet(PostOrder.TOPIC,msg,throwable);
                        System.out.println("发送失败！");
                    }
                });
    }

     @Scheduled(cron = "0/5 * * * * ?")
    public void retry(){
        Set<Object> set = redisUtils.getSet(PostOrder.TOPIC);
        if(set==null){
            return;
        }
        for (Object o : set) {
            MQEntity mqEntity = JSONUtil.toBean(o.toString(), MQEntity.class);
            String date = mqEntity.getDate();
          /*  String s = JSON.parseObject(date, String.class);*/
            Map map = JSON.parseObject(date, Map.class);
            sendOrderlyMessage(JSON.toJSONString(map));
            redisUtils.delSet(PostOrder.TOPIC,o);
        }
    }
}
