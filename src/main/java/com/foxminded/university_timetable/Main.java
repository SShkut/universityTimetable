package com.foxminded.university_timetable;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.foxminded.university_timetable.util.JdbcConfig;
import com.foxminded.university_timetable.util.Menu;

public class Main {

	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(JdbcConfig.class);
		Menu menu = context.getBean(Menu.class);
		menu.showMenu();
	}
}