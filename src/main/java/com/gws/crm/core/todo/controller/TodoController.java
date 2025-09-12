package com.gws.crm.core.todo.controller;

import com.gws.crm.core.todo.dto.CreateTodoDto;
import com.gws.crm.core.todo.dto.UpdateTodoDto;
import com.gws.crm.core.todo.entities.Todo;
import com.gws.crm.core.todo.mapper.TodoMapper;
import com.gws.crm.core.todo.service.TodoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@AllArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public List<Todo> getAll() {
        return todoService.getAll() ;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return todoService.getTodo(id);
    }

    @PostMapping
    public Todo create(@RequestBody CreateTodoDto dto) {
        Todo todo = TodoMapper.fromCreateDto(dto);
        return todoService.create(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable String id, @RequestBody UpdateTodoDto dto) {
        return todoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
       return todoService.delete(id);
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<Todo> toggleCompleted(@PathVariable String id) {
        return todoService.toggleCompleted(id);
    }

}
