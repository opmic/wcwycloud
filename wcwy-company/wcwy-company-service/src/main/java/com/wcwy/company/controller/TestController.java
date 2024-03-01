package com.wcwy.company.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wcwy.company.produce.PostOrderProduce;
import com.wcwy.company.session.CompanyMetadata;
import com.wcwy.post.api.SendMessageApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * ClassName: TestController
 * Description:
 * date: 2023/7/24 20:27
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/test")
@Api(tags = "测试")
@Slf4j
public class TestController {

    @Resource
    private ThreadPoolExecutor dtpExecutor1;

    @Autowired
    private PostOrderProduce postOrderProduce;

    @Autowired
    private SendMessageApi sendMessageApi;
    @Autowired
    private CompanyMetadata  companyMetadata;

    @GetMapping("/senMsg")
    @ApiOperation("测试消息")
    public void senMsg(){
        Map map=new HashMap(3);
        map.put("msg","12311323123");
        map.put("fromUserId","123123");
        map.put("toUserId",companyMetadata.userid());
        sendMessageApi.sendMsg(map);
    }


    @GetMapping("/cc")
    @ApiOperation("测试")
    public String cc() throws InterruptedException {
        //exec();
        JSONObject jsonObject = JSON.parseObject("\n" +
                "{\"birthday\":\"2000-01-05\",\"jobHunter\":\"TJ2308211516867-3\",\"education\":\"5\",\"referrer_id\":\"TR2308211359642-3\",\"recommend_time\":\"2023-08-21 15:22:30\",\"entryDate\":\"2023-08-22\",\"avatar\":\"https://dev-1313175594.cos.ap-guangzhou.myqcloud.com/images/48a042c05d8247e28806cb3392428406.jpg\",\"userName\":\"偶多多\",\"payer\":\"TC2308211355139-1\",\"workTime\":\"2020-01-13\",\"put_in_resume_id\":\"PR2308211522983-4\",\"putInJobhunter\":\"TJ2308211516867-3\",\"entryTime\":\"2023-08-22 20:44:00\",\"create_id\":\"TC2308211355139-1\",\"post_id\":\"PS2308211456950-1\",\"post_type\":2,\"hired_bounty\":22.00,\"create_name\":\"欧尼酱\"}");
        postOrderProduce.sendOrderlyMessage(jsonObject.toJSONString());
        return "ddd";
    }



    public void exec() throws InterruptedException {

        dtpExecutor1.execute(()->{
            try {
                log.info("核心线程数:{},最大线程数:{},队列大小:{}",dtpExecutor1.getCorePoolSize(),dtpExecutor1.getMaximumPoolSize(),dtpExecutor1.getQueue().size());
                Thread.sleep(102222222222200000L);
                                System.out.println("test");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
    }

}
