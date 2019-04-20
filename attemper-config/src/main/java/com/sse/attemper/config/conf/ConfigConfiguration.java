package com.sse.attemper.config.conf;

import com.sse.attemper.config.aspect.MultiDataSourceAspect;
import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.config.dao.repo.ApiLogRepository;
import com.sse.attemper.config.datasource.DataSourceConfig;
import com.sse.attemper.config.datasource.DataSourceProperties;
import com.sse.attemper.config.exception.GlobalExceptionAdvicer;
import com.sse.attemper.config.id.SnowFlakeIdGenerator;
import com.sse.attemper.config.property.AppProperties;
import com.sse.attemper.config.service.ApiLogService;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @auth ldang
 */
@EnableConfigurationProperties(
        AppProperties.class
)
@Configuration
@ComponentScan(basePackageClasses = {
        //多数据源切面
        MultiDataSourceAspect.class,
        //数据源属性
        DataSourceProperties.class,
        //多数据配置
        DataSourceConfig.class,

        //全局异常处理
        GlobalExceptionAdvicer.class,

        //swagger2配置
        Swagger2Config.class,

        //bean factory bean
        ContextBeanAware.class,

        //log
        ApiLogService.class,
        ApiLogRepository.class
})
public class ConfigConfiguration {

    @Autowired
    private AppProperties appProperties;

    @Bean
    public IdGenerator idGenerator() {
        return new SnowFlakeIdGenerator(
                appProperties.getSnowFlake().getDataCenterId(),
                appProperties.getSnowFlake().getMachineId());
    }

}
