package com.brotherlogic.proxycache.runners;

import java.io.File;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.brotherlogic.proxycache.Config;
import com.brotherlogic.proxycache.ObjectManager;
import com.brotherlogic.proxycache.discogs.DiscogsUser;
import com.brotherlogic.proxycache.discogs.Folder;
import com.brotherlogic.proxycache.discogs.Identity;
import com.brotherlogic.proxycache.discogs.Release;

/**
 * Master discogs
 * 
 * @author simon
 * 
 */
public class Discogs {

    private final DiscogsService service = new DiscogsService();

    /**
     * Gets the identify user
     * 
     * @return The DiscogsUser
     * @throws Exception
     *             if we can't pull the identity
     */
    public DiscogsUser getMe() throws Exception {
        // Force load a user from the identity URL
        ObjectManager<Identity> manager = new ObjectManager<Identity>(Identity.class, service);
        Identity ident = manager.get();
        DiscogsUser user = new DiscogsUser(ident.getUsername());

        new ObjectManager<>(DiscogsUser.class, service).refresh(user);
        return user;
    }

    /**
     * Runner
     * 
     * @param args
     *            we con't use cla
     * @throws Exception
     *             if something goes wrong
     */
    public static void main(final String[] args) throws Exception {
        Config.getInstance().loadDir(new File("configs"));
        Discogs me = new Discogs();
        // me.getLengths(245793);
        // me.printRecords("12s", "Vinyl", 8, new File("/Users/simon/local/Dropbox/records/12.records"));
        // me.printRecords("CDs", "CD", 2, new File("/Users/simon/local/Dropbox/records/cd.records"));
        me.pick12();
    }

    /**
     * Gets the lengths of all the releases of this master
     * 
     * @param releaseNumber
     *            The release Number
     * @throws Exception
     *             if something goes wrong
     */
    public void getLengths(final int releaseNumber) throws Exception {
        Master m = new Master();
        m.setId(releaseNumber);

        new ObjectManager<>(Master.class, service).refresh(m);
        System.out.println(m.getTitle() + " => " + m.getVersions().size());
        for (Release r : m.getVersions()) {
            new ObjectManager<>(Release.class, service).refresh(r);
            System.out.println(" " + r.getTitle() + " (" + r.getId() + ") => " + r.getTracks().size());
        }
    }

    /**
     * Prints out the records
     * 
     * @param category
     *            The category to be printed
     * @param format
     *            the format to count as a disc
     * @param bridge
     *            the number of blocks the collection goes into
     * @param outFile
     *            the file to write to
     * @throws Exception
     *             If something goes wrong
     */
    public void printRecords(final String category, final String format, final int bridge, final File outFile) throws Exception {
        DiscogsUser user = getMe();

        outFile.createNewFile();
        PrintStream ps = new PrintStream(outFile);
        for (Folder f : user.getFolders()) {
            if (f.getName().equals(category)) {
                List<Release> rels = new LinkedList<>(f.getReleases());

                Collections.sort(rels, new Comparator<Release>() {
                    @Override
                    public int compare(final Release o1, final Release o2) {
                        if (o1.getLabelString().equals(o2.getLabelString())) {
                            return o1.getCatNoString().compareTo(o2.getCatNoString());
                        } else {
                            return o1.getLabelString().compareTo(o2.getLabelString());
                        }
                    }
                });

                int count = 0;
                for (Release rel : rels) {
                    count += rel.getNumberOfDiscs(format);
                }

                double boundary = (count + 0.0) / bridge;

                count = 0;
                double printAt = boundary;
                for (Release rel : rels) {
                    if (count + rel.getNumberOfDiscs(format) > printAt) {
                        ps.println("--------------------------------------");
                        printAt += boundary;
                    }
                    count += rel.getNumberOfDiscs(format);
                    ps.println(rel.getArtistString() + " - " + rel.getTitle() + " [" + rel.getLabelString() + "-" + rel.getCatNoString() + "] : " + rel.getNumberOfDiscs(format));
                }
            }
        }

        ps.close();
    }

    /**
     * Picks a 12"
     * 
     * @throws Exception
     *             if we can't reach discogs
     */
    public void pick12() throws Exception {
        DiscogsUser user = getMe();
        List<Release> rels = new LinkedList<>();

        for (Folder f : user.getFolders()) {
            System.out.println(f.getName());
            if (f.getName().equals("12s") || f.getName().equals("10s")) {
                rels.addAll(f.getReleases());
            }
        }

        System.out.println("Found " + rels.size() + " releases");

        Collections.shuffle(rels);
        for (Release rel : rels) {
            if (rel.getRating() <= 0) {
                System.out.println(rel.getTitle() + " - " + rel.getLabelString());
                System.exit(1);
            }
        }

    }
}
