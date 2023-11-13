package com.espub.component;

import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EssayPageSort 
{
	TIME(Sort.by(Sort.Direction.DESC, "modificationDate"))
	//VIEWS(Sort.by(Sort.Direction.DESC, "views")), TODO
	//TIME_AND_VIEWS(Sort.by(Sort.Direction.DESC, "views" ,"modificationDate")), TODO
	;
	
	private final Sort sortValue;
}
