package ru.savuri.webprak.model.dao.impl;
import org.springframework.stereotype.Repository;
import ru.savuri.webprak.model.dao.OrderGoodDAO;
import ru.savuri.webprak.model.entity.OrderGood;

@Repository
public class OrderGoodDAOImpl extends SuperDAOImpl<OrderGood, Long> implements OrderGoodDAO {
}
