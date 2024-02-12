package com.selfman.search.parser;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import com.selfman.search.dto.details.PlacesDetailsByIdDto;
import com.selfman.search.exception.UnableToParseException;
import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class ParserWebSiteServiceImpl {
	public void parseLogoExtractor(PlacesDetailsByIdDto place) {
		try {

			String url = place.getWebsite();
			System.out.println("url rars = " + url);
			if (url != null) {
				UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();
				String baseUrl = uri.getScheme() + "://" + uri.getHost();
				String robotsTxtUrl = baseUrl + "/robots.txt";
				System.out.println("url robo = " + robotsTxtUrl);
				String robotsTxtContect = Jsoup.connect(robotsTxtUrl).get().text();
				if (!robotsTxtContect.contains("User-agent:*\nDisallow:/")) {
//				Pattern pattern = Pattern.compile("(?!)^User-agent:\\*\nDisallow:/");
//				Matcher matcher = pattern.matcher(robotsTxtContect);
//				if (!matcher.find()) {
					Document document = Jsoup.connect(url).get();
//					Element logo = document.selectFirst("img[src*=logo], img.logo, img[src]") ;
//						String logoUrl1 = logo.absUrl("src");
//		System.out.println("logoURL 1 = " + logoUrl1);
					String logoUrl = document.getElementsByTag("img").get(0).attr("src");
					String[] logoTrue = logoUrl.split("://");
					System.out.println("url src = " + logoUrl);
					if (logoUrl != null && (logoTrue[0].equals("https") || logoTrue[0].equals("http"))) {
						place.setIcon(logoUrl);
//					} else if (logoUrl != null && !logoTrue[0].equals("https")) {
//						place.setIcon(baseUrl + logoUrl);
					} else {
						place.setIcon("https://shetko.online/static/media/selfman.0dfe4b35a490aa138b91.png");
					}
				}
			} else {
				place.setIcon("https://shetko.online/static/media/selfman.0dfe4b35a490aa138b91.png");
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
