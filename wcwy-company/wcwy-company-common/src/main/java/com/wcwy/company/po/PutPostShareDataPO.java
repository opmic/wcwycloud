package com.wcwy.company.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * ClassName: PutPostShareDataPO
 * Description:
 * date: 2023/10/28 9:48
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
public class PutPostShareDataPO {
    private Integer counts;
    private LocalDateTime time;
}
