package com.selfman.search.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;



public class WebScrapper {
    public static String scrapResource(String initialUrl) {
        URL formattedInitialUrl = getFormattedInitialUrl(initialUrl);
        if (formattedInitialUrl == null) {
            return null;
        }
        Document docHome = request(formattedInitialUrl.toString());
        if (docHome == null) {
            return null;
        }
        return docHome.text();
    }

    @SuppressWarnings("deprecation")
	private static URL getFormattedInitialUrl(String initialUrl) {
        try {
            URL currentUrl = new URL(initialUrl);
            return new URL(String.format("%s://%s", currentUrl.getProtocol(), currentUrl.getHost()));
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private static Document request(String url) {
        try {
            Connection con = Jsoup.connect(url);
            Document doc = con.get();
            return con.response().statusCode() == 200 ? doc : null;
        } catch (IOException e) {
            return null;
        }
    }
}
