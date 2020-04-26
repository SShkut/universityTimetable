DROP TABLE IF EXISTS courses;

CREATE TABLE courses (
	id serial PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	prerequisite_for INTEGER,
	FOREIGN KEY (prerequisite_for) REFERENCES courses(id) ON DELETE SET NULL
);