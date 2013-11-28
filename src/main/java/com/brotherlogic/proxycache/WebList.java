package com.brotherlogic.proxycache;

import java.io.IOException;

import com.brotherlogic.proxycache.discogs.StandardOAuthService;
import com.brotherlogic.proxycache.example.twitter.ObjectBuilder;
import com.brotherlogic.proxycache.example.twitter.ObjectBuilderFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * An unbounded double ended list
 * 
 * @author simon
 * 
 * @param <X>
 *            The type represented by the list
 */
public class WebList<X> extends UnboundedList<X> {

    private final Pagination pagScheme;
    private final String baseURL;
    private final String path;
    private JsonObject lastPull;

    public WebList(Class<X> clz, StandardOAuthService serv, String url, String pathIn, Pagination pag) {
        super(clz, serv);
        this.baseURL = url;
        this.path = pathIn;
        this.pagScheme = pag;
    }

    @Override
    protected int fillBottom() throws IOException {
        // Get the builder
        ObjectBuilder<X> builder = ObjectBuilderFactory.getInstance().getObjectBuilder(getUnderlyingClass(), getService());

		JsonElement elem = builder.resolvePath(pagScheme.botPath(), lastPull);
		if (elem != null) {
			String url = builder.resolvePath(pagScheme.botPath(), lastPull)
					.getAsString();
			lastPull = getService().get(url).getAsJsonObject();
			JsonArray col = builder.resolvePath(path, lastPull)
					.getAsJsonArray();
			for (int i = col.size() - 1; i >= 0; i--) {
				// Add to the bottom of the collection
				X obj = builder.build(col.get(i).getAsJsonObject());
				add(obj);
			}

            return col.size();
        }

        return 0;
    }

    @Override
    protected int fillTop() throws IOException {
        // Get the builder
        ObjectBuilder<X> builder = ObjectBuilderFactory.getInstance().getObjectBuilder(getUnderlyingClass(), getService());

        lastPull = getService().get(baseURL).getAsJsonObject();
        JsonArray col = builder.resolvePath(path, lastPull).getAsJsonArray();
        for (int i = col.size() - 1; i >= 0; i--) {
            // Add to the top of the collection
            X obj = builder.build(col.get(i).getAsJsonObject());
            add(0, obj);
        }

        return col.size();
    }

    public String generateBottomURL(JsonElement curr) {
        if (!pagScheme.botPath().equals("")) {
            return new ObjectBuilder<X>(getUnderlyingClass(), getService()).resolvePath(pagScheme.botPath(), curr.getAsJsonObject()).getAsString();
        }
        return "blah";
    }

    public String generateTopURL(JsonElement curr) {
        return "blah";
    }

}
