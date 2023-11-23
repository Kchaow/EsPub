package com.espub.util;

import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EssayPageSort 
{
	TIME(Sort.by(Sort.Direction.DESC, "modificationDate")),
	VIEWS(Sort.by(Sort.Direction.DESC, "views")), //no test
	TIME_AND_VIEWS(Sort.by(Sort.Direction.DESC, "views" ,"modificationDate")); //no test
	
	private final Sort sortValue;
}
