package com.biboheart.huip.reservation.basejpa;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableJpaRepositories(repositoryFactoryBeanClass = CustomRepositoryFactoryBean.class, basePackages = "com.biboheart")
public class Config {}
