package controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import controller.model.TelegramMessageRequest;
import controller.repository.telegram.TelegramService;

@RestController
@RequestMapping("/api/telegram")
public class TelegramRestService {

	@Autowired
	private TelegramService service;
	
	@RequestMapping(value="message/send", method = RequestMethod.POST)
	@ResponseBody
	public void updateTodo(@RequestBody TelegramMessageRequest request) {
		try {
			service.sendBotMessage(request.getMessage());
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}
