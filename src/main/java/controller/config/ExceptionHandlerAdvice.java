package controller.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import controller.exceptions.TemporaryException;
import controller.repository.telegram.TelegramService;


@ControllerAdvice
public class ExceptionHandlerAdvice {

	@Autowired
	private TelegramService repo;
	
	@ExceptionHandler(Exception.class)
	public void exceptionHandler(Exception e, HttpServletRequest request) {
		try {
			if (!(e instanceof TemporaryException)) {
				String stack = ExceptionUtils.getRootCauseMessage(e);
				repo.sendBotMessage(stack);
				
				File temp = File.createTempFile("stack", ".txt");
				
				BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
				bw.write(ExceptionUtils.getStackTrace(e));
				bw.close();
				
				repo.sendBotDocument(temp);
				temp.delete();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			e.printStackTrace();
		}
	}
}
