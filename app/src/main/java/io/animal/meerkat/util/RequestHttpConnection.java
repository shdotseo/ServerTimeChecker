package io.animal.meerkat.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestHttpConnection {

    private String serverUrl;

    public RequestHttpConnection(String url) {
        this.serverUrl = url;
    }

    public long getServerDate() throws IOException {
        URL url = new URL(serverUrl);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        conn.setRequestMethod("HEAD");
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(3000);

        conn.connect();

        return conn.getHeaderFieldDate("Date", 0);
    }
}
