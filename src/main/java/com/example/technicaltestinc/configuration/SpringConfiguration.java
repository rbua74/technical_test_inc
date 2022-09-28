package com.example.technicaltestinc.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;

@Configuration
public class SpringConfiguration {


	@Bean
	public RestTemplate restTemplate() {
		RestTemplateBuilder builder = new RestTemplateBuilder();
		RestTemplate restTemplate = builder.build();
		restTemplate.getMessageConverters()
				.add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		return restTemplate;
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}



}
