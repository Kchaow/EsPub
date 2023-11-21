package com.espub.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.espub.dto.EssayResponse;
import com.espub.model.Essay;
import com.espub.model.User;

public class EssayResponseWrapperTest 
{
	ZonedDateTime moscowTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
	ZonedDateTime vladivostokTime = ZonedDateTime.now(ZoneId.of("Asia/Vladivostok"));
	User user = User.builder().username("user").build();
	Essay essay = Essay.builder()
			.publicationDate(moscowTime)
			.modificationDate(moscowTime)
			.content("text")
			.user(user)
			.build();
	
	@Test
	void ofShouldReturnEssayResponse()
	{
		EssayResponse essayResponse = EssayResponseWrapper.of(essay, ZoneId.of("Asia/Vladivostok"));	
		assertEquals(vladivostokTime.getHour(), essayResponse.getPublicationDate().getHour());
	}
	
	@Test
	void ofWithNullEssayShouldReturnNull()
	{
		EssayResponse essayResponse = EssayResponseWrapper.of(null, ZoneId.of("Asia/Vladivostok"));
		assertEquals(null, essayResponse);
	}
	
	@Test
	void ofWithNullZoneIdShouldReturnMoscowEssayResponse()
	{
		EssayResponse essayResponse = EssayResponseWrapper.of(essay, null);
		assertEquals(moscowTime.getHour(), essayResponse.getPublicationDate().getHour());
	}
}
