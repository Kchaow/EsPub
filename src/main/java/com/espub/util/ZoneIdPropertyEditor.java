package com.espub.util;

import java.beans.PropertyEditorSupport;
import java.time.ZoneId;

public class ZoneIdPropertyEditor extends PropertyEditorSupport
{
	@Override
	public void setAsText(String text) throws IllegalArgumentException
	{
		setValue(ZoneId.of(text));
	}
}
