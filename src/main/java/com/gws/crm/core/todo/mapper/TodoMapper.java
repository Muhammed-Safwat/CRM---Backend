package com.gws.crm.core.todo.mapper;

import com.gws.crm.core.todo.dto.CreateTodoDto;
import com.gws.crm.core.todo.dto.UpdateTodoDto;
import com.gws.crm.core.todo.entities.Todo;

public class TodoMapper {

    public static Todo fromCreateDto(CreateTodoDto dto) {
        return Todo.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priority(dto.getPriority())
                .dueDate(dto.getDueDate())
                .completed(false)
                .build();
    }

    public static void updateEntity(Todo todo, UpdateTodoDto dto) {
        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());
        todo.setPriority(dto.getPriority());
        todo.setDueDate(dto.getDueDate());
        todo.setCompleted(dto.isCompleted());
    }
}
