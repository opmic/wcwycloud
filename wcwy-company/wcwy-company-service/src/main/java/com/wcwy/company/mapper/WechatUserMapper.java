package com.wcwy.company.mapper;

import com.wcwy.company.entity.WechatUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【wechat_user(用户微信信息表)】的数据库操作Mapper
* @createDate 2022-09-26 09:11:27
* @Entity com.wcwy.company.entity.WechatUser
*/
@Mapper
public interface WechatUserMapper extends BaseMapper<WechatUser> {

}




