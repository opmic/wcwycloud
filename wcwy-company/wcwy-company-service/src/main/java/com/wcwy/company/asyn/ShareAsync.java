package com.wcwy.company.asyn;

import com.wcwy.company.entity.Share;
import com.wcwy.company.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * ClassName: ShareAsync
 * Description:
 * date: 2023/7/20 11:53
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
public class ShareAsync {

    @Autowired
    private ShareService shareService;

    @Async()
    public void addAmount(String userId,int type) throws InterruptedException {
        Share byId = shareService.getById(userId);
        if(byId==null){
            Share share=new Share();
            share.setUserId(userId);
            share.setJobHunterAmount(0L);
            share.setCompanyAmout(0L);
            share.setRecommendAmount(0L);
            share.setHeadhunterAmount(0L);
            share.setCampusRecommendAmount(0L);
            boolean save = shareService.save(share);
            if(save){
                byId=share;
            }
        }
        if(byId==null){
            return;
        }
        if(type==0){
            byId.setCompanyAmout(byId.getCompanyAmout()+1L);
        }else if(type==1){
            byId.setRecommendAmount(byId.getRecommendAmount()+1L);
        }else if(type==2){
            byId.setJobHunterAmount(byId.getJobHunterAmount()+1L);
        }else if(type==3){
            byId.setHeadhunterAmount(byId.getHeadhunterAmount()+1L);
        }else if(type==3){
            byId.setCampusRecommendAmount(byId.getCampusRecommendAmount()+1L);
        }
        shareService.updateById(byId);
    }
}
