package com.brotherlogic.proxycache.example.twitter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import com.brotherlogic.proxycache.LinkURL;

/**
 * Example Tweet class
 * @author simon
 *
 */
public class Tweet {

	private String text;
	
	@LinkURL(path="blah->bloh->bleh",url="http://blah.com")
	public void setText(String txt)
	{
		this.text = txt;
	}
	
	public String getText(String txt)
	{
		return text;
	}
	
	public static void main(String[] args)
	{
		Class<?> tClass = Tweet.class;
		for(Method m : tClass.getMethods())
		{
			System.out.println(m.getName() + " => " + Arrays.toString(m.getAnnotations()));		
		}

	}
	
}
