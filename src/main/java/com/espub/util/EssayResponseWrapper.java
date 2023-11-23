package com.espub.util;

import java.time.ZoneId;

import com.espub.dto.EssayResponse;
import com.espub.model.Essay;

public class EssayResponseWrapper 
{
	private static ZoneId sZoneId = ZoneId.of("Europe/Moscow");
	private EssayResponseWrapper() { }
	
	public static EssayResponse of(Essay essay, ZoneId zoneId)
	{
		if (essay == null) return null;
		if (zoneId == null) zoneId = sZoneId;
		int annotationSize = 30;
		return EssayResponse.builder()
				.annotation(createEssayAnnotation(essay, annotationSize))
				.category(essay.getCategory())
				.content(essay.getContent())
				.modificationDate(essay.getModificationDate().withZoneSameInstant(zoneId))
				.publicationDate(essay.getPublicationDate().withZoneSameInstant(zoneId))
				.username(essay.getUser().getUsername())
				.userUrl(null)
				.build();
	}
	
	static private String createEssayAnnotation(Essay essay, int size)
	{
		String annotation;
		if (essay.getContent().length() > size)
			annotation = essay.getContent().substring(0, size-3);
		else
			annotation = essay.getContent();
		while (annotation.endsWith(" "))
		{
			annotation = annotation.substring(0, annotation.lastIndexOf(" "));
		}
		annotation += "...";
		return annotation;
	}
}
