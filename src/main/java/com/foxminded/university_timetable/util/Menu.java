package com.foxminded.university_timetable.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foxminded.university_timetable.dao.CourseDao;
import com.foxminded.university_timetable.dao.DailyTimetableDao;
import com.foxminded.university_timetable.dao.GroupDao;
import com.foxminded.university_timetable.dao.RoomDao;
import com.foxminded.university_timetable.dao.SemesterDao;
import com.foxminded.university_timetable.dao.StudentDao;
import com.foxminded.university_timetable.dao.TeacherDao;
import com.foxminded.university_timetable.dao.TimeSlotDao;
import com.foxminded.university_timetable.dao.TimetableDao;
import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Room;
import com.foxminded.university_timetable.model.Semester;
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.model.Teacher;
import com.foxminded.university_timetable.model.TimeSlot;
import com.foxminded.university_timetable.model.Timetable;

@Component
public class Menu {

	private static final Scanner scanner = new Scanner(System.in);

	private final StudentDao studentDao;
	private final TeacherDao teacherDao;
	private final CourseDao courseDao;
	private final GroupDao groupDao;
	private final RoomDao roomDao;
	private final SemesterDao semesterDao;
	private final TimetableDao timetableDao;
	private final DailyTimetableDao dailyTimetableDao;
	private final TimeSlotDao timeSlotDao;

	@Autowired
	public Menu(StudentDao studentDao, TeacherDao teacherDao, CourseDao courseDao, GroupDao groupDao, RoomDao roomDao,
			SemesterDao semesterDao, TimetableDao timetableDao, DailyTimetableDao dailyTimetableDao,
			TimeSlotDao timeSlotDao) {
		this.studentDao = studentDao;
		this.teacherDao = teacherDao;
		this.courseDao = courseDao;
		this.groupDao = groupDao;
		this.roomDao = roomDao;
		this.semesterDao = semesterDao;
		this.timetableDao = timetableDao;
		this.dailyTimetableDao = dailyTimetableDao;
		this.timeSlotDao = timeSlotDao;
	}

	public void showMenu() {
		showPrompt();
		while (scanner.hasNext()) {
			int menuItem = scanner.nextInt();
			if (menuItem > 12) {
				System.out.println("Chose correct menu item.");
			} else if (menuItem == 1) {
				createStudent();
				showPrompt();
			} else if (menuItem == 2) {
				createTeacher();
				showPrompt();
			} else if (menuItem == 3) {
				createCourse();
				showPrompt();
			} else if (menuItem == 4) {
				createGroup();
				showPrompt();
			} else if (menuItem == 5) {
				assignStudentToGroup();
				showPrompt();
			} else if (menuItem == 6) {
				createTimetable();
				showPrompt();
			} else if (menuItem == 7) {
				createDailyTimetable();
				showPrompt();
			} else if (menuItem == 8) {
				createTimeSlot();
				showPrompt();
			} else if (menuItem == 9) {
				printDailyTimetableForStudent();
				showPrompt();
			} else if (menuItem == 10) {
				printMonthlyTimetableForStudent();
				showPrompt();
			} else if (menuItem == 11) {
				printDailyTimetableForTeacher();
				showPrompt();
			} else if (menuItem == 12) {
				printMonthlyTimetableForTeacher();
				showPrompt();
			}
		}
	}

	private void createStudent() {
		System.out.print("First Name: ");
		String firstName = scanner.next();
		System.out.print("Last Name: ");
		String lastName = scanner.next();
		System.out.print("Tax number: ");
		String taxNumber = scanner.next();
		System.out.print("Phone number: ");
		String phoneNumber = scanner.next();
		System.out.print("Email: ");
		String email = scanner.next();
		System.out.print("Student card number: ");
		String studentCardNumber = scanner.next();
		studentDao.save(new Student(firstName, lastName, taxNumber, phoneNumber, email, null, studentCardNumber));
	}

	private void createTeacher() {
		System.out.print("First Name: ");
		String firstName = scanner.next();
		System.out.print("Last Name: ");
		String lastName = scanner.next();
		System.out.print("Tax number: ");
		String taxNumber = scanner.next();
		System.out.print("Phone number: ");
		String phoneNumber = scanner.next();
		System.out.print("Email: ");
		String email = scanner.next();
		System.out.print("Degree: ");
		String degree = scanner.next();
		teacherDao.save(new Teacher(firstName, lastName, taxNumber, phoneNumber, email, null, degree));
	}

	private void createCourse() {
		System.out.print("Course name: ");
		String name = scanner.next();
		courseDao.save(new Course(null, name, null));
	}

	private void createGroup() {
		System.out.print("Group name: ");
		String name = scanner.next();
		System.out.print("Major: ");
		String major = scanner.next();
		System.out.print("Department: ");
		String department = scanner.next();

		boolean correct = false;
		List<Semester> semesters = semesterDao.findAll();
		Optional<Semester> semester;
		do {
			System.out.println("Chose from semesters by typing semester id.");
			semesters.forEach(System.out::println);
			Long id = scanner.nextLong();
			semester = semesterDao.findById(id);
			if (semester.isPresent()) {
				correct = true;
			} else {
				System.out.println("There is no such group.");
			}
		} while (!correct);
		correct = false;
		groupDao.save(new Group(name, major, department, semester.get(), null));
	}

	private void assignStudentToGroup() {
		boolean correct = false;
		List<Group> groups = groupDao.findAll();
		Optional<Group> group;
		Optional<Student> student;
		do {
			System.out.println("Chose from groups by typing group name.");
			groups.forEach(System.out::println);
			String groupName = scanner.next();
			group = groups.stream().filter(g -> g.getName().equals(groupName)).findFirst();
			if (group.isPresent()) {
				correct = true;
			} else {
				System.out.println("There is no such group.");
			}
		} while (!correct);
		correct = false;

		List<Student> students = studentDao.findAll();
		do {
			System.out.println("Chose from students by typing student full name.");
			students.forEach(System.out::println);
			String firstName = scanner.next();
			String lastName = scanner.next();
			student = findStudent(firstName, lastName);
			if (student.isPresent()) {
				correct = true;
			} else {
				System.out.println("There is no such student.");
			}
		} while (!correct);
		studentDao.addStudentToGroup(student.orElseThrow(NoSuchElementException::new),
				group.orElseThrow(NoSuchElementException::new));
	}

	private void createTimetable() {
		System.out.println("Timetable name: ");
		String name = scanner.next();
		timetableDao.save(new Timetable(null, name, null));
	}

	private void createDailyTimetable() {
		List<Timetable> timetables = timetableDao.findAll();
		Optional<Timetable> timetable;
		Optional<DailyTimetable> dailyTimetable;
		boolean correct = false;
		do {
			System.out.println("Chose from timetables by typing timetable name.");
			timetables.forEach(System.out::println);
			String timetableName = scanner.next();
			timetable = timetables.stream().filter(g -> g.getName().equals(timetableName)).findFirst();
			if (timetable.isPresent()) {
				correct = true;
			} else {
				System.out.println("There is no such dailyTimetable.");
			}
		} while (!correct);
		correct = false;

		do {
			try {
				System.out.println("Enter date (YYYY-MM-DD): ");
				String text = scanner.next();
				LocalDate date = LocalDate.parse(text);
				dailyTimetable = dailyTimetableDao.findByDate(date);
				if (!dailyTimetable.isPresent()) {
					dailyTimetable = Optional.of(dailyTimetableDao.save(new DailyTimetable(null, date, null)));
				}
				dailyTimetableDao.addDailyTimetableToTimetable(dailyTimetable.orElseThrow(NoSuchElementException::new),
						timetable.orElseThrow(NoSuchElementException::new));
				correct = true;
			} catch (DateTimeParseException e) {
				System.out.println("Incorrect date format.");
			}
		} while (!correct);
	}

	private void createTimeSlot() {
		List<Course> courses = courseDao.findAll();
		List<Group> groups = groupDao.findAll();
		List<Teacher> teachers = teacherDao.findAll();
		List<Room> rooms = roomDao.findAll();
		List<Timetable> timetables = timetableDao.findAll();
		Optional<Course> course;
		Optional<Teacher> teacher;
		Optional<Group> group;
		Optional<Room> room;
		Optional<DailyTimetable> dailyTimetable = Optional.empty();
		Optional<Timetable> timetable;
		boolean correct = false;
		do {
			System.out.println("Chose timetable by typing timetable name.");
			timetables.forEach(System.out::println);
			String timetableName = scanner.next();
			timetable = timetables.stream().filter(c -> c.getName().equals(timetableName)).findFirst();
			if (timetable.isPresent()) {
				correct = true;
			} else {
				System.out.println("There is no such course.");
			}
		} while (!correct);
		correct = false;

		do {
			System.out.println("Chose from courses by typing course name.");
			courses.forEach(System.out::println);
			String courseName = scanner.next();
			course = courses.stream().filter(c -> c.getName().equals(courseName)).findFirst();
			if (course.isPresent()) {
				correct = true;
			} else {
				System.out.println("There is no such course.");
			}
		} while (!correct);
		correct = false;

		do {
			System.out.println("Chose from groups by typing group name.");
			groups.forEach(System.out::println);
			String groupName = scanner.next();
			group = groups.stream().filter(g -> g.getName().equals(groupName)).findFirst();
			if (group.isPresent()) {
				correct = true;
			} else {
				System.out.println("There is no such group.");
			}
		} while (!correct);
		correct = false;

		do {
			System.out.println("Chose from teachers by typing teacher full name.");
			teachers.forEach(System.out::println);
			String firstName = scanner.next();
			String lastName = scanner.next();
			teacher = findTeacher(firstName, lastName);
			if (teacher.isPresent()) {
				correct = true;
			} else {
				System.out.println("There is no such teacher.");
			}
		} while (!correct);
		correct = false;

		do {
			System.out.println("Chose from room by typing room symbol.");
			rooms.forEach(System.out::println);
			String roomSymbol = scanner.next();
			room = rooms.stream().filter(r -> r.getSybmol().equals(roomSymbol)).findFirst();
			if (room.isPresent()) {
				correct = true;
			} else {
				System.out.println("There is no such room.");
			}
		} while (!correct);
		correct = false;

		do {
			try {
				System.out.println("Enter date (YYYY-MM-DD): ");
				String text = scanner.next();
				LocalDate date = LocalDate.parse(text);
				dailyTimetable = dailyTimetableDao.findByDate(date);
				dailyTimetableDao.addDailyTimetableToTimetable(dailyTimetable.orElseThrow(NoSuchElementException::new),
						timetable.orElseThrow(NoSuchElementException::new));
				correct = true;
			} catch (DateTimeParseException e) {
				System.out.println("Incorrect date format.");
			}
		} while (!correct);
		correct = false;

		do {
			System.out.print("Enter start time (HH:MM)");
			String start = scanner.next();
			System.out.print("Enter end time (HH:MM)");
			String end = scanner.next();
			try {
				LocalTime startTime = LocalTime.parse(start, DateTimeFormatter.ofPattern("HH:mm"));
				LocalTime endTime = LocalTime.parse(end, DateTimeFormatter.ofPattern("HH:mm"));
				TimeSlot timeSlot = timeSlotDao
						.save(new TimeSlot(startTime, endTime, course.get(), teacher.get(), group.get(), room.get()));
				timeSlotDao.addTimeSlotToDailyTimetable(timeSlot,
						dailyTimetable.orElseThrow(NoSuchElementException::new));
				correct = true;
			} catch (DateTimeParseException | NumberFormatException e) {
				System.out.println("Incorrect time format.");
			}
		} while (!correct);
	}

	private void printDailyTimetableForStudent() {
		List<Student> students = studentDao.findAll();
		LocalDate date = LocalDate.MIN;
		boolean correct = false;
		do {
			try {
				System.out.println("Enter date (YYYY-MM-DD): ");
				String text = scanner.next();
				date = LocalDate.parse(text);
				correct = true;
			} catch (DateTimeParseException e) {
				System.out.println("Incorrect date format.");
			}
		} while (!correct);
		correct = false;

		do {
			System.out.println("Chose from students by typing student full name.");
			students.forEach(System.out::println);
			String firstName = scanner.next();
			String lastName = scanner.next();
			Optional<Student> student = findStudent(firstName, lastName);
			if (student.isPresent()) {
				Optional<DailyTimetable> dailyTimetable = dailyTimetableDao
						.findDailyTimetableForStudent(student.orElseThrow(NoSuchElementException::new), date);
				if (dailyTimetable.isPresent()) {
					System.out.println(dailyTimetable.get());
					correct = true;
				} else {
					System.out.println("Threre is no timetable for date: " + date + " and student: " + student.get());
				}
			} else {
				System.out.println("There is no such student.");
			}
		} while (!correct);
	}

	private void printDailyTimetableForTeacher() {
		List<Teacher> teachers = teacherDao.findAll();
		LocalDate date = LocalDate.MIN;
		boolean correct = false;
		do {
			try {
				System.out.println("Enter date (YYYY-MM-DD): ");
				String text = scanner.next();
				date = LocalDate.parse(text);
				correct = true;
			} catch (DateTimeParseException e) {
				System.out.println("Incorrect date format.");
			}
		} while (!correct);
		correct = false;

		do {
			System.out.println("Chose from teachers by typing teacher full name.");
			teachers.forEach(System.out::println);
			String firstName = scanner.next();
			String lastName = scanner.next();
			Optional<Teacher> teacher = findTeacher(firstName, lastName);
			if (teacher.isPresent()) {
				Optional<DailyTimetable> dailyTimetable = dailyTimetableDao
						.findDailyTimetableForTeacher(teacher.orElseThrow(NoSuchElementException::new), date);
				if (dailyTimetable.isPresent()) {
					System.out.println(dailyTimetable.get());
					correct = true;
				} else {
					System.out.println("Threre is no timetable for date: " + date + " and teacher: " + teacher.get());
				}
			} else {
				System.out.println("There is no such teacher.");
			}
		} while (!correct);
	}

	private void printMonthlyTimetableForStudent() {
		List<Student> students = studentDao.findAll();
		Month month;
		boolean correct = false;
		do {
			try {
				System.out.print("Enter the name of a desired month: ");
				String monthAsString = scanner.next();
				month = Month.valueOf(monthAsString.toUpperCase());
			} catch (IllegalArgumentException e) {
				System.out.println("Incorrect month name.");
				continue;
			}
			System.out.println("Chose from students by typing student full name.");
			students.forEach(System.out::println);
			String firstName = scanner.next();
			String lastName = scanner.next();
			Optional<Student> student = findStudent(firstName, lastName);
			if (student.isPresent()) {
				List<DailyTimetable> dailyTimetables = dailyTimetableDao
						.findMonthlyTimetableForStudent(student.orElseThrow(NoSuchElementException::new), month, 2020);
				if (!dailyTimetables.isEmpty()) {
					dailyTimetables.forEach(System.out::println);
					correct = true;
				} else {
					System.out.println("Threre is no timetable for month: " + String.valueOf(month) + " and student: "
							+ student.get());
				}
			} else {
				System.out.println("There is no such student.");
			}
		} while (!correct);
	}

	private void printMonthlyTimetableForTeacher() {
		List<Teacher> teachers = teacherDao.findAll();
		Month month;
		boolean correct = false;
		do {
			try {
				System.out.print("Enter fullname of a desired month: ");
				String monthAsString = scanner.next();
				month = Month.valueOf(monthAsString.toUpperCase());
			} catch (IllegalArgumentException e) {
				System.out.println("Incorrect month name.");
				continue;
			}
			System.out.println("Chose from teachers by typing teacher full name.");
			teachers.forEach(System.out::println);
			String firstName = scanner.next();
			String lastName = scanner.next();
			Optional<Teacher> teacher = findTeacher(firstName, lastName);
			if (teacher.isPresent()) {
				List<DailyTimetable> dailyTimetable = dailyTimetableDao
						.findMonthlyTimetableForTeacher(teacher.orElseThrow(NoSuchElementException::new), month, 2020);
				if (!dailyTimetable.isEmpty()) {
					dailyTimetable.forEach(System.out::println);
					correct = true;
				} else {
					System.out.println("Threre is no timetable for month: " + String.valueOf(month) + " and teacher: "
							+ teacher.get());
				}
			} else {
				System.out.println("There is no such teacher.");
			}
		} while (!correct);
	}

	private Optional<Student> findStudent(String firstName, String lastName) {
		List<Student> students = studentDao.findAll();
		return students.stream().filter(t -> t.getFirstName().equals(firstName) && t.getLastName().equals(lastName))
				.findFirst();
	}

	private Optional<Teacher> findTeacher(String firstName, String lastName) {
		List<Teacher> teachers = teacherDao.findAll();
		return teachers.stream().filter(t -> t.getFirstName().equals(firstName) && t.getLastName().equals(lastName))
				.findFirst();
	}

	private void showPrompt() {
		System.out.println("-------------------------------------");
		System.out.println("1. Create student.");
		System.out.println("2. Create teacher.");
		System.out.println("3. Create course.");
		System.out.println("4. Create group.");
		System.out.println("5. Assign student to group.");
		System.out.println("6. Create timetable.");
		System.out.println("7. Cretae daily timetable.");
		System.out.println("8. Cretae time slot.");
		System.out.println("9. Get daily timetable for student.");
		System.out.println("10. Get monthly timetable for student.");
		System.out.println("11. Get daily timetable for teacher.");
		System.out.println("12. Get monthly timetable for student.");
		System.out.println("-------------------------------------");
	}
}
