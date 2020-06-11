package com.foxminded.university_timetable.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.foxminded.university_timetable.dao.CourseDao;

@Configuration
@Profile("test")
@ComponentScan("com.foxminded.university_timetable")
public class MockConfituration {

	@Bean
	public CourseDao courseDao() {
		return Mockito.mock(CourseDao.class);
	}
}
