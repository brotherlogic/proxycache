package com.brotherlogic.proxycache.discogs;

import java.util.LinkedList;
import java.util.List;

import com.brotherlogic.proxycache.LinkURL;

/**
 * Format in the Discogs world
 * 
 * @author simon
 * 
 */
public class Format
{
   private int quantity;
   private String name;
   private final List<String> descriptions = new LinkedList<>();
   private String text;

   public List<String> getDescriptions()
   {
      return descriptions;
   }

   /**
    * @return The name of the format
    */
   public String getName()
   {
      return name;
   }

   /**
    * @return Number of discs
    */
   public int getQuantity()
   {
      return quantity;
   }

   public String getText()
   {
      return text;
   }

   public void setDescriptions(String[] descr)
   {
      for (String descript : descr)
         descriptions.add(descript);
   }

   /**
    * @param formatName
    *           The name of the format
    */
   public void setName(final String formatName)
   {
      this.name = formatName;
   }

   /**
    * @param formatQuantity
    *           Number of discs
    */
   @LinkURL(path = "qty")
   public void setQuantity(final int formatQuantity)
   {
      this.quantity = formatQuantity;
   }

   public void setText(final String txt)
   {
      this.text = txt;
   }

   @Override
   public String toString()
   {
      return quantity + " - " + name + " => " + descriptions + "," + text;
   }
}
