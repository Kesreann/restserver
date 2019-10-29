package controller.config;
import java.nio.charset.Charset;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import controller.util.Constants;


@Configuration
public class DefaultRestTemplateConfig {

	@Bean(name="defaultRestTemplate")
	@Primary
	public RestTemplate defaultRestTemplate() {
		return new RestTemplate();
	}

	@Bean(name="utf8RestTemplate")
	public RestTemplate utf8RestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName(Constants.DEFAULT_ENCODING.name())));
		return restTemplate;
	}

	@Bean(name="jsonRestTemplate")
	public RestTemplate jsonRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		return restTemplate;
	}

	@Bean(name="fileRestTemplate")
	public RestTemplate fileRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
		return restTemplate;
	}
}
