package com.wcwy.post.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: TransferResponsEntity
 * @Description: TODO
 * @Author: tangzhuo
 * @Date: 2022-08-16 19:42
 */
@Data
@NoArgsConstructor
public class TransferResponsEntity implements Serializable {

    private String code;
    private String message;
    private String batch_id;
    private String out_batch_no;

    @Override
    public String toString() {
        return "TransferResponsEntity{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", batch_id='" + batch_id + '\'' +
                ", out_batch_no='" + out_batch_no + '\'' +
                '}';
    }
}

