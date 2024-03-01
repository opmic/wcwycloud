package com.wcwy.system.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.system.entity.SysRocketmqLog;
import com.wcwy.system.service.SysRocketmqLogService;
import com.wcwy.system.mapper.SysRocketmqLogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author Administrator
* @description 针对表【sys_rocketmq_log(操作日志记录)】的数据库操作Service实现
* @createDate 2023-06-27 16:03:26
*/
@Service
@DS("slave_1")
public class SysRocketmqLogServiceImpl extends ServiceImpl<SysRocketmqLogMapper, SysRocketmqLog>
    implements SysRocketmqLogService{

    @Resource
    private SysRocketmqLogMapper sysRocketmqLogMapper;

    @Override
    public void insert(SysRocketmqLog operLog) {
        sysRocketmqLogMapper.insert(operLog);
    }
}




