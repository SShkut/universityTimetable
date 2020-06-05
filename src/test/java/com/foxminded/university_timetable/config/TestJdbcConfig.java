package com.foxminded.university_timetable.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.foxminded.university_timetable.util.JdbcConfig;

@Configuration
public class TestJdbcConfig extends JdbcConfig {

	@Bean
	public DataSource dataSource(Environment environment) {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:/schema.sql")
				.addScript("classpath:/data.sql").build();
	}
}
