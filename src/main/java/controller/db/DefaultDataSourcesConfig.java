package controller.db;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DefaultDataSourcesConfig {

	@Bean(name="dataSource")
	@Primary
	@ConfigurationProperties(prefix = "restserver.core.datasource")
	public DataSource coreDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name="transactionManager")
	@Primary
	public PlatformTransactionManager transactionManager() {
		return new JpaTransactionManager();
	}
}
