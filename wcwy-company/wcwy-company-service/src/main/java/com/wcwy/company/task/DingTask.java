package com.wcwy.company.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taobao.api.ApiException;
import com.wcwy.common.base.utils.SendDingDing;
import com.wcwy.common.redis.enums.RedisCache;
import com.wcwy.company.entity.CompanyUserRole;
import com.wcwy.post.entity.TPayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * ClassName: DingTask
 * Description:
 * date: 2023/12/11 15:46
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Slf4j
@Component
public class DingTask {
    @Scheduled(cron = "0 0 8 * * ?")//每天凌晨8点执行一次
    public void delete() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, ApiException {
        SendDingDing.sendText("今天又是美好的的一天。文总早上好啊！");
        SendDingDing.sendText("文总，你是我们的主心骨，领头羊，公司可以没有我们，但不能没有文总。感谢你默默的付出，你是我们公司生命中的一道光，你的无私和奉献让我们感到无比的敬佩。愿你的付出得到上天的眷顾，愿你的善良得到世界的认可。愿你的每一天都充满阳光，愿你的生活如诗如画。感谢你，我的朋友，愿你幸福安康，一切如意。");
        SendDingDing.sendLink();
        SendDingDing.sendText("小小江龙啊，别当舔狗,给女朋友的钱要自己也买点早餐，小心挂了----东莞第一深情《江龙》");
    }
}
