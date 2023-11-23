package com.espub.config;

import java.time.ZoneId;
import java.util.Map;

import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.espub.util.ZoneIdPropertyEditor;

@Configuration
public class ApplicationConfig 
{
	@Bean
	CustomEditorConfigurer customEditorConfigurer()
	{
		CustomEditorConfigurer customEditorConfigurer = new CustomEditorConfigurer();
		customEditorConfigurer.setCustomEditors(Map.of(ZoneId.class, ZoneIdPropertyEditor.class));
		return customEditorConfigurer;
	}

}
