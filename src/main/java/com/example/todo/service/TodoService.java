package com.example.todo.service;

import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository repo;

    public TodoService(TodoRepository repo) {
        this.repo = repo;
    }

    public List<Todo> findAll(String filter) {
        if (filter == null || filter.isEmpty() || filter.equalsIgnoreCase("ALL")) {
            return repo.findAllByOrderByCreatedAtAsc();
        }
        if (filter.equalsIgnoreCase("PENDING")) {
            return repo.findByStatusOrderByCreatedAtAsc(Todo.Status.PENDING);
        }
        if (filter.equalsIgnoreCase("DONE")) {
            return repo.findByStatusOrderByCreatedAtAsc(Todo.Status.DONE);
        }
        return repo.findAllByOrderByCreatedAtAsc();
    }

    public Todo save(Todo todo) { return repo.save(todo); }
    public Optional<Todo> findById(Long id) { return repo.findById(id); }
    public void deleteById(Long id) { repo.deleteById(id); }

    public void markDone(Long id) {
        repo.findById(id).ifPresent(t -> {
            t.setStatus(Todo.Status.DONE);
            repo.save(t);
        });
    }
    public long countAll() { return repo.count(); }
    public long countByStatus(Todo.Status status) { return repo.countByStatus(status); }
}