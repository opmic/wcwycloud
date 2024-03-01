package com.wcwy.post.session.impl;

import com.wcwy.common.config.session.LoginUser;
import com.wcwy.post.session.CompanyMetadata;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


/**
 * ClassName: CompanyMetadataIpml
 * Description:
 * date: 2022/9/1 16:02
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Service
public class CompanyMetadataIpml implements CompanyMetadata {
    @Override
    public String userid() {

        LoginUser loginUser =(LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser.getId();
    }
    @Override
    public String userName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(principal) ) {
            return null;
        }
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser.getUsername();
    }
}
