package com.cbinfo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring4.resourceresolver.SpringResourceResourceResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

/**
 * Created by islabukhin on 16.09.16.
 */
@Configuration
public class ThymeleafConfig {
    @Autowired
    private ThymeleafProperties properties;

    @Bean
    public TemplateResolver defaultTemplateResolver() {
        TemplateResolver resolver = new TemplateResolver();
        resolver.setResourceResolver(thymeleafResourceResolver());
        resolver.setPrefix(this.properties.getPrefix());
        resolver.setSuffix(this.properties.getSuffix());
        resolver.setTemplateMode(this.properties.getMode());
        if (this.properties.getEncoding() != null) {
            resolver.setCharacterEncoding(this.properties.getEncoding().name());
        }
        resolver.setCacheable(this.properties.isCache());
        Integer order = this.properties.getTemplateResolverOrder();
        if (order != null) {
            resolver.setOrder(order);
        }
        return resolver;
    }

    @Bean
    public SpringResourceResourceResolver thymeleafResourceResolver() {
        return new SpringResourceResourceResolver();
    }

}
