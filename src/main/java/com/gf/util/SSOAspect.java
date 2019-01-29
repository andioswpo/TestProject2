package com.gf.util;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class SSOAspect {
	private HttpServletRequest request =null;
	
    @Pointcut(value = "execution(* com.gf.ctrl..*(..))")
    public void Point() { }

    @Around(value = "Point()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
    	request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        String methodName = point.getSignature().getName();
    	System.out.println("SSOAspect==========="+methodName);

        Object jsonObject = point.proceed();
        return jsonObject;
    }
}
