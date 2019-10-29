package controller.repository.telegram.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import controller.service.AuroraService;

// https://github.com/rubenlagus/TelegramBotsExample/blob/master/src/main/java/org/telegram/commands/HelloCommand.java

@Component
public class LightsCommand extends BotCommand {

	@Value("${telegram.chatid}")
	private String chatId;
	
	@Autowired
	private AuroraService auroraService;
	
	public LightsCommand() {
		super("lights", "Toggles the lights on and off");
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {		
		if (user.getId() == Integer.parseInt(chatId)) {
			boolean isOn = auroraService.toggleLights();
			String response = "Lights are now " + (isOn ? "on" : "off");
			

			SendMessage sendMessageRequest = new SendMessage();
			sendMessageRequest.setChatId(chatId);
			sendMessageRequest.setText(response);
			sendMessageRequest.enableHtml(true);
			try {
				absSender.execute(sendMessageRequest);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
		
	}

}
