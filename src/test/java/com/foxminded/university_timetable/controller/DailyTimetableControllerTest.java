package com.foxminded.university_timetable.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.model.Teacher;
import com.foxminded.university_timetable.service.DailyTimetableService;
import com.foxminded.university_timetable.service.StudentService;
import com.foxminded.university_timetable.service.TeacherService;

@ExtendWith(MockitoExtension.class)
class DailyTimetableControllerTest {

	private MockMvc mockMvc;

	@Mock
	DailyTimetableService dailyTimetableService;

	@Mock
	StudentService studentService;

	@Mock
	TeacherService teacherService;

	@InjectMocks
	DailyTimetableController dailyTimetableController;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(dailyTimetableController).build();
	}

	@Test
	void givenModel_whenFindAll_thenShowAllDailyTimetables() throws Exception {
		List<DailyTimetable> dailyTimetables = new ArrayList<>();
		dailyTimetables.add(new DailyTimetable(1L, LocalDate.now(), new ArrayList<>()));
		dailyTimetables.add(new DailyTimetable(2L, LocalDate.now(), new ArrayList<>()));
		when(dailyTimetableService.findAll()).thenReturn(dailyTimetables);

		mockMvc.perform(get("/daily-timetables"))
			.andExpect(status().isOk())
			.andExpect(view().name("daily-timetables/daily-timetables"))
			.andExpect(model().attribute("dailyTimetables", hasSize(2)));
	}

	@Test
	void givenModel_whenFindById_thenShowDailyTimetable() throws Exception {
		DailyTimetable expected = new DailyTimetable(1L, LocalDate.now(), new ArrayList<>());
		Optional<DailyTimetable> dailyTimetable = Optional.of(expected);
		when(dailyTimetableService.findById(1L)).thenReturn(dailyTimetable);

		mockMvc.perform(get("/daily-timetables/1"))
			.andExpect(status().isOk())
			.andExpect(view().name("daily-timetables/daily-timetable"))
			.andExpect(model().attributeExists("dailyTimetable"))
			.andExpect(model().attribute("dailyTimetable", expected));
	}

	@Test
	void givenCommandObjectModelStudentId_whenFindTimetableForStudent_thenShowFormForDateRangeChose() throws Exception {
		Student student = new Student(1L, "", "", "", "", "", "");
		LocalDate start = LocalDate.of(2020, 1, 1);
		LocalDate end = LocalDate.of(2020, 2, 2);
		List<DailyTimetable> dailyTimetables = new ArrayList<>();
		dailyTimetables.add(new DailyTimetable(1L, LocalDate.now(), new ArrayList<>()));
		dailyTimetables.add(new DailyTimetable(2L, LocalDate.now(), new ArrayList<>()));
		when(studentService.findById(1L)).thenReturn(Optional.of(student));
		when(dailyTimetableService.findTimetableForStudent(student, start, end)).thenReturn(dailyTimetables);

		mockMvc.perform(get("/daily-timetables/student/1")
				.param("dateFrom", "2020-01-01")
				.param("dateTo", "2020-02-02"))
			.andExpect(status().isOk())
			.andExpect(view().name("daily-timetables/student-timetables"))
			.andExpect(model().attributeExists("dailyTimetables", "student"))
			.andExpect(model().attribute("dailyTimetables", hasSize(2)));
	}

	@Test
	void givenCommandObjectModelTeacherId_whenFindTimetableForStudent_thenShowFormForDateRangeChose() throws Exception {
		Teacher teacher = new Teacher(1L, "", "", "", "", "", "");
		LocalDate start = LocalDate.of(2020, 1, 1);
		LocalDate end = LocalDate.of(2020, 2, 2);
		List<DailyTimetable> dailyTimetables = new ArrayList<>();
		dailyTimetables.add(new DailyTimetable(1L, LocalDate.now(), new ArrayList<>()));
		dailyTimetables.add(new DailyTimetable(2L, LocalDate.now(), new ArrayList<>()));
		when(teacherService.findById(1L)).thenReturn(Optional.of(teacher));
		when(dailyTimetableService.findTimetableForTeacher(teacher, start, end)).thenReturn(dailyTimetables);

		mockMvc.perform(get("/daily-timetables/teacher/1")
				.param("dateFrom", "2020-01-01")
				.param("dateTo", "2020-02-02"))
			.andExpect(status().isOk()).andExpect(view().name("daily-timetables/teacher-timetables"))
			.andExpect(model().attributeExists("dailyTimetables", "teacher"))
			.andExpect(model().attribute("dailyTimetables", hasSize(2)));
	}
}
