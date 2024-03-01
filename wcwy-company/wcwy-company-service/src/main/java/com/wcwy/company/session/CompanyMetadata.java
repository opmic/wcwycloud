package com.wcwy.company.session;


import com.wcwy.common.base.result.R;
import org.springframework.stereotype.Component;

/**
 * ClassName: CompanyMetadata
 * Description:获取用户信息
 * date: 2022/9/1 16:01
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Component
public interface CompanyMetadata {
    /**
     * @Description: 获取用户id
     * @return null
     * @Author tangzhuo
     * @CreateTime 2022/9/1 16:02
     */
    String userid();


    String userName();
}