package com.foxminded.university_timetable.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.foxminded.university_timetable.model.Course;
import com.foxminded.university_timetable.model.DailyTimetable;
import com.foxminded.university_timetable.model.Group;
import com.foxminded.university_timetable.model.Room;
import com.foxminded.university_timetable.model.Semester;
import com.foxminded.university_timetable.model.Student;
import com.foxminded.university_timetable.model.Teacher;
import com.foxminded.university_timetable.model.TimeSlot;
import com.foxminded.university_timetable.model.Timetable;

public class Menu {
	
	private static final Scanner scanner = new Scanner(System.in);
	
	private List<Student> students;
	private List<Teacher> teachers;
	private List<Course> courses;
	private List<Group> groups;
	private List<Room> rooms;
	private Semester semester;
	private Timetable timetable;

	public Menu() {
		this.students = new ArrayList<>();
		this.teachers = new ArrayList<>();
		this.courses = new ArrayList<>();
		this.groups = new ArrayList<>();
		this.rooms = Arrays.asList(new Room("A111", 100), new Room("A112", 100), new Room("B110", 25));
		this.semester = new Semester(2020, "summer");
		this.timetable = new Timetable("My timetable");
	}

	public void showMenu() {
		showPrompt();
		while (scanner.hasNext()) {
			int menuItem = scanner.nextInt();
			if (menuItem > 10) {
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
				createDailyTimetable();
				showPrompt();
			} else if (menuItem == 7) {
				printDailyTimetableForStudent();
				showPrompt();
			} else if (menuItem == 8) {
				printMonthlyTimetableForStudent();
				showPrompt();
			} else if (menuItem == 9) {
				printDailyTimetableForTeacher();
				showPrompt();
			} else if (menuItem == 10) {
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
		students.add(new Student(firstName, lastName, taxNumber, phoneNumber, email, studentCardNumber));
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
		teachers.add(new Teacher(firstName, lastName, taxNumber, phoneNumber, email, degree));
	}

	private void createCourse() {
		System.out.print("Course name: ");
		String name = scanner.next();
		courses.add(new Course(name));
	}

	private void createGroup() {
		System.out.print("Group name: ");
		String name = scanner.next();
		System.out.print("Major: ");
		String major = scanner.next();
		System.out.print("Department: ");
		String department = scanner.next();
		groups.add(new Group(name, major, department, this.semester));
	}
	
	private void assignStudentToGroup() {
		boolean correct = false;
		Optional<Group> group;
		Optional<Student> student;
		do {
			System.out.println("Chose from groups by typing group name.");
			groups.forEach(System.out::println);
			String groupName = scanner.next();
			group = groups.stream()
					.filter(g -> g.getName().equals(groupName))
					.findFirst();
			if (group.isPresent()) {
				correct = true;
			} else {
				System.out.println("There is no such group.");
			}
		} while (!correct);
		correct = false;
		
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
		
		group.get().addStudent(student.get());
	}

	private void createDailyTimetable() {
		boolean correct = false;
		do {
			try {
				System.out.println("Enter date (YYYY-MM-DD): ");
				String text = scanner.next();
				LocalDate date = LocalDate.parse(text);
				Optional<DailyTimetable> dailyTimetable = this.timetable.getTimetables().stream()
						.filter(table -> table.getDate().equals(date))
						.findFirst();
				if (dailyTimetable.isPresent()) {
					createTimeSlot(dailyTimetable.get());
					correct = true;
				} else {
					dailyTimetable = Optional.of(new DailyTimetable(date));
					timetable.getTimetables().add(dailyTimetable.get());
					createTimeSlot(dailyTimetable.get());
					correct = true;
				}
			} catch (DateTimeParseException e) {
				System.out.println("Incorrect date format.");
			}
		} while (!correct);
	}
	
	private void createTimeSlot(DailyTimetable dailyTimetable) {
		Optional<Course> course;
		Optional<Teacher> teacher;
		Optional<Group> group;
		Optional<Room> room;
		boolean correct = false;
		do {
			System.out.println("Chose from courses by typing course name.");
			courses.forEach(System.out::println);
			String courseName = scanner.next();
			course = courses.stream()
					.filter(c -> c.getName().equals(courseName))
					.findFirst();
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
			group = groups.stream()
					.filter(g -> g.getName().equals(groupName))
					.findFirst();
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
			room = rooms.stream()
					.filter(r -> r.getSybmol().equals(roomSymbol))
					.findFirst();
			if (room.isPresent()) {
				correct = true;
			} else {
				System.out.println("There is no such room.");
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
				TimeSlot timeSlot = new TimeSlot(startTime, endTime, course.get(), teacher.get(), group.get(), room.get());
				dailyTimetable.addTimeSlot(timeSlot);
				correct = true;
			} catch (DateTimeParseException | NumberFormatException e) {
				System.out.println("Incorrect time format.");
			}
		} while (!correct);
	}

	private void printDailyTimetableForStudent() {
		System.out.print("Enter date (YYYY-MM-DD): ");
		String text = scanner.next();
		LocalDate date;
		boolean correct = false;
		do {
			try {
				date = LocalDate.parse(text);				
			} catch (DateTimeParseException e) {
				System.out.println("Incorrect date format.");
				continue;
			}
			System.out.println("Chose from students by typing student full name.");
			students.forEach(System.out::println);
			String firstName = scanner.next();
			String lastName = scanner.next();
			Optional<Student> student = findStudent(firstName, lastName);
			if (student.isPresent()) {
				Optional<DailyTimetable> dailyTimetable = timetable.getTimetableDay(date, student.get());
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
		System.out.print("Enter date (YYYY-MM-DD): ");
		String text = scanner.next();
		LocalDate date;
		boolean correct = false;
		do {
			try {
				date = LocalDate.parse(text);				
			} catch (DateTimeParseException e) {
				System.out.println("Incorrect date format.");
				continue;
			}
			System.out.println("Chose from teachers by typing teacher full name.");
			teachers.forEach(System.out::println);
			String firstName = scanner.next();
			String lastName = scanner.next();
			Optional<Teacher> teacher = findTeacher(firstName, lastName);
			if (teacher.isPresent()) {
				Optional<DailyTimetable> dailyTimetable = timetable.getTimetableDay(date, teacher.get());
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
				List<DailyTimetable> dailyTimetable = timetable.getTimetableMonth(month, student.get());
				if (!dailyTimetable.isEmpty()) {
					dailyTimetable.forEach(System.out::println);
					correct = true;
				} else {
					System.out.println("Threre is no timetable for month: " + String.valueOf(month) + " and student: " + student.get());
				}
			} else {
				System.out.println("There is no such student.");
			}
		} while (!correct);
	}
	
	private void printMonthlyTimetableForTeacher() {
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
				List<DailyTimetable> dailyTimetable = timetable.getTimetableMonth(month, teacher.get());
				if (!dailyTimetable.isEmpty()) {
					dailyTimetable.forEach(System.out::println);
					correct = true;
				} else {
					System.out.println("Threre is no timetable for month: " + String.valueOf(month) + " and teacher: " + teacher.get());
				}
			} else {
				System.out.println("There is no such teacher.");
			}
		} while (!correct);
	}
	
	private Optional<Student> findStudent(String firstName, String lastName) {
		return students.stream()
				.filter(t -> t.getFirstName().equals(firstName) && t.getLastName().equals(lastName))
				.findFirst();
	}
	
	private Optional<Teacher> findTeacher(String firstName, String lastName) {
		return teachers.stream()
				.filter(t -> t.getFirstName().equals(firstName) && t.getLastName().equals(lastName))
				.findFirst();
	}
	
	private void showPrompt() {
		System.out.println("-------------------------------------");
		System.out.println("1. Create student.");
		System.out.println("2. Create teacher.");
		System.out.println("3. Create course.");
		System.out.println("4. Create group.");
		System.out.println("5. Assign student to group.");
		System.out.println("6. Cretae daily timetable.");
		System.out.println("7. Get daily timetable for student.");
		System.out.println("8. Get monthly timetable for student.");
		System.out.println("9. Get daily timetable for teacher.");
		System.out.println("10. Get monthly timetable for student.");
		System.out.println("-------------------------------------");
	}
}
