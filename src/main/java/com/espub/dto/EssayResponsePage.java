package com.espub.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EssayResponsePage 
{
	private List<EssayResponse> essayResponseList;
	private int pageNumber;
	private int essayCount;
	private long totalEssayCount;
	private int totalPageCount;
	private boolean isFirst;
	private boolean isLast;
}
