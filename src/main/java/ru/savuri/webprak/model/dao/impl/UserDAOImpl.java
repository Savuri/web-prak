package ru.savuri.webprak.model.dao.impl;

import org.springframework.stereotype.Repository;

import org.hibernate.Session;
import ru.savuri.webprak.model.dao.UserDAO;
import ru.savuri.webprak.model.entity.Order;
import ru.savuri.webprak.model.entity.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;


@Repository
public class UserDAOImpl extends SuperDAOImpl<User, Long> implements UserDAO {
    @Override
    public List<User> getByFullName(String fullName) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);

            Predicate predicate = criteriaBuilder.equal(root.get("fullName"), fullName);

            criteriaQuery.where(predicate);

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> getOrders(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select u.orders from User u where u.id = :id order by u.id").setParameter("id", id).getResultList();
        }
    }


}
