package com.brotherlogic.proxycache;

import java.io.IOException;

import com.brotherlogic.proxycache.runners.StandardOAuthService;
import com.google.gson.JsonArray;

/**
 * A simple unbounded collection
 * 
 * @author simon
 * 
 * @param <X>
 */
public class SimpleCollectionUnbounded<X> extends UnboundedList<X> {

    private final String path;
    private final String url;

    /**
     * @param clz
     *            The class for this collection
     * @param serv
     *            The web service
     * @param mainUrl
     *            The principle url
     * @param pth
     *            The path to follow
     */
    public SimpleCollectionUnbounded(final Class<X> clz, final StandardOAuthService serv, final String mainUrl, final String pth) {
        super(clz, serv);
        url = mainUrl;
        this.path = pth;
    }

    @Override
    protected int fillBottom() {
        // We don't fill the bottom
        return 0;
    }

    private boolean topFilled = false;

    @Override
    protected int fillTop() throws IOException {
        if (!topFilled) {
            // Get the builder
            ObjectBuilder<X> builder = ObjectBuilderFactory.getInstance().getObjectBuilder(getUnderlyingClass(), getService());

            JsonArray col = builder.resolvePath(path, getService().get(url).getAsJsonObject()).getAsJsonArray();
            for (int i = col.size() - 1; i >= 0; i--) {
                // Add to the top of the collection
                X obj = builder.build(col.get(i).getAsJsonObject());
                add(0, obj);
            }

            topFilled = true;
            return col.size();
        }
        return 0;
    }

    @Override
    public int size() {
        if (!getTopFilled()) {
            try {
                fillTop();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.size();
    }

}
