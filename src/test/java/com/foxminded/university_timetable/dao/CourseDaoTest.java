package com.foxminded.university_timetable.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.util.JdbcConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JdbcConfig.class})
class CourseDaoTest {
	
	private CourseDao courseDao;
	
	@Autowired
	@Qualifier("embeddedDataSource")
	private DataSource dataSource;
	
	@BeforeEach
	void setUp() throws Exception {
		IDatabaseTester tester = new DataSourceDatabaseTester(dataSource);
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResource("testData.xml"));
		tester.setDataSet(dataSet);
		tester.onSetup();
		courseDao = new CourseDao(dataSource);
	}

	@Test
	void givenId_whenFindById_thenReturnOptionalOfCourse() {
		Optional<Course> expected = Optional.of(new Course(2L, "CS", new ArrayList<Course>()));
		
		Optional<Course> actual = courseDao.findById(2L);		
		
		assertEquals(expected, actual);
	}

	@Test
	void givenNonExistent_whenFindById_thenRetrunEmptyOptional() {
		Optional<Course> expected = Optional.empty();
		
		Optional<Course> actual = courseDao.findById(0L);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void whenFindAll_thenReturnListOfAllCourses() {
		List<Course> expected = Arrays.asList(new Course(1L, "Math", new ArrayList<>()), new Course(2L, "CS", new ArrayList<>()));
		
		List<Course> actual = courseDao.findAll();
		
		assertEquals(expected, actual);
	}
}
