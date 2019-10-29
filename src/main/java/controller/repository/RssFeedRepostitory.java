package controller.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import controller.db.JdbcRepositoryRowMapper;
import controller.model.RssFeedEntity;

@Service
public class RssFeedRepostitory {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<RssFeedEntity> getAll() {
		String query = "select * from rssfeeds";
		return jdbcTemplate.query(query.toString(), new JdbcRepositoryRowMapper<>(RssFeedEntity.class));	
	}
	
	public RssFeedEntity getById(Long id) {
		String query = "select * from rssfeeds where id = " + id;
		return jdbcTemplate.query(query.toString(), new JdbcRepositoryRowMapper<>(RssFeedEntity.class))
				.stream()
				.findFirst()
				.orElse(null);	
	}
	
	public void addFeed(RssFeedEntity rfe) {
		jdbcTemplate.update("insert into rssfeeds (url, description) values (?, ?)", rfe.getUrl(), rfe.getDescription());
	}
	
	public void removeFeed(Integer id) {
		jdbcTemplate.execute("delete from rssfeeds where id = " + id);
	}

}
