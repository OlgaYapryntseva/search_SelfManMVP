package com.selfman.search.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.selfman.search.dto.details.PlacesDetailsByIdDto;
import com.selfman.search.exception.UnableToParseException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class ParserWebSite {
	public void parseLogoExtractor(PlacesDetailsByIdDto place) {
		try {
			String url = place.getWebsite();
			if (url != null) {
				String robotsTxtUrl = url + "/robots.txt";
				String robotsTxtContect = Jsoup.connect(robotsTxtUrl).get().text();
				Pattern pattern = Pattern.compile("(?!)^User-agent:\\*\nDisallow:/");
				Matcher matcher = pattern.matcher(robotsTxtContect);
				if (!matcher.find()) {
						Document document = Jsoup.connect(url).get();
						String src = document.getElementsByTag("img").get(0).attr("src");
						String[] srcTrue = src.split("://");
						if (src != null && srcTrue[0].equals("https")) {
							place.setIcon(src);
						} else {
							place.setIcon("https://shetko.online/static/media/selfman.0dfe4b35a490aa138b91.png");
						}
				} else {
					place.setIcon("https://shetko.online/static/media/selfman.0dfe4b35a490aa138b91.png");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String[] parseTitleDescription(String url) throws IOException {
		if (url != null) {
			String robotsTxtUrl = url + "/robots.txt";
			String robotsTxtContect = Jsoup.connect(robotsTxtUrl).get().text();
			Pattern pattern = Pattern.compile("(?!)^User-agent:\\*\nDisallow:/");
			Matcher matcher = pattern.matcher(robotsTxtContect);
			if (!matcher.find()) {
				try {
					Document doc = Jsoup.connect(url).get();
					String title = parseTitle(doc);
					String description = parseDescription(doc);
					return new String[] { title, description };
				} catch (IOException e) {
					throw new UnableToParseException("Unable to parse");
				}
			}
		}
		return new String[] { "None", "None" };
	}

	private String parseDescription(Document doc) {
		if (doc.select("meta[name=description]").isEmpty()) {
			return "None";
		}
		return doc.select("meta[name=description]").get(0).attr("content");
	}

	private String parseTitle(Document doc) {
		if (doc.select("title").isEmpty()) {
			return "None";
		}
		return doc.title();
	}

}
