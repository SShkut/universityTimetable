package com.foxminded.university_timetable.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.university_timetable.dao.SemesterDao;
import com.foxminded.university_timetable.model.Semester;

@ExtendWith(MockitoExtension.class)
class SemesterServiceTest {
	
	@Mock
	SemesterDao semesterDao;
	
	@InjectMocks
	SemesterService semesterService;

	@Test
	void whenFindAll_thenReturnAllSemesters() {
		List<Semester> expected = new ArrayList<>();
		expected.add(new Semester(1L, 0, ""));
		expected.add(new Semester(2L, 0, ""));

		when(semesterDao.findAll()).thenReturn(expected);
		
		List<Semester> actual = semesterDao.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenExistentSemesterId_whenFindById_thenReturnOptionalOfSemester() {
		Semester semester = new Semester(1L, 0, "");
		Optional<Semester> expected = Optional.of(semester);
		when(semesterDao.findById(semester.getId())).thenReturn(expected);
		
		Optional<Semester> actual = semesterService.findById(semester.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenNonExistentSemesterId_whenFindById_thenReturnEmptyOptional() {
		Semester semester = new Semester(1L, 0, "");
		Optional<Semester> expected = Optional.empty();
		when(semesterDao.findById(semester.getId())).thenReturn(expected);
		
		Optional<Semester> actual = semesterService.findById(semester.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenSemester_whenDelete_thenDeleteSemester() {
		Semester semester = new Semester(1L, 0, "");
		
		semesterService.delete(semester);
		
		verify(semesterDao, times(1)).delete(semester);
	}
	
	@Test
	void givenSemester_whenUpdate_thenUpdateSemester() {
		Semester semester = new Semester(1L, 0, "");
		
		semesterService.update(semester);
		
		verify(semesterDao, times(1)).update(semester);
	}
	
	@Test
	void givenSemester_whenSave_thenSaveSemester() {
		Semester expected = new Semester(1L, 0, "");
		when(semesterDao.save(expected)).thenReturn(expected);
		
		Semester actual = semesterService.save(expected);
		
		assertEquals(expected, actual);
	}
}
