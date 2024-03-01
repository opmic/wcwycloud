package com.wcwy.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcwy.company.entity.BankCard;
import com.wcwy.company.service.BankCardService;
import com.wcwy.company.mapper.BankCardMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【bank_card(银行卡)】的数据库操作Service实现
* @createDate 2023-08-17 14:09:29
*/
@Service
public class BankCardServiceImpl extends ServiceImpl<BankCardMapper, BankCard>
    implements BankCardService{

}




