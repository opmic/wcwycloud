package com.wcwy.post.asyn;

import com.wcwy.post.entity.UpdatePostRecord;
import com.wcwy.post.service.UpdatePostRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * ClassName: UpdatePostRecordAsync
 * Description:
 * date: 2023/9/6 10:39
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Slf4j
@Component
public class UpdatePostRecordAsync {

    @Autowired
    private UpdatePostRecordService postRecordService;

    /**
     * 添加推荐官
     * @param postId
     * @param userId
     * @param data
     */

    @Async
    public void add(String postId,String userId,String data){
        UpdatePostRecord updatePostRecord=new UpdatePostRecord();
        updatePostRecord.setUpdateTime(LocalDateTime.now());
        updatePostRecord.setPostId(postId);
        updatePostRecord.setUpdateId(userId);
        updatePostRecord.setHistoricalData(data);
        boolean save = postRecordService.save(updatePostRecord);
    }
}
