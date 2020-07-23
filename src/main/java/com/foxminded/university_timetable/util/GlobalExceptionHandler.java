package com.foxminded.university_timetable.util;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public ModelAndView handleAllException(Exception exception, HttpServletRequest request) {
		logger.error("Request: {} raised {}", request.getRequestURI(), exception);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("exceptionMessage", exception.getMessage());
		modelAndView.setViewName("errors/error");
		return modelAndView;
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public String handleNotFoundError(HttpServletRequest request) {
		logger.error("Request: {} caused 404 error", request.getRequestURI());
		return "errors/not-found";
	}
}
