package com.espub.util;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import com.espub.dto.EssayResponse;
import com.espub.dto.EssayResponsePage;
import com.espub.model.Essay;

import lombok.Getter;

@Getter
public class EssayResponsePageWrapper
{
	private static ZoneId sZoneId = ZoneId.of("Europe/Moscow");
	private EssayResponsePageWrapper() {}
	public static EssayResponsePage of(Page<Essay> page, ZoneId zoneId)
	{
		if (page == null) return null;
		if (zoneId == null) zoneId = sZoneId;
		return EssayResponsePage.builder()
				.essayResponseList(getEssayResponseList(page, zoneId))
				.pageNumber(page.getNumber())
				.essayCount(page.getNumberOfElements())
				.totalEssayCount(page.getTotalPages())
				.totalPageCount(page.getTotalPages())
				.isFirst(page.isFirst())
				.isLast(page.isLast())
				.build();
	}

	private static List<EssayResponse> getEssayResponseList( Page<Essay> page, ZoneId zoneId)
	{
		List<EssayResponse> list = new ArrayList<>();
		for (Essay essay : page.getContent())
		{
			list.add(
					EssayResponseWrapper.of(essay, zoneId)
					);
		}
		return list;
	}
}
