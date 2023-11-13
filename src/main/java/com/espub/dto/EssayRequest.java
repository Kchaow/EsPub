package com.espub.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EssayRequest {
	@NotEmpty(message="Essay cannot contain empty content")
	private String content;

}
