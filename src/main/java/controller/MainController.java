package controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@ComponentScan(basePackages = "controller.*")
@PropertySource("classpath:application.properties")
@EnableScheduling
@EnableAsync
@EnableAutoConfiguration()
@EnableJpaRepositories(basePackages = "controller.repository")
@ComponentScans({
	@ComponentScan(basePackages = {"controller"}),
})
public class MainController extends SpringBootServletInitializer {

	static {
		ApiContextInitializer.init();
	}
	
	@Configuration
	@Profile("default")
	@PropertySource(value = "file:${REST_CONF}/restserver.properties")
	static class Defaults{}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MainController.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(MainController.class, args);
	}
}