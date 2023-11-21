package com.espub.dto;

import java.time.ZonedDateTime;
import java.util.List;

import com.espub.model.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EssayResponse 
{
	private ZonedDateTime publicationDate;
	private ZonedDateTime modificationDate;
	private String content;
	private List<Category> category;
	private String annotation;
	private String username;
	private String userUrl;
}
