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
public class FareControllerLogger {
	
	private Logger logger = Logger.getLogger(FareControllerLogger.class.getName());
	
	@Before("execution(* com.railway.controller.FareController.*(..))")
	public void logBefore(JoinPoint joinPoint) {
		logger.info("Entering method: " + joinPoint.getSignature().getName() + " with argument: " + joinPoint.getArgs());
	}
	
	@After("execution(* com.railway.controller.FareController.*(..))")
	public void logAfter(JoinPoint joinPoint) {
		logger.info("Exiting method: " + joinPoint.getSignature().getName());
	}
	
	@AfterReturning(pointcut = "execution(* com.railway.controller.FareController.*(..))", returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) {
		logger.info("Method " + joinPoint.getSignature().getName() + " returned with value: " + result);
	}
	

	@AfterThrowing(pointcut = "execution(* com.railway.controller.FareController.*(..))", throwing = "exception")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
	    logger.severe("Method " + joinPoint.getSignature().getName() + " threw exception: " + exception.getMessage());
	}
}
