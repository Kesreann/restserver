package controller.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import controller.model.TodoEntity;
import controller.model.TodoStatus;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {

	public List<TodoEntity> findByCompleteFalse();
	
	public List<TodoEntity> findByStatusIn(List<TodoStatus> status);
}
