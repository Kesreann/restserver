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

import controller.model.TodoEntity;
import controller.model.TodoStatus;
import controller.service.TodoService;

// https://github.com/rubenlagus/TelegramBotsExample/blob/master/src/main/java/org/telegram/commands/HelloCommand.java

@Component
public class TodoCommand extends BotCommand {

	@Value("${telegram.chatid}")
	private String		  chatId;

	@Autowired
	private TodoService todoService;

	public TodoCommand() {
		super("todo", "create a new Todo");
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		System.out.println(chatId + " " + absSender + " " + user + " " + chat + " " + arguments + " ");

		if (user.getId() == Integer.parseInt(chatId)) {
			String message = "";
			if (arguments.length != 0) {
				TodoEntity newTodo = new TodoEntity();
				newTodo.setStatus(TodoStatus.TODO);
				newTodo.setComplete(false);
				newTodo.setDescription(String.join(" ", arguments));
				todoService.addTodo(newTodo);
				message = "Added Todo to the list:\n" + newTodo.getDescription();
			} else {
				message = "Please enter a todo to add";
			}
			
			SendMessage sendMessageRequest = new SendMessage();
			sendMessageRequest.setChatId(chatId);
			sendMessageRequest.setText(message);
			sendMessageRequest.enableHtml(true);
			try {
				absSender.execute(sendMessageRequest);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}

	}

}
