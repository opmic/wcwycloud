package com.wcwy.system.service;

import com.wcwy.system.entity.SysLogininfor;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author Administrator
* @description 针对表【sys_logininfor(系统访问记录)】的数据库操作Service
* @createDate 2023-03-24 09:08:27
*/

public interface SysLogininforService extends IService<SysLogininfor> {
    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     */
    public void insertLogininfor(SysLogininfor logininfor);

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor);

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    public int deleteLogininforByIds(Long[] infoIds);

    /**
     * 清空系统登录日志
     */
    public void cleanLogininfor();
}
