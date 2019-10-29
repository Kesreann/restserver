package controller.config;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Configuration
public class DefaultJsonConfig {

	public static class JsonDateSerializer extends JsonSerializer<Date> {
		@Override
		public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
			if (value == null) {
				gen.writeNull();
			}
			gen.writeNumber(value.getTime());
		}

		@Override
		public Class<Date> handledType() {
			return Date.class;
		}
	}

	public static class JsonDateDeserializer extends JsonDeserializer<Date> {
		@Override
		public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			JsonToken t = p.getCurrentToken();
			if (t == JsonToken.VALUE_NULL) {
				return null;
			}
			if (t == JsonToken.VALUE_STRING) {
				String str = StringUtils.trim(p.getText());
				if (str.length() == 0) {
					return null;
				}
				return ctxt.parseDate(str);
			}
			if (t == JsonToken.VALUE_NUMBER_INT) {
				return new Date(p.getLongValue());
			}
			throw new IllegalStateException("Problem deserializing date.");
		}

		@Override
		public Class<?> handledType() {
			return Date.class;
		}
	}

	@Bean
	public Jackson2ObjectMapperBuilder jacksonMapper() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.serializationInclusion(JsonInclude.Include.NON_NULL);
		builder.serializers(new JsonDateSerializer());
		builder.deserializers(new JsonDateDeserializer());
		return builder;
	}
}
