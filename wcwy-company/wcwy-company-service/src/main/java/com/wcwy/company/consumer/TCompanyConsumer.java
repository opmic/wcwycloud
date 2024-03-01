package com.wcwy.company.consumer;

import com.wcwy.common.utils.TCompany;
import com.wcwy.company.config.CosUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ClassName: TCompanyConsunmer
 * Description:
 * date: 2022/12/26 16:16
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
//@Component
//@Slf4j
//@RocketMQMessageListener(topic = TCompany.TOPIC,consumerGroup = TCompany.GROUP,messageModel = MessageModel.CLUSTERING)//默认 集群
public class TCompanyConsumer implements RocketMQListener<String> {
    @Autowired
    private CosUtils cosUtils;

    //删除二维码
    @Override
    public void onMessage(String message) {
        cosUtils.DelSingleFile(message);
    }
}
