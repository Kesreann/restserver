package controller.rest;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import controller.model.TodoEntity;
import controller.model.TodoStatus;
import controller.service.TodoService;

@RestController
@RequestMapping("/api/todo")
public class TodoRestService {

	@Autowired
	private TodoService service;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<TodoEntity> getAll() {
		return service.getAll();
	}
	
	@RequestMapping(value="open", method = RequestMethod.GET)
	@ResponseBody
	public Integer findOpen() {
		return service.getOpen().size();
	}
	
	@RequestMapping(value="{id}", method = RequestMethod.GET)
	@ResponseBody
	public TodoEntity getById(@PathVariable Long id) {
		return service.getById(id);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public TodoEntity addTodo(@RequestBody TodoEntity todo) {
		return service.addTodo(todo);
	}
	
	@RequestMapping(value="{id}/update", method = RequestMethod.POST)
	@ResponseBody
	public TodoEntity updateTodo(@RequestBody TodoEntity todo, @PathVariable Long id) {
		if (todo.getId() == null) {
			todo.setId(id);
		}
		return service.updateTodo(todo);
	}
	
	@RequestMapping(value="{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteTodo(@PathVariable Long id) {
		service.deleteTodo(id);
	}
	
	
}
