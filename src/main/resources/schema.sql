DROP TABLE IF EXISTS course_hierarchy;
DROP TABLE IF EXISTS student_group;
DROP TABLE IF EXISTS student_course;
DROP TABLE IF EXISTS teacher_course;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS time_slots;
DROP TABLE IF EXISTS daily_timetables;
DROP TABLE IF EXISTS timetables;
DROP TABLE IF EXISTS teachers;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS semesters;
DROP TABLE IF EXISTS rooms;

CREATE TABLE courses (
	id bigserial PRIMARY KEY,
	name VARCHAR(255) NOT NULL
);

CREATE TABLE course_hierarchy (
	course_id INTEGER,
	prerequisite_id INTEGER,
	FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
	FOREIGN KEY (prerequisite_id) REFERENCES courses(id) ON DELETE CASCADE,
	UNIQUE (course_id, prerequisite_id)
);

CREATE TABLE semesters (
	id bigserial PRIMARY KEY,
	year_of_study INTEGER NOT NULL,
	period VARCHAR(30) NOT NULL
);

CREATE TABLE groups (
	id bigserial PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	major VARCHAR(255) NOT NULL,
	department VARCHAR(255) NOT NULL,
	semester_id INTEGER,
	FOREIGN KEY (semester_id) REFERENCES semesters(id) ON DELETE SET NULL
);

CREATE TABLE students (
	id bigserial PRIMARY KEY,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	tax_number VARCHAR(20) NOT NULL,
	phone_number VARCHAR(11),
	email VARCHAR(255),
	student_card_number VARCHAR(20)
);

CREATE TABLE student_group (
	student_id INTEGER,
	group_id INTEGER,
	FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
	FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE CASCADE,
	UNIQUE (student_id, group_id)
);

CREATE TABLE student_course (
	student_id INTEGER,
	course_id INTEGER,
	FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
	FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
	UNIQUE(student_id, course_id)
);

CREATE TABLE teachers (
	id bigserial PRIMARY KEY,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	tax_number VARCHAR(20) NOT NULL,
	phone_number VARCHAR(11),
	email VARCHAR(255),
	degree VARCHAR(20)
);

CREATE TABLE teacher_course (
	teacher_id INTEGER,
	course_id INTEGER,
	FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE CASCADE,
	FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
	UNIQUE (teacher_id, course_id)
);

CREATE TABLE rooms (
	id bigserial PRIMARY KEY,
	symbol VARCHAR(30) NOT NULL,
	capacity INTEGER
);

CREATE TABLE timetables (
	id bigserial PRIMARY KEY,
	name VARCHAR(255)
);

CREATE TABLE daily_timetables (
	id bigserial PRIMARY KEY,
	date DATE,
	timetable_id INTEGER,
	FOREIGN KEY (timetable_id) REFERENCES timetables(id) ON DELETE CASCADE
);

CREATE TABLE time_slots (
	id bigserial PRIMARY KEY,
	start_time TIME NOT NULL,
	end_time TIME NOT NULL,
	course_id INTEGER,
	teacher_id INTEGER,
	group_id INTEGER,
	room_id INTEGER,
	daily_timetable_id INTEGER,
	FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE SET NULL,
	FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE SET NULL,
	FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE SET NULL,
	FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE SET NULL,
	FOREIGN KEY (daily_timetable_id) REFERENCES daily_timetables(id) ON DELETE SET NULL
);