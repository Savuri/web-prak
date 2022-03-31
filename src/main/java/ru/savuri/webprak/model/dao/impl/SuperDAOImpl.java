package ru.savuri.webprak.model.dao.impl;

import net.jodah.typetools.TypeResolver;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.bytecode.enhance.internal.bytebuddy.EnhancerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import ru.savuri.webprak.model.dao.SuperDAO;
import ru.savuri.webprak.model.entity.SuperEntity;
import ru.savuri.webprak.model.entity.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Repository
abstract class SuperDAOImpl<Entity extends SuperEntity<ID>, ID extends Number> implements SuperDAO<Entity, ID> {
    protected SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    private final Class<Entity> persistentClass = (Class<Entity>) ((Class<?>[]) TypeResolver.resolveRawArguments(SuperDAO.class, getClass()))[0];

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
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Entity> criteriaQuery = criteriaBuilder.createQuery(persistentClass);
            criteriaQuery.from(persistentClass);

            List<Entity> res = session.createQuery(criteriaQuery).getResultList();
            res.sort((Entity e1, Entity e2)-> (int) (e1.getId() - e2.getId()));
            return res;
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
