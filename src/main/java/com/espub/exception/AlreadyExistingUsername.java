package com.espub.exception;

@SuppressWarnings("serial")
public class AlreadyExistingUsername extends Exception
{
	public AlreadyExistingUsername()
	{
		
	}
	public AlreadyExistingUsername(String message) 
	{
		super(message);
	}

}
