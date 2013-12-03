package com.brotherlogic.proxycache.runners;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public abstract class CachingOAuthService extends StandardOAuthService {

	private final File cacheDir;
	private static boolean useCache = false;

	public static void forceCache(boolean cache) {
		useCache = cache;
	}

	public CachingOAuthService() {
		// Create the cache directory
		cacheDir = new File(new File("."), "cache");
		cacheDir.mkdirs();
	}

	@Override
	protected String getRaw(String url) throws IOException {

		File f = new File(cacheDir, url.hashCode() + ".cache");

		if (useCache && f.exists()) {
			String resp = "";
			BufferedReader reader = new BufferedReader(new FileReader(f));
			for (String line = reader.readLine(); line != null; line = reader
					.readLine())
				resp += line.trim();
			reader.close();
			return resp;
		}

		String superResp = super.getRaw(url);

		// Cache out the response
		PrintStream ps = new PrintStream(f);
		ps.print(superResp);
		ps.close();

		return superResp;
	}
}
