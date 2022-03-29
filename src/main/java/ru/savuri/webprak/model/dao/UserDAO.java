package ru.savuri.webprak.model.dao;

import ru.savuri.webprak.model.entity.Order;
import ru.savuri.webprak.model.entity.User;

import java.util.List;

public interface UserDAO extends SuperDAO<User, Long> {
    List<User> getByFullName(String fullName);
    List<Order> getOrders(Long id);
}
