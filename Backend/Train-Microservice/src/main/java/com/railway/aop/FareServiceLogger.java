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
public class FareServiceLogger {
	
	private Logger logger = Logger.getLogger(FareServiceLogger.class.getName());
	
	@Before("execution(* com.railway.service.FareServiceImpl.*(..))")
	public void logBeforeMethod(JoinPoint joinPoint) {
		logger.info("Entering Method: " + joinPoint.getSignature().getName() + "with arguments : " + joinPoint.getArgs());
	}
	
	@After("execution(* com.railway.service.FareServiceImpl.*(..))")
	public void logAfterMethod(JoinPoint joinPoint) {
		logger.info("Exiting Method: " + joinPoint.getSignature().getName());
	}
	
	@AfterReturning(pointcut = "execution(* com.railway.service.FareServiceImpl.*(..))", returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) {
		logger.info("Method: "+joinPoint.getSignature().getName() + "returns result: " + result);
	}
	
	@AfterThrowing(pointcut = "execution(* com.railway.service.FareServiceImpl.*(..))", throwing = "exception")
	public void logAfterThrowingException(JoinPoint joinPoint, Throwable exception) {
		logger.severe("Method: " + joinPoint.getSignature().getName() + "throwing an exception: " + exception.getMessage());
	}
}
