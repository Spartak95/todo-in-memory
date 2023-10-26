package com.apress.todo.repository;

import static java.util.Comparator.comparing;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.apress.todo.domain.ToDo;
import org.springframework.stereotype.Repository;

@Repository
public class ToDoRepository implements CommonRepository<ToDo> {
    private final Map<String, ToDo> toDos = new HashMap<>();

    @Override
    public ToDo save(ToDo domain) {
        ToDo result = toDos.get(domain.getId());

        if (result != null) {
            result.setModified(LocalDateTime.now());
            result.setDescription(domain.getDescription());
            result.setCompleted(domain.isCompleted());
            domain = result;
        }

        toDos.put(domain.getId(), domain);

        return toDos.get(domain.getId());
    }

    @Override
    public Iterable<ToDo> save(Collection<ToDo> domains) {
        domains.forEach(this::save);
        return findAll();
    }

    @Override
    public void delete(ToDo domain) {
        toDos.remove(domain.getId());
    }

    @Override
    public ToDo findById(String id) {
        return toDos.get(id);
    }

    @Override
    public Iterable<ToDo> findAll() {
        return toDos.entrySet().stream()
            .sorted(comparing(toDoEntry -> toDoEntry.getValue().getCreated()))
            .map(Entry::getValue).toList();
    }
}
