package com.foxminded.university_timetable.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.foxminded.university_timetable.dao.TimetableDao;
import com.foxminded.university_timetable.model.Timetable;

@ExtendWith(MockitoExtension.class)
class TimetableServiceTest {

	@Mock
	private TimetableDao timetableDao;
	
	@InjectMocks
	private TimetableService timetableService;
	
	@Test
	void whenFindAll_thenReturnAllTimetables() {
		List<Timetable> expected = new ArrayList<>();
		expected.add(new Timetable(1L, ""));
		expected.add(new Timetable(1L, ""));

		when(timetableDao.findAll()).thenReturn(expected);
		
		List<Timetable> actual = timetableService.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenExistentTimetableId_whenFindById_thenReturnOptionalOfTimetable() {
		Timetable timetable = new Timetable(1L, "");
		Optional<Timetable> expected = Optional.of(timetable);
		when(timetableDao.findById(timetable.getId())).thenReturn(expected);
		
		Optional<Timetable> actual = timetableService.findById(timetable.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenNonExistentTimetableId_whenFindById_thenReturnEmptyOptional() {
		Timetable timetable = new Timetable(-1L, "");
		Optional<Timetable> expected = Optional.empty();
		when(timetableDao.findById(timetable.getId())).thenReturn(expected);
		
		Optional<Timetable> actual = timetableService.findById(timetable.getId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenTimetable_whenDelete_thenDeleteTimetable() {
		Timetable timetable = new Timetable(1L, "");
		
		timetableService.delete(timetable);
		
		verify(timetableDao).delete(timetable);
	}
	
	@Test
	void givenTimetable_whenUpdate_thenUpdateTimetable() {
		Timetable timetable = new Timetable(1L, "");
		
		timetableService.update(timetable);
		
		verify(timetableDao).update(timetable);
	}
	
	@Test
	void givenTimetable_whenSave_thenSaveTimetable() {
		Timetable expected = new Timetable(1L, "");
		when(timetableDao.save(expected)).thenReturn(expected);
		
		Timetable actual = timetableService.save(expected);
		
		assertEquals(expected, actual);
	}
}
