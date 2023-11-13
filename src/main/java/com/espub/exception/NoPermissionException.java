package com.espub.exception;

@SuppressWarnings("serial")
public class NoPermissionException extends Exception
{
	public NoPermissionException() { }
	public NoPermissionException(String message) { super(message); }
}
