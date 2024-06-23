package com.example.springdemo.config;
import com.example.springdemo.filters.SessionFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<Filter> sessionFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SessionFilter());
        registrationBean.addUrlPatterns("/personal/*", "/posting/*");
        return registrationBean;
    }
}