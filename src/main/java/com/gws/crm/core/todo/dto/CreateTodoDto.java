package com.gws.crm.core.todo.dto;

import com.gws.crm.core.todo.entities.Todo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateTodoDto {
    private String title;
    private String description;
    private Todo.Priority priority;
    private LocalDateTime dueDate;
}
