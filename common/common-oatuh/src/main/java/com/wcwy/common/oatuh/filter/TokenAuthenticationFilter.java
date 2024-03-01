package com.wcwy.common.oatuh.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.wcwy.common.base.enums.IpAddress;
import com.wcwy.common.config.session.LoginUser;
import com.wcwy.common.utils.IpUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
*
 * @author Administrator
 * @version 1.0
 *
*/

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter implements OrderedFilter {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
            //解析出头中的token
        String requestURI = httpServletRequest.getRequestURI();
        String token1 = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String token = httpServletRequest.getHeader("Authorization");
/*        if(token !=null){
            if("system".equals(token)){
                ValueOperations valueOperations = redisTemplate.opsForValue();
                String o = (String) valueOperations.get("system");
              String  systemUser= (String) valueOperations.get("systemUser");
                LoginUser loginUser = JSON.parseObject(systemUser, LoginUser.class);
                String[] authorities1 = new String[]{o};
                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(loginUser,null, AuthorityUtils.createAuthorityList(authorities1));
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            }
        }*/
        if(token!=null){
           // Boolean flag = stringRedisTemplate.expire("auth:"+token.replace("Bearer","").trim(),  2 * 60 * 60, TimeUnit.SECONDS);
         //   System.out.println(token.replace("Bearer","").trim());
            if (StrUtil.isBlank(token) || !StrUtil.startWithIgnoreCase(token,"Bearer")) {
       /*         System.out.println(StrUtil.startWithIgnoreCase(token,"Bearer"));
                System.out.println(token);*/
                throw new Exception("非法token");
            }
            //从安全上下文中拿 到用户身份对象
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof OAuth2Authentication)){
                 /* return null;*/
            }


            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
            Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
            //取出用户身份信息
            String principal = userAuthentication.getName();
            //取出用户权限
            List<String> authorities = new ArrayList<>();
            //从userAuthentication取出权限，放在authorities
               userAuthentication.getAuthorities().stream().forEach(c->authorities.add(((GrantedAuthority) c).getAuthority()));
            /*  userAuthentication*/
          /*  for (String authority : authorities) {
                System.out.println(authority);
            }*/
            LoginUser userDTO = JSON.parseObject(principal, LoginUser.class);
            //用户权限
            String[] authorities1 = authorities.toArray(new String[ authorities.size()]);

            //将用户信息和权限填充 到用户身份token对象中
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(userDTO,null, AuthorityUtils.createAuthorityList(authorities1));
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            //将authenticationToken填充到安全上下文
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);

    }

    @Override
    public int getOrder() {
        return -100;
    }



}
