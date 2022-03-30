package ru.savuri.webprak.model.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.bytecode.enhance.internal.bytebuddy.EnhancerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import ru.savuri.webprak.model.dao.SuperDAO;
import ru.savuri.webprak.model.entity.SuperEntity;

import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;


@Repository
abstract class SuperDAOImpl<Entity extends SuperEntity<ID>, ID extends Number> implements SuperDAO<Entity, ID> {
    protected SessionFactory sessionFactory;

    protected Class<Entity> persistentClass;

    public SuperDAOImpl(Class<Entity> entityClass) {
        this.persistentClass = entityClass;
    }

    @Autowired
    public void setSessionFactory(LocalSessionFactoryBean sessionFactory) {
        this.sessionFactory = sessionFactory.getObject();
    }

    @Override
    public Entity getById(ID id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(persistentClass, id);
        }
    }

    @Override
    public List<Entity> getAll() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<Entity> criteriaQuery = session.getCriteriaBuilder().createQuery(persistentClass);
            criteriaQuery.from(persistentClass);
            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    @Override
    public void save(Entity entity) {
        try (Session session = sessionFactory.openSession()) {
            if (entity.getId() != null) {
                entity.setId(null);
            }
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void saveCollection(Collection<Entity> collection) {
        for (Entity entity : collection) {
            save(entity);
        }
    }


    @Override
    public void update(Entity entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Entity entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public String getSearchPattern(String searchString) {
        return "%" + searchString + "%";
    }
}
