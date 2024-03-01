package com.wawy.company.pojo;

import com.wcwy.post.entity.RunningWater;
import lombok.Data;

import java.util.List;

/**
 * ClassName: DeductGold
 * Description:
 * date: 2022/11/17 10:51
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
public class DeductGold {
    /**
     * 无忧币流水
     */
    public List<RunningWater> list;

    /**
     * 是否成功
     */
    public Boolean isOK;

    /**
     * 失败原因
     */
    public String cause;
}
