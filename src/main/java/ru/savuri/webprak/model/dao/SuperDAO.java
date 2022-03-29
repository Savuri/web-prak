package ru.savuri.webprak.model.dao;

import ru.savuri.webprak.model.entity.SuperEntity;

import java.util.List;

public interface SuperDAO<Entity extends SuperEntity<ID>, ID extends Number> {
    void save(Entity entity);
    void update(Entity entity);
    void delete(Entity entity);
    Entity getById(ID id);
    List<Entity> getAll();
    String getSearchPattern(String searchString);
}
