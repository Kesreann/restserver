package controller.repository.telegram;

import java.io.File;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotSession;

@Service
public class TelegramService {
	
	@Autowired
	private TelegramMikuBot bot;
	
	@Autowired
	private TelegramMikuCommandBot commandBot;
	
	@Value("${telegram.chatid}")
	private String chatId;
	
	private List<BotSession> botSessions;
	
	@PostConstruct
	private void inti() throws TelegramApiRequestException {
		try {
			TelegramBotsApi botsApi = new TelegramBotsApi();
//			BotSession mikuSessoin = botsApi.registerBot(bot);
//			botSessions.add(mikuSessoin);
			
			BotSession mikuSessoin = botsApi.registerBot(commandBot);
			botSessions.add(mikuSessoin);
		} catch(Exception e) {
			System.out.println("Telebot could not be registered");
		}
	}
	
	@PreDestroy
	private void onShutdown() {
		for (BotSession botSession : botSessions) {
			botSession.stop();
		}
	}
	
	public void sendBotMessage(String message) throws TelegramApiException {
		SendMessage sendMessageRequest = new SendMessage();
		sendMessageRequest.setChatId(chatId);
		sendMessageRequest.setText(message);
		sendMessageRequest.enableHtml(true);
		commandBot.sendBotMessage(sendMessageRequest);
	}
	
	public void sendBotDocument(File f) throws TelegramApiException {
		SendDocument document = new SendDocument();
		document.setDocument(f);
		document.setChatId(chatId);
		commandBot.sendBotFile(document);
	}
	
}
