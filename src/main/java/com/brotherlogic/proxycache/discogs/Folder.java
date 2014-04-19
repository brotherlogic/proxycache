package com.brotherlogic.proxycache.discogs;

import java.util.Collection;

import com.brotherlogic.proxycache.LinkURL;
import com.brotherlogic.proxycache.Pagination;
import com.brotherlogic.proxycache.Staling;

/**
 * A folder in the discogs world - we always check for refresh
 * 
 * @author simon
 * 
 */
@Staling(0)
public class Folder
{

   private int count;
   private int id;
   private String name;
   private Collection<Release> releases;

   /**
    * @return The number of elements in this folder
    */
   public int getCount()
   {
      return count;
   }

   /**
    * @return The id of the folder
    */
   public int getId()
   {
      return id;
   }

   /**
    * @return THe name of the folder
    */
   public String getName()
   {
      return name;
   }

   /**
    * @return A Collection of all the releases in this folder
    */
   public Collection<Release> getReleases()
   {
      return releases;
   }

   /**
    * @param cnt
    *           The number of releases in the folder
    */
   public void setCount(final int cnt)
   {
      this.count = cnt;
   }

   /**
    * @param folderId
    *           The number of this folder
    */
   public void setId(final int folderId)
   {
      this.id = folderId;
   }

   /**
    * @param nme
    *           The name of this folder
    */
   public void setName(final String nme)
   {
      this.name = nme;
   }

   /**
    * @param rels
    *           The releases in this folder
    */
   @LinkURL(url = "[resource_url]/releases?sort=added", prodClass = "com.brotherlogic.proxycache.discogs.Release", path = "releases")
   @Pagination(botPath = "pagination->urls->next")
   public void setReleases(final Collection<Release> rels)
   {
      this.releases = rels;
   }

   @Override
   public String toString()
   {
      return name;
   }

}
