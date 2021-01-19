package com.example.medicinal.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
	public static boolean validDate(String date)
	{
		String regex = "^(1[5-9][0-9][0-9]|2[0-2][0-2][0-9])-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])$"; 
		 Pattern pattern = Pattern.compile(regex); 
		 Matcher matcher = pattern.matcher((CharSequence)date); 
		 return matcher.matches();
	}
}
