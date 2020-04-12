package com.biboheart.huip.patient;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.model.BhResponseResult;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@RestControllerAdvice
@EnableRedisHttpSession
@EnableResourceServer
@Slf4j
public class PatientApplication {
	public static void main(String[] args) {
		SpringApplication.run(PatientApplication.class, args);
	}

	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public BhResponseResult<?> defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
			throw e;
		}
		if (e instanceof AuthenticationException) {
			throw e;
		}
		if (e instanceof BhException) {
			return new BhResponseResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
		} else {
			log.error("Request path : [url:{}, method:{}] Error => [E-Class：{}, E-Info:{}]", req.getRequestURI(),
					req.getMethod(), e.getClass(), e.getMessage());
			return new BhResponseResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "System Error", e.getMessage());
		}
	}
}
