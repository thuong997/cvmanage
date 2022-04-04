package com.bezkoder.springjwt.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAwareProvider")
public class ComponentConfiguration {

    @Bean
    public ModelMapper initModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public AuditorAware<String> auditorAwareProvider() {
        return () ->  Optional.ofNullable("Văn Nguyễn");
    }
}
