package com.foxminded.university_timetable.util;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@ComponentScan("com.foxminded.university_timetable.dao")
public class SpringJdbcConfig {

	@Bean
	public DataSource postgresDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("");
		dataSource.setUsername("admin");
		dataSource.setPassword("admin");
		
		return dataSource;
	}
	
	@Bean
	public DataSource embeddedDataSource() {
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("schema")
				.addScript("test-data").build();
	}
}
