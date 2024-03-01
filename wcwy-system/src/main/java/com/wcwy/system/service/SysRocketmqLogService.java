package com.wcwy.system.service;

import com.wcwy.system.entity.SysRocketmqLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【sys_rocketmq_log(操作日志记录)】的数据库操作Service
* @createDate 2023-06-27 16:03:26
*/
public interface SysRocketmqLogService extends IService<SysRocketmqLog> {


    void insert(SysRocketmqLog operLog);
}
