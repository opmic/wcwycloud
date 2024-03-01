package com.wcwy.system.mapper;

import com.wcwy.system.entity.SysRocketmqLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【sys_rocketmq_log(操作日志记录)】的数据库操作Mapper
* @createDate 2023-06-27 16:03:26
* @Entity com.wcwy.system.entity.SysRocketmqLog
*/
@Mapper
public interface SysRocketmqLogMapper extends BaseMapper<SysRocketmqLog> {

}




