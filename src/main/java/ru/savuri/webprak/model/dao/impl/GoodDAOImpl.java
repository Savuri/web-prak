package ru.savuri.webprak.model.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.savuri.webprak.model.dao.GoodDAO;
import ru.savuri.webprak.model.entity.Good;
import ru.savuri.webprak.model.entity.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GoodDAOImpl extends SuperDAOImpl<Good, Long> implements GoodDAO {

    @Override
    public List<Good> getByFilter(GoodFilter filter) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Good> criteriaQuery = criteriaBuilder.createQuery(Good.class);
            Root<Good> root = criteriaQuery.from(Good.class);

            List<Predicate> predicates = new ArrayList<>();
            if (filter.getType() != null) predicates.add(criteriaBuilder.equal(root.get("type"), filter.getType()));
            if (filter.getDescription() != null) predicates.add(criteriaBuilder.like(root.get("description"), getSearchPattern(filter.getDescription())));
            if (filter.getManufacturer() != null) predicates.add(criteriaBuilder.like(root.get("manufacturer"), getSearchPattern(filter.getManufacturer())));

            if (predicates.size() != 0) {
                criteriaQuery.where(predicates.toArray(new Predicate[0]));
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));
            }

            return session.createQuery(criteriaQuery).getResultList();
        }
    }
}
