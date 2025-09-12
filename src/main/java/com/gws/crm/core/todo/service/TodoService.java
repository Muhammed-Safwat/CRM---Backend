package com.gws.crm.core.todo.service;

import com.gws.crm.core.todo.dto.UpdateTodoDto;
import com.gws.crm.core.todo.entities.Todo;
import com.gws.crm.core.todo.mapper.TodoMapper;
import com.gws.crm.core.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public List<Todo> getAll() {
        return todoRepository.findAll();
    }

    public ResponseEntity<?> getTodo(String id) {
        try {
            Long todoId = Long.parseLong(id);
            Optional<Todo> todo = todoRepository.findById(todoId);
            return todo.<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid ID format");
        }
    }

    public Todo create(Todo todo) {
        return todoRepository.save(todo);
    }

    public ResponseEntity<Todo> update(String id, UpdateTodoDto dto) {
        try {
            Long todoId = Long.parseLong(id);
            return todoRepository.findById(todoId).map(existing -> {
                TodoMapper.updateEntity(existing, dto);
                return ResponseEntity.ok(todoRepository.save(existing));
            }).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Void> delete(String id) {
        try {
            Long todoId = Long.parseLong(id);
            if (todoRepository.existsById(todoId)) {
                todoRepository.deleteById(todoId);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Todo> toggleCompleted(String id) {
        try {
            Long todoId = Long.parseLong(id);
            return todoRepository.findById(todoId).map(todo -> {
                todo.setCompleted(!todo.isCompleted());
                todoRepository.save(todo);
                return ResponseEntity.ok(todo);
            }).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
