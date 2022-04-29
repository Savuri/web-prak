package ru.savuri.webprak.model.dao.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.savuri.webprak.model.dao.OrderDAO;
import ru.savuri.webprak.model.entity.Order;

import java.util.List;

@Repository
public class OrderDAOImpl extends SuperDAOImpl<Order, Long> implements OrderDAO {
}