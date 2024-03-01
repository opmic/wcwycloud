package com.wcwy.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.post.entity.ConsigneeAddress;
import com.wcwy.post.service.ConsigneeAddressService;
import com.wcwy.post.mapper.ConsigneeAddressMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【consignee_address(发票邮寄地址)】的数据库操作Service实现
* @createDate 2022-12-01 08:42:49
*/
@Service
public class ConsigneeAddressServiceImpl extends ServiceImpl<ConsigneeAddressMapper, ConsigneeAddress>
    implements ConsigneeAddressService{

}




