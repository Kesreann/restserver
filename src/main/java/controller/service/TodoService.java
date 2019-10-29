package controller.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import controller.model.TodoEntity;
import controller.model.TodoStatus;
import controller.repository.TodoRepository;

@Service
public class TodoService {
	
	@Autowired
	private TodoRepository repo;

	public List<TodoEntity> getAll() {
		return repo.findByCompleteFalse();
	}
	
	public List<TodoEntity> getOpen() {
		return repo.findByStatusIn(Arrays.asList(TodoStatus.TODO, TodoStatus.IN_PROGRESS));
	}
	
	public TodoEntity getById(Long id) {
		return repo.findOne(id);
	}
	
	public TodoEntity addTodo(TodoEntity todo) {
		return repo.save(todo);
	}
	
	public TodoEntity updateTodo(TodoEntity todo) {
		return repo.save(todo);
	}
	
	public void deleteTodo(Long id) {
		repo.delete(id);
	}
}
