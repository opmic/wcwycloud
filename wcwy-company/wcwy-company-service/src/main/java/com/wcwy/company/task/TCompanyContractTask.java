package com.wcwy.company.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcwy.common.base.utils.LocalDateTimeUtils;
import com.wcwy.company.asyn.InformAsync;
import com.wcwy.company.entity.TCompanyContract;
import com.wcwy.company.service.TCompanyContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: TCompanyContractTask
 * Description:
 * date: 2023/9/14 8:44
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Slf4j
@Component
public class TCompanyContractTask {
    @Autowired
    private TCompanyContractService tCompanyContractService;
    @Autowired
    private InformAsync informAsync;

    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点执行一次
    //@Scheduled(cron = "0/3 * * * * ?")
    public void redisCreateOrderQueue() {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("state",0);
        queryWrapper.eq("audit_contract",1);
        queryWrapper.lt("contract_date", LocalDate.now());
        List<TCompanyContract> list = tCompanyContractService.list(queryWrapper);
        for (TCompanyContract tCompanyContract : list) {
            tCompanyContract.setState(1);
            tCompanyContractService.updateById(tCompanyContract);
        }
    }
    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点执行一次
   // @Scheduled(cron = "0/3 * * * * ?")
    public void noticeOfExpiration() {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("state",0);
        queryWrapper.eq("audit_contract",1);
        LocalDate localDate = LocalDate.now().plusDays(30);
        queryWrapper.lt("contract_date", localDate);
        List<TCompanyContract> list = tCompanyContractService.list(queryWrapper);
        for (TCompanyContract tCompanyContract : list) {
            informAsync.noticeOfExpiration(tCompanyContract.getCreateId(),tCompanyContract.getContractDate());
        }
    }
}
