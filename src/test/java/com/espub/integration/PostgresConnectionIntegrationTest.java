package com.espub.integration;

import javax.sql.DataSource;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Postgres integration testing")
@Disabled("Нет необходимости")
public class PostgresConnectionIntegrationTest 
{
	@Autowired
	private DataSource dataSource;
	
	@Test
	public void connectionTest()
	{
		try
		{
			assertDoesNotThrow(() -> dataSource.getConnection());
			Connection connection = dataSource.getConnection();
			assertTrue(connection.isValid(0));
		}
		catch (Exception e)
		{
			
		}
		
	}
}
