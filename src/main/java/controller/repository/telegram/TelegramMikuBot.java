package controller.repository.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import controller.service.TelegramCommandService;

@Component
public class TelegramMikuBot extends TelegramLongPollingBot {

	@Value("${telegram.miku.apiKey}")
	private String telegramApiKey;
	
	@Value("${telegram.miku.botname}")
	private String telegramBotname;
	
	@Value("${telegram.chatid}")
	private String chatId;
	
	@Autowired
	private TelegramCommandService commandService;
	
	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			if (update.getMessage().getChatId().equals(Long.valueOf(chatId))) {
				try {
					commandService.commandListener(update.getMessage().getText());
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			} else {
				SendMessage sendMessageRequest = new SendMessage();
				sendMessageRequest.setChatId(chatId);
				StringBuffer sb = new StringBuffer();
				sb.append("Someone else tried to use me :( \n");
				if (update.getMessage().getChat().getUserName() != null) {
					sb.append("Username: " + update.getMessage().getChat().getUserName() + " \n");
				}
				if (update.getMessage().getChat().getFirstName() != null) {
					sb.append("Firstname: " + update.getMessage().getChat().getFirstName() + " \n");
				}
				if (update.getMessage().getChat().getLastName() != null) {
					sb.append("Lastname: " + update.getMessage().getChat().getLastName() + " \n");
				}
				if (update.getMessage().getChatId() != null) {
					sb.append("ChatId: " + update.getMessage().getChatId() + " \n");
				}
				
				sendMessageRequest.setText(sb.toString());
				try {
					sendBotMessage(sendMessageRequest);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String getBotUsername() {
		return telegramBotname;
	}

	@Override
	public String getBotToken() {
		return telegramApiKey;
	}
	
	public void sendBotMessage(SendMessage message) throws TelegramApiException {
		execute(message);
	}
	
	public void sendBotFile(SendDocument document) throws TelegramApiException {
		execute(document);
	}

}
