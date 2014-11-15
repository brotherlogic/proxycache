package com.brotherlogic.proxycache.discogs;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.brotherlogic.proxycache.Config;
import com.brotherlogic.proxycache.ObjectManager;

/**
 * Master discogs
 * 
 * @author simon
 * 
 */
public class Discogs
{

   private final DiscogsService service = new DiscogsService();

   public static final String USER_AGENT = "DiscogsSimonStuff/0.1 +http://github.com/BrotherLogic/proxycache";

   /**
    * Gets the lengths of all the releases of this master
    * 
    * @param releaseNumber
    *           The release Number
    * @throws Exception
    *            if something goes wrong
    */
   public void getLengths(final int releaseNumber) throws Exception
   {
      Master m = new Master();
      m.setId(releaseNumber);

      new ObjectManager<>(Master.class, service).refresh(m);
      System.out.println(m.getTitle() + " => " + m.getVersions().size());
      for (Release r : m.getVersions())
      {
         new ObjectManager<>(Release.class, service).refresh(r);
         System.out.println(" " + r.getTitle() + " (" + r.getId() + ") => " + r.getTracks().size());
      }
   }

   /**
    * Gets the identify user
    * 
    * @return The DiscogsUser
    * @throws Exception
    *            if we can't pull the identity
    */
   public DiscogsUser getMe() throws Exception
   {
      // Force load a user from the identity URL
      ObjectManager<Identity> manager = new ObjectManager<Identity>(Identity.class, service);
      Identity ident = manager.get();
      DiscogsUser user = new DiscogsUser(ident.getUsername());

      new ObjectManager<>(DiscogsUser.class, service).refresh(user);
      return user;
   }

   /**
    * Picks a 12"
    * 
    * @throws Exception
    *            if we can't reach discogs
    */
   public void pick12() throws Exception
   {
      DiscogsUser user = getMe();
      List<Release> rels = new LinkedList<>();

      for (Folder f : user.getFolders())
      {
         System.out.println(f.getName());
         if (f.getName().equals("12s") || f.getName().equals("10s"))
            rels.addAll(f.getReleases());
      }

      int count = 0;
      for (Release rel : rels)
         if (rel.getRating() <= 0)
            count++;

      System.out.println("Found " + rels.size() + "/" + count + " releases");

      Collections.shuffle(rels);
      for (Release rel : rels)
         if (rel.getRating() <= 0)
         {
            System.out.println(rel.getTitle() + " - " + rel.getLabelString());
            System.exit(1);
         }

   }

   /**
    * Picks a 12"
    *
    * @throws Exception
    *            if we can't reach discogs
    */
   public void pickCD() throws Exception
   {
      DiscogsUser user = getMe();
      List<Release> rels = new LinkedList<>();

      for (Folder f : user.getFolders())
      {
         System.out.println(f.getName());
         if (f.getName().equals("CDs") || f.getName().equals("Digital"))
            rels.addAll(f.getReleases());
      }

      int count = 0;
      for (Release rel : rels)
         if (rel.getRating() <= 0)
            count++;

      System.out.println("Found " + rels.size() + "/" + count + " releases");

      Collections.shuffle(rels);
      for (Release rel : rels)
         if (rel.getRating() <= 0)
         {
            System.out.println(rel.getTitle() + " - " + rel.getLabelString());
            System.exit(1);
         }

   }

   /**
    * Prints out the records
    * 
    * @param category
    *           The category to be printed
    * @param format
    *           the format to count as a disc
    * @param bridge
    *           the number of blocks the collection goes into
    * @param outFile
    *           the file to write to
    * @throws Exception
    *            If something goes wrong
    */
   public void printRecords(final String category, final String format, final int bridge,
         final File outFile) throws Exception
   {
      DiscogsUser user;
      try
      {
         user = getMe();
      }
      catch (IOException e)
      {
         if (e.getMessage().contains("Requires Re-Auth"))
         {
            Config.getInstance().clear();
            user = getMe();
         }
         else
            throw e;
      }

      outFile.createNewFile();
      PrintStream ps = new PrintStream(outFile);
      for (Folder f : user.getFolders())
         if (f.getName().equals(category))
         {
            List<Release> rels = new LinkedList<>(f.getReleases());

            List<Release> toRemove = new LinkedList<Release>();
            for (Release rel : rels)
               if (rel.getRating() < 1)
                  toRemove.add(rel);
            rels.removeAll(toRemove);

            Collections.sort(rels, new Comparator<Release>()
                  {
               @Override
               public int compare(final Release o1, final Release o2)
               {
                  if (o1.getLabelString().equals(o2.getLabelString()))
                     return o1.getCatNoString().compareTo(o2.getCatNoString());
                  else
                     return o1.getLabelString().compareTo(o2.getLabelString());
               }
                  });

            int count = 0;
            for (Release rel : rels)
               count += rel.getNumberOfDiscs(format);

            double boundary = (count + 0.0) / bridge;

            count = 0;
            double printAt = boundary;
            for (Release rel : rels)
            {
               if (count + rel.getNumberOfDiscs(format) > printAt)
               {
                  ps.println("--------------------------------------");
                  printAt += boundary;
               }
               count += rel.getNumberOfDiscs(format);
               ps.println(rel.getArtistString() + " - " + rel.getTitle() + " ["
                     + rel.getLabelString() + "-" + rel.getCatNoString() + "] : "
                     + rel.getNumberOfDiscs(format));
            }

            ps.println("------------------------");
            for (Release rel : toRemove)
               ps.println(rel.getArtistString() + " - " + rel.getTitle() + " ["
                     + rel.getLabelString() + "-" + rel.getCatNoString() + "] : "
                     + rel.getNumberOfDiscs(format));
         }

      ps.close();
   }

   /**
    * Runner
    * 
    * @param args
    *           we con't use cla
    * @throws Exception
    *            if something goes wrong
    */
   public static void main(final String[] args) throws Exception
   {
      Config.getInstance().loadDir(new File("configs"));
      Discogs me = new Discogs();
      // me.getLengths(245793);
      // me.printRecords("12s", "Vinyl", 10, new
      // File("/Users/simon/Dropbox/records/12.records"));
      // me.printRecords("CDs", "CD", 3, new
      // File("/Users/simon/Dropbox/records/cd.records"));
      // me.printRecords("10s", "Vinyl", 1, new
      // File("/Users/stucker/Dropbox/records/10.records"));
      me.pick12();
   }
}
