package controller.model.news;

import java.util.Date;
import java.util.List;

@lombok.Getter
@lombok.Setter
public class News {

	private Date pubDate;
	private List<NewsEntry> list;
}
