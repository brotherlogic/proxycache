package com.brotherlogic.proxycache.discogs;

import java.util.Collection;

import javax.sound.midi.Track;

import com.brotherlogic.proxycache.LinkURL;

/**
 * A release in the discogs world
 * 
 * @author simon
 * 
 */
@LinkURL(url = "http://api.discogs.com/releases/<Id>")
public class Release
{

   private long id;
   private Collection<Label> labels;
   private int rating = -1;
   private String title;
   private Collection<Track> tracks;
   private Collection<Artist> artists;
   private Collection<Format> formats;

   /**
    * @return The artists on this release
    */
   public Collection<Artist> getArtists()
   {
      return artists;
   }

   /**
    * @return A String rep of the artists on this record
    */
   public String getArtistString()
   {
      String str = "";
      if (getArtists() != null)
      {
         for (Artist label : getArtists())
            str += label.getName() + ", ";
         return str.substring(0, str.length() - 2);
      }

      return str;
   }

   /**
    * @return A String rep of the cat no on this record
    */
   public String getCatNoString()
   {
      String str = "";
      for (Label label : getLabels())
         str += label.getCatno() + ", ";
      return str.substring(0, str.length() - 2);
   }

   /**
    * @return The formats for the release
    */
   public Collection<Format> getFormats()
   {
      return formats;
   }

   /**
    * @return The id of the release
    */
   public long getId()
   {
      return id;
   }

   /**
    * @return A {@link Collection} of all the {@link Label} for this release
    */
   public Collection<Label> getLabels()
   {
      return labels;
   }

   /**
    * @return A String rep of the labels on this record
    */
   public String getLabelString()
   {
      String str = "";
      for (Label label : getLabels())
         str += label.getName() + ", ";
      return str.substring(0, str.length() - 2);
   }

   /**
    * The number of discs for a given format
    * 
    * @param format
    *           The format name
    * @return The number of discs for this format
    */
   public int getNumberOfDiscs(final String format)
   {
      System.out.println("FORM (" + title + ") = " + formats);
      int quantity = 0;
      for (Format form : formats)
      {
         if (form.getName().equals(format))
            quantity += form.getQuantity();
         if (form.getName().equals("Box Set") || form.getName().equals("Gatefold"))
            quantity += 2;
         if (form.getDescriptions() != null)
            for (String descr : form.getDescriptions())
               if (descr.contains("Box"))
                  quantity += 2;
               else if (descr.contains("Gatefold"))
                  quantity += 1;
         if (form.getText() != null)
         {
            if (form.getText().contains("Box"))
               quantity += 2;
            if (form.getText().contains("Book"))
               quantity += 4;
            if (form.getText().contains("Gatefold"))
               quantity += 1;
         }

      }
      return quantity;
   }

   /**
    * @return The rating of this release
    */
   public int getRating()
   {
      return rating;
   }

   /**
    * @return The name of this release
    */
   public String getTitle()
   {
      return title;
   }

   /**
    * @return The tracks in this release
    */
   public Collection<Track> getTracks()
   {
      return tracks;
   }

   /**
    * @param releaseArtists
    *           The artists on this release
    */
   @LinkURL(path = "basic_information->artists", prodClass = "com.brotherlogic.proxycache.discogs.Artist")
   public void setArtists(final Collection<Artist> releaseArtists)
   {
      this.artists = releaseArtists;
   }

   /**
    * @param releaseFormats
    *           The formats for the release
    */
   @LinkURL(path = "basic_information->formats", prodClass = "com.brotherlogic.proxycache.discogs.Format")
   public void setFormats(final Collection<Format> releaseFormats)
   {
      this.formats = releaseFormats;
   }

   /**
    * @param relId
    *           The id of this release
    */
   public void setId(final long relId)
   {
      this.id = relId;
   }

   /**
    * @param relLabels
    *           All the {@link Label} suitable for this release
    */
   @LinkURL(path = "basic_information->labels", prodClass = "com.brotherlogic.proxycache.discogs.Label")
   public void setLabels(final Collection<Label> relLabels)
   {
      this.labels = relLabels;
   }

   /**
    * @param rate
    *           The user rating of this release
    */
   @LinkURL(path = "rating")
   public void setRating(final int rate)
   {
      this.rating = rate;
   }

   /**
    * @param name
    *           The name of this release
    */
   @LinkURL(path = "basic_information->title")
   public void setTitle(final String name)
   {
      this.title = name;
   }

   /**
    * @param releasesTracks
    *           The tracks in this release
    */
   @LinkURL(path = "tracklist", prodClass = "com.brotherlogic.proxycache.discogs.Track")
   public void setTracks(final Collection<Track> releasesTracks)
   {
      this.tracks = releasesTracks;
   }

}
