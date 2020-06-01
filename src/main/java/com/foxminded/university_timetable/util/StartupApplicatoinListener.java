package com.foxminded.university_timetable.util;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

@Component
public class StartupApplicatoinListener implements ApplicationListener<ContextRefreshedEvent> {
	
	private final DataSource dataSource;
	
	@Autowired
	public StartupApplicatoinListener(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		Resource resource = new ClassPathResource("schema.sql");
		Resource data = new ClassPathResource("data.sql");
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScripts(resource, data);
		populator.execute(dataSource);		
	}	
}
