package com.wcwy.company.asyn;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcwy.company.service.TCompanyService;
import com.wcwy.company.service.TJobhunterService;
import com.wcwy.company.service.TRecommendService;
import com.wcwy.company.service.WeixinLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * ClassName: LoginRecord
 * Description: 用户登录记录
 * date: 2022/10/10 9:19
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Slf4j
@Component
public class LoginRecord {
    @Autowired
    private TCompanyService tCompanyService;
    @Autowired
    private TRecommendService tRecommendService;
    @Autowired
    private TJobhunterService tJobhunterService;


    /**
     * @Description: 异步记录
     * @param userid 用户id
     * @param identity 用户身份（(0普通改为 1:赏金岗位 2猎头岗位 3：管理员)	）
     * @return null
     * @Author tangzhuo
     * @CreateTime 2022/10/10 9:21
     */
    @Async
    public void CompanyLogin(String userid ,int identity){

        UpdateWrapper updateWrapper=new UpdateWrapper();
        if(identity==0){
            updateWrapper.eq("company_id",userid);
            updateWrapper.set("login_time", LocalDateTime.now());
            boolean update = tCompanyService.update(updateWrapper);
            if(! update){
                log.error("登录更新失败！");
            }
        }

        if(identity==1){
            updateWrapper.eq("id",userid);
            updateWrapper.set("login_time", LocalDateTime.now());
            boolean update = tRecommendService.update(updateWrapper);
            if(! update){
                log.error("登录更新失败！");
            }
        }
        if(identity==2){
            updateWrapper.eq("user_id",userid);
            updateWrapper.set("login_time", LocalDateTime.now());
            boolean update = tJobhunterService.update(updateWrapper);
            if(! update){
                log.error("登录更新失败！");
            }
        }
    }
}
