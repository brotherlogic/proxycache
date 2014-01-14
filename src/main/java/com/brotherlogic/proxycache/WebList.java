package com.brotherlogic.proxycache;

import java.io.IOException;

import com.brotherlogic.proxycache.runners.StandardOAuthService;
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

    private final String baseURL;
    private JsonObject lastPull;
    private final Pagination pagScheme;
    private final String path;
    private boolean filledTop = false;
    private boolean filledBottom = false;

    /**
     * @param clz
     *            The class to build from
     * @param serv
     *            The web service
     * @param url
     *            The url for this class
     * @param pathIn
     *            The path to pull
     * @param pag
     *            The pagination process
     */
    public WebList(final Class<X> clz, final StandardOAuthService serv, final String url, final String pathIn, final Pagination pag) {
        super(clz, serv);
        this.baseURL = url;
        this.path = pathIn;
        this.pagScheme = pag;
    }

    @Override
    protected int fillBottom() throws IOException {

        if (!filledBottom) {
            // Get the builder
            ObjectBuilder<X> builder = ObjectBuilderFactory.getInstance().getObjectBuilder(getUnderlyingClass(), getService());

            JsonElement elem = builder.resolvePath(pagScheme.botPath(), lastPull);
            if (elem != null) {
                String url = builder.resolvePath(pagScheme.botPath(), lastPull).getAsString();
                lastPull = getService().get(url).getAsJsonObject();
                JsonArray col = builder.resolvePath(path, lastPull).getAsJsonArray();
                if (col.size() == 0) {
                    filledBottom = true;
                }
                for (int i = col.size() - 1; i >= 0; i--) {
                    // Add to the bottom of the collection
                    X obj = builder.build(col.get(i).getAsJsonObject());
                    add(obj);
                }

                return col.size();
            }

            filledBottom = true;
        }
        return 0;
    }

    @Override
    protected int fillTop() throws IOException {
        if (!filledTop) {
            // Get the builder
            ObjectBuilder<X> builder = ObjectBuilderFactory.getInstance().getObjectBuilder(getUnderlyingClass(), getService());

            lastPull = getService().get(baseURL).getAsJsonObject();
            JsonArray col = builder.resolvePath(path, lastPull).getAsJsonArray();
            for (int i = col.size() - 1; i >= 0; i--) {
                // Add to the top of the collection
                X obj = builder.build(col.get(i).getAsJsonObject());
                add(0, obj);
            }

            filledTop = true;
            return col.size();
        }
        return 0;
    }

    /**
     * Generate the bottom url
     * 
     * @param curr
     *            The current page
     * @return The url for pulling from the web
     */
    public String generateBottomURL(final JsonElement curr) {
        if (!pagScheme.botPath().equals("")) {
            return new ObjectBuilder<X>(getUnderlyingClass(), getService()).resolvePath(pagScheme.botPath(), curr.getAsJsonObject()).getAsString();
        }
        return "blah";
    }

    /**
     * Generates the top url
     * 
     * @param curr
     *            The current page
     * @return The url
     */
    public String generateTopURL(final JsonElement curr) {
        return "blah";
    }

    @Override
    public int size() {
        try {
            if (!filledTop) {
                fillTop();
            }

            while (!filledBottom) {
                fillBottom();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.size();
    }
}
