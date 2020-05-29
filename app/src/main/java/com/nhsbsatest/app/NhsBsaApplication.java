package com.nhsbsatest.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan(basePackages = "com.nhsbsatest")
@EntityScan(basePackages = "com.nhsbsatest.domain.entity")
@EnableJpaRepositories(basePackages = "com.nhsbsatest.domain.repository")
@EnableAutoConfiguration
@EnableSwagger2
public class NhsBsaApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(NhsBsaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(NhsBsaApplication.class, args);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Person")
                .select()
                .paths(PathSelectors.ant("/person/**"))
                .build();
    }

    @PostConstruct
    public void setup() {
        // anything else
    }
}
