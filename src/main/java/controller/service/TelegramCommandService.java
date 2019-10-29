package controller.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import controller.model.TodoEntity;
import controller.model.TodoStatus;
import controller.repository.telegram.TelegramService;

@Service
public class TelegramCommandService {

	@Autowired
	private TelegramService	telegramService;

	@Autowired
	private TodoService		todoService;

	@Autowired
	private AuroraService	auroraService;

	private List<String>		commandList	= new ArrayList<>();

	public void commandListener(String textMessage) throws TelegramApiException {
		commandList.add("/todo");
		commandList.add("/todolist");
		commandList.add("/lights");

		String response = "Could not find command '" + textMessage + "'";
		if (textMessage.toLowerCase().startsWith("/help")) {
			response = commandList.stream().collect(Collectors.joining("\n"));
		}
		if (textMessage.toLowerCase().startsWith("/lights") || textMessage.toLowerCase().startsWith("/licht")) {
			response = handleAurora(textMessage);
		}
		if (textMessage.toLowerCase().startsWith("/todo")) {
			response = handleTodo(textMessage);
		}
		if (textMessage.toLowerCase().startsWith("/todolist")) {
			response = getTodoList();
		}

		telegramService.sendBotMessage(response);
	}

	private String handleAurora(String testMessage) {
		boolean isOn = auroraService.toggleLights();
		String response = "Lights are now " + (isOn ? "on" : "off");

		return response;
	}

	private String handleTodo(String textMessage) {
		String[] cmdList = textMessage.split(" ");
		TodoEntity newTodo = new TodoEntity();
		newTodo.setStatus(TodoStatus.TODO);
		newTodo.setComplete(false);
		newTodo.setDescription(Arrays.asList(cmdList).stream().skip(1).collect(Collectors.joining(" ")));
		todoService.addTodo(newTodo);

		return "Added Todo to the list:\n" + newTodo.getDescription();
	}

	private String getTodoList() {
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
		return sb.toString();
	}
}
