package com.wcwy.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * ClassName: JWT
 * Description:
 * date: 2023/3/29 20:21
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
public class JWT {
    public static Claims getClaimsFromJwt(String jwt) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey("wangcaiwuyou").parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }
}
