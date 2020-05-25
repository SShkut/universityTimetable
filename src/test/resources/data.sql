INSERT INTO courses (id, name) VALUES 
	(1, 'CS'),
	(2, 'Math'),
	(3, 'Physics'),
	(4, 'History'),
    (5, 'Chemistry');
	
INSERT INTO course_hierarchy (course_id, prerequisite_id) VALUES
	(1, 2),
	(1, 4),
    (2, 5),
    (3, 5);
	
INSERT INTO semesters (id, year_of_study, period) VALUES
	(1, 2020, 'summer'),
	(2, 2020, 'winter');
	
INSERT INTO groups (id, name, major, department, semester_id) VALUES
	(1, 'cs-1', 'cs', 'cs', 1),
	(2, 'cs-2', 'cs', 'cs', 1),
	(3, 'cs-3', 'cs', 'cs', 2);
	
INSERT INTO students (id, first_name, last_name, tax_number, phone_number, email, student_card_number) VALUES
	(1, 'fn-1', 'ln-1', '123456789', '1234567890', 'ln-1@unv.com', 'cn-123'),
	(2, 'fn-2', 'ln-2', '123456798', '1234567891', 'ln-2@unv.com', 'cn-124'),
	(3, 'fn-3', 'ln-3', '123456987', '1234567892', 'ln-3@unv.com', 'cn-125'),
	(4, 'fn-4', 'ln-4', '123459876', '1234567893', 'ln-4@unv.com', 'cn-126'),
	(5, 'fn-5', 'ln-5', '123498765', '1234567894', 'ln-5@unv.com', 'cn-127');
	
INSERT INTO student_group (student_id, group_id) VALUES
	(1, 1),
	(1, 2),
	(2, 1),
	(3, 2),
	(4, 2);
	
INSERT INTO teachers (id, first_name, last_name, tax_number, phone_number, email, degree) VALUES
	(1, 'fnt-1', 'lnt-1', '223456789', '4234567890', 'lnt-1@unv.com', 'phD'),
	(2, 'fnt-2', 'lnt-2', '323456798', '5234567891', 'lnt-2@unv.com', 'masters');
	
INSERT INTO teacher_course (teacher_id, course_id) VALUES
	(1, 1),
	(1, 3),
	(2, 3),
	(2, 4);
	
INSERT INTO student_course (student_id, course_id) VALUES
	(1, 2),
	(1, 1),
	(2, 3),
	(2, 2);
	
INSERT INTO rooms (id, symbol, capacity) VALUES
	(1, 'a-1', 100),
	(2, 'b-1', 70);