package com.payment.aop;

import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PaymentControllerLogger {
	
	private Logger logger = Logger.getLogger(PaymentControllerLogger.class.getName());
	
	@Before("execution(* com.payment.controller.PaymentController.*(..))")
	public void logBeforeMethod(JoinPoint joinPoint) {
		logger.info("Entering Method: " + joinPoint.getSignature().getName() + "with arguments : " + joinPoint.getArgs());
	}
	
	
	@AfterReturning(pointcut = "execution(* com.payment.controller.PaymentController.*(..))", returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) {
		logger.info("Method: "+joinPoint.getSignature().getName() + "returns result: " + result);
	}
	
	
	@AfterThrowing(pointcut = "execution(* com.payment.controller.PaymentController.*(..))", throwing = "exception")
	public void logAfterThrowingException(JoinPoint joinPoint, Throwable exception) {
		logger.severe("Method: " + joinPoint.getSignature().getName() + "throwing an exception: " + exception.getMessage());
	}
}
