package com.wcwy.gateway.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: IpAddress
 * Description:
 * date: 2023/3/28 14:43
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@AllArgsConstructor
@Getter
public enum IpAddress {

    login_ip("login:ip"),
    login("login");
    private String ipAddress;
}
