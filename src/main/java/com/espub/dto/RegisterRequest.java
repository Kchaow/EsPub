package com.espub.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest 
{
	@NotNull(message = "username cannot be empty")
	private String username;
	@NotNull(message = "password cannot be empty")
	private String password;
}
