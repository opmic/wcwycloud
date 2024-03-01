package com.wcwy.company.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: ChangeIdentity
 * Description:
 * date: 2023/1/16 14:15
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Configuration
@Data
public class ChangeIdentity {
    @Value("${updateToken}")
    public String path;
}
