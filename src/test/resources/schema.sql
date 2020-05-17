DROP TABLE IF EXISTS courses;

CREATE TABLE courses (
	id serial PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	ancestor INTEGER,
	FOREIGN KEY (ancestor) REFERENCES courses(id) ON DELETE SET NULL
);