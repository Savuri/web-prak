package ru.savuri.webprak.model.dao;

import ru.savuri.webprak.model.entity.SuperEntity;

import java.util.Collection;
import java.util.List;

public interface SuperDAO<Entity extends SuperEntity<ID>, ID extends Number> {
    void save(Entity entity);
    void saveCollection(Collection<Entity> collection);
    void update(Entity entity);
    void delete(Entity entity);
    Entity getById(ID id);

    /*
    asc sorted by id
     */
    List<Entity> getAll();
    String getSearchPattern(String searchString);
}
