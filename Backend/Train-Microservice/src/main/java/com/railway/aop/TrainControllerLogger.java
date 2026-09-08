package com.railway.aop;

import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TrainControllerLogger {
	
	private Logger logger = Logger.getLogger(TrainControllerLogger.class.getName());
	
	@Before("execution(* com.railway.controller.TrainController.*(..))")
	public void logBefore(JoinPoint joinPoint) {
		logger.info("Entering method: " + joinPoint.getSignature().getName() + " with argument: " + joinPoint.getArgs());
	}
	
	@After("execution(* com.railway.controller.TrainController.*(..))")
	public void logAfter(JoinPoint joinPoint) {
		logger.info("Exiting method: " + joinPoint.getSignature().getName());
	}
	
	@AfterReturning(pointcut = "execution(* com.railway.controller.TrainController.*(..))", returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) {
		logger.info("Method " + joinPoint.getSignature().getName() + " returned with value: " + result);
	}
	

	@AfterThrowing(pointcut = "execution(* com.railway.controller.TrainController.*(..))", throwing = "exception")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
	    logger.severe("Method " + joinPoint.getSignature().getName() + " threw exception: " + exception.getMessage());
	}
}
