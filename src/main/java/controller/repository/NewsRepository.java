package controller.repository;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import controller.model.RssFeedEntity;
import controller.model.news.News;
import controller.model.news.NewsEntry;

@Repository
public class NewsRepository {

	@Autowired
	private RssFeedRepostitory	rfr;

	public News getNews() {

		List<RssFeedEntity> rssList = rfr.getAll();

		News news = new News();
		try {
			List<NewsEntry> lne = new ArrayList<>();
			for (RssFeedEntity entity : rssList) {
				String url = entity.getUrl();
				try (XmlReader reader = new XmlReader(new URL(url))) {
					SyndFeed feed = new SyndFeedInput().build(reader);
					news.setPubDate(feed.getPublishedDate());

					System.out.println(url + " got " + feed.getEntries().size() + " entries");

					for (SyndEntry entry : feed.getEntries()) {
						NewsEntry ne = new NewsEntry();
						String rssDescription = entry.getDescription() != null ? entry.getDescription().getValue()
						      : "Keine Beschreibung vorhanden";
						ne.setDescription(Jsoup.parse(rssDescription).text());

						Matcher m = Pattern.compile("src=\\\"([^\\\"]*)\\\"").matcher(rssDescription);
						if (m.find()) {
							if (!m.group(1).equals("undefined")) {
								ne.setImageUrl(m.group(1));
							}

						}

						ne.setTitle(entry.getTitle());
						ne.setSource(entity.getDescription());
						lne.add(ne);
					}

				}
			}
			Collections.shuffle(lne);
			news.setList(lne);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return news;
	}

	public News getNewsById(Long id) {

		RssFeedEntity rss = rfr.getById(id);

		News news = new News();
		try {
			List<NewsEntry> lne = new ArrayList<>();
			String url = rss.getUrl();
			try (XmlReader reader = new XmlReader(new URL(url))) {
				SyndFeed feed = new SyndFeedInput().build(reader);
				news.setPubDate(feed.getPublishedDate());

				System.out.println(url + " got " + feed.getEntries().size() + " entries");

				for (SyndEntry entry : feed.getEntries()) {
					NewsEntry ne = new NewsEntry();
					String rssDescription = entry.getDescription() != null ? entry.getDescription().getValue() : "Keine Beschreibung vorhanden";
					ne.setDescription(Jsoup.parse(rssDescription).text());

					Matcher m = Pattern.compile("src=\\\"([^\\\"]*)\\\"").matcher(rssDescription);
					if (m.find()) {
						if (!m.group(1).equals("undefined")) {
							ne.setImageUrl(m.group(1));
						}

					}

					ne.setTitle(entry.getTitle());
					ne.setSource(rss.getDescription());
					lne.add(ne);
				}

			}
			news.setList(lne);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return news;
	}
}
