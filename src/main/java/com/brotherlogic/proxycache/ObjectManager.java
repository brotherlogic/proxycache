package com.brotherlogic.proxycache;

/**
 * Represents an object manager in the system. Object managers are able to process basic objects
 * @author simon
 *
 * @param <X> The generic class of this manager
 */
public class ObjectManager<X> {

	/** The class defined by this manager */
	private Class<X> underlyingClass;
	
	/** Flag to indicate if we want a fast (i.e. non updating) return */
	private Boolean immediateReturn = false;
	
	/**
	 * Base constructor
	 * @param uClass The class that this object manages
	 */
	public ObjectManager(Class<X> uClass)
	{
		underlyingClass = uClass;
	}
	
	public X get(String id)
	{
		//Check the local cache
		
		//Check for staling
		
		//Update the object if necessary
		
		//Return the object
		return null;
	}
	
	private void runMethod(Method m, URL url, String path)
	{
		
	}
}
