package com.wcwy.company.config;

import org.dromara.dynamictp.common.em.QueueTypeEnum;
import org.dromara.dynamictp.core.support.DynamicTp;
import org.dromara.dynamictp.core.support.ThreadPoolBuilder;
import org.dromara.dynamictp.core.support.ThreadPoolCreator;
import org.dromara.dynamictp.core.thread.DtpExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: DtpConfig
 * Description:
 * date: 2023/7/24 19:56
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
//@Configuration
public class DtpConfig {

    /**
     * 通过 {@link @DynamicTp} 注解定义普通 juc 线程池，会享受到该框架监控功能，注解名称优先级高于方法名
     *
     * @return 线程池实例
     */
    @DynamicTp("commonExecutor")
    @Bean
    public ThreadPoolExecutor commonExecutor() {
        return (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
    }

    /**
     * 通过 {@link ThreadPoolCreator} 快速创建一些简单配置的动态线程池
     * tips: 建议直接在配置中心配置就行，不用 @Bean 声明
     *
     * @return 线程池实例
     */
    @Bean
    public DtpExecutor dtpExecutor1() {
        return ThreadPoolCreator.createDynamicFast("dtpExecutor1");
    }

    /**
     * 通过 {@link ThreadPoolBuilder} 设置详细参数创建动态线程池（推荐方式），
     * ioIntensive，参考 tomcat 线程池设计，实现了处理 io 密集型任务的线程池，具体参数可以看代码注释
     *
     * tips: 建议直接在配置中心配置就行，不用@Bean声明
     * @return 线程池实例
     */
    @Bean
    public DtpExecutor ioIntensiveExecutor() {
        return ThreadPoolBuilder.newBuilder()
                .threadPoolName("ioIntensiveExecutor")
                .corePoolSize(20)
                .maximumPoolSize(50)
                .queueCapacity(2048)
                .ioIntensive(true)
                .buildDynamic();
    }

    /**
     * tips: 建议直接在配置中心配置就行，不用 @Bean 声明
     * @return 线程池实例
     */
    @Bean
    public ThreadPoolExecutor dtpExecutor2() {
        return ThreadPoolBuilder.newBuilder()
                .threadPoolName("dtpExecutor2")
                .corePoolSize(10)
                .maximumPoolSize(15)
                .keepAliveTime(50)
                .timeUnit(TimeUnit.MILLISECONDS)
                .workQueue(QueueTypeEnum.SYNCHRONOUS_QUEUE.getName(), null, false)
                .waitForTasksToCompleteOnShutdown(true)
                .awaitTerminationSeconds(5)
                .buildDynamic();
    }
}
