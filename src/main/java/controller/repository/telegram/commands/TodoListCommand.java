package controller.repository.telegram.commands;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import controller.model.TodoEntity;
import controller.service.AuroraService;
import controller.service.TodoService;

// https://github.com/rubenlagus/TelegramBotsExample/blob/master/src/main/java/org/telegram/commands/HelloCommand.java

@Component
public class TodoListCommand extends BotCommand {

	@Value("${telegram.chatid}")
	private String chatId;

	@Autowired
	private TodoService todoService;
	
	public TodoListCommand() {
		super("todolist", "Show all todos that are open");
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {		
		if (user.getId() == Integer.parseInt(chatId)) {
			List<TodoEntity> todos = todoService.getOpen();
			StringBuffer sb = new StringBuffer();

			for (TodoEntity todoEntity : todos) {
				if (!StringUtils.isEmpty(todoEntity.getTitle())) {
					sb.append("<b>" + todoEntity.getTitle() + "</b>\n");
				}
				if (!StringUtils.isEmpty(todoEntity.getDescription())) {
					sb.append(todoEntity.getDescription() + "\n");
				}
				sb.append("\n");
			}
			String response = sb.toString();
			

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



