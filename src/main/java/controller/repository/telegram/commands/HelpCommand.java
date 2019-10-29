package controller.repository.telegram.commands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

public class HelpCommand extends BotCommand {

   private final ICommandRegistry commandRegistry;
	
	public HelpCommand(ICommandRegistry commandRegistry) {
		super("help", "Get all the commands");
		// TODO Auto-generated constructor stub
      this.commandRegistry = commandRegistry;
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
      StringBuilder helpMessageBuilder = new StringBuilder();

		
		for (IBotCommand botCommand : commandRegistry.getRegisteredCommands()) {
         helpMessageBuilder.append(botCommand.toString()).append("\n\n");
		}
		
		SendMessage helpMessage = new SendMessage();
      helpMessage.setChatId(chat.getId().toString());
      helpMessage.enableHtml(true);
      helpMessage.setText(helpMessageBuilder.toString());

      try {
          absSender.execute(helpMessage);
      } catch (TelegramApiException e) {
      }
		
	}

}
