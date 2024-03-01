package com.wcwy.post.mapper;

import com.wcwy.post.entity.ConsigneeAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【consignee_address(发票邮寄地址)】的数据库操作Mapper
* @createDate 2022-12-01 08:42:49
* @Entity com.wcwy.post.entity.ConsigneeAddress
*/
@Mapper
public interface ConsigneeAddressMapper extends BaseMapper<ConsigneeAddress> {

}




