package controller.repository.telegram;

import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import controller.repository.telegram.commands.LightsCommand;
import controller.repository.telegram.commands.TodoCommand;
import controller.repository.telegram.commands.TodoListCommand;

// https://github.com/rubenlagus/TelegramBotsExample/blob/master/src/main/java/org/telegram/updateshandlers/CommandsHandler.java
@Component
public class TelegramMikuCommandBot extends TelegramLongPollingCommandBot {

	@Value("${telegram.miku.apiKey}")
	private String telegramApiKey;
	
	@Value("${telegram.miku.botname}")
	private static String telegramBotname;
	
	@Autowired
	private TodoCommand todoCommand;
	
	@Autowired
	private TodoListCommand todoListCommand;
	
	@Autowired
	private LightsCommand lightsCommand;
	
	public TelegramMikuCommandBot() {
		super(telegramBotname);
	}
	
	@PostConstruct
	public void init() {
		register(todoCommand);
		register(todoListCommand);
		register(lightsCommand);
		register(new HelpCommand());
	}

	@Override
	public void processNonCommandUpdate(Update update) {
		
//		ArrayList<String> commadns = getRegisteredCommands().stream()
//				.map(command -> command.getCommandIdentifier() + " - " + command.getDescription());
		
		Message msg = update.getMessage();
		
		String response = "Could not find command '" + update.getMessage().getText() + "'";
		
		SendMessage sendMessageRequest = new SendMessage();
		sendMessageRequest.setChatId(msg.getChatId());
		sendMessageRequest.setText(response);
		sendMessageRequest.enableHtml(true);
		try {
			execute(sendMessageRequest);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
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
