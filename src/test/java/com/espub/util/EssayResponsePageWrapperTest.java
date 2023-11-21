package com.espub.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.espub.dto.EssayResponsePage;
import com.espub.model.Essay;
import com.espub.model.User;

public class EssayResponsePageWrapperTest 
{
	ZonedDateTime moscowTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
	ZonedDateTime vladivostokTime = ZonedDateTime.now(ZoneId.of("Asia/Vladivostok"));
	User user = User.builder().username("user").build();
	Essay essay1 = Essay.builder()
			.publicationDate(moscowTime)
			.modificationDate(moscowTime)
			.content("text1")
			.user(user)
			.build();
	Essay essay2 = Essay.builder()
			.publicationDate(moscowTime)
			.modificationDate(moscowTime)
			.content("text2")
			.user(user)
			.build();
	Page<Essay> page = new PageImpl<>(List.of(essay1, essay2));
 	
	@Test
	void ofShouldReturnEssayResponsePage()
	{
		EssayResponsePage essayResponse = EssayResponsePageWrapper.of(page, ZoneId.of("Asia/Vladivostok"));	
		assertAll(
					() -> assertEquals(2, essayResponse.getEssayResponseList().size()),
					() -> assertEquals(vladivostokTime.getHour(), essayResponse.getEssayResponseList().get(0).getPublicationDate().getHour()),
					() -> assertEquals(vladivostokTime.getHour(), essayResponse.getEssayResponseList().get(1).getPublicationDate().getHour())
				);
	}
	
	@Test
	void ofWithNullPageShouldReturnNull()
	{
		EssayResponsePage essayResponse = EssayResponsePageWrapper.of(null, ZoneId.of("Asia/Vladivostok"));
		assertEquals(null, essayResponse);
	}
	
	@Test
	void ofWithNullZoneIdShouldReturnMoscowEssayResponse()
	{
		EssayResponsePage essayResponse = EssayResponsePageWrapper.of(page, null);
		assertEquals(moscowTime.getHour(), essayResponse.getEssayResponseList().get(0).getPublicationDate().getHour());
	}
}
