package com.espub.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ControllerExceptionHandler 
{
	Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);
	@Autowired
	private ObjectMapper objectMapper;
	
	@ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyExistingUsername.class)
    public String handleExistingUsername(AlreadyExistingUsername ex) 
	{
		String errorText = "user with the same name already exists";
		logger.error("{}: {}", ex.toString(), errorText);
		return respondJsonError(
				String.format("%s: %s", errorText, ex.getMessage())
				);
    }
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public String handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) 
	{
		String errorText = "invalid username or password";
		logger.warn("Failed login attempt from address {}", request.getRemoteAddr());
		logger.error("{}: {}", ex.toString(), errorText);
		return respondJsonError(errorText);
    }
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) 
	{
		logger.error("{}: invalid request fields", ex.toString());
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoSuchElementException.class)
	public String handleUnknownObjectId(NoSuchElementException ex)
	{
		String errorText = "unknown object id";
		logger.error("{}: {}", ex.toString(), errorText);
		return respondJsonError(errorText);
	}
	
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(NoPermissionException.class)
	public String handleNoPermissionToData(NoPermissionException ex)
	{
		String errorText = "no permission to access data";
		logger.error("{}: {}", ex.toString(), errorText);
		return respondJsonError(errorText);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public String handlerMethodArgumentType(MethodArgumentTypeMismatchException ex)
	{
		String errorText = "invalid request params";
		logger.error("{}: {}", ex.toString(), errorText);
		return respondJsonError(errorText);
	}
	
	private String respondJsonError(String error)
	{
		ObjectNode objectNode = objectMapper.createObjectNode();
		try
		{
			objectNode.put("error", error);
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
		}
		catch (JsonProcessingException e)
		{
			return objectNode.toString();
		}
		
	}
}
