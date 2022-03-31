package ru.savuri.webprak.model.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.savuri.webprak.model.dao.OrderDAO;
import ru.savuri.webprak.model.dao.UserDAO;
import ru.savuri.webprak.model.entity.Order;
import ru.savuri.webprak.model.entity.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
class UserDAOImplTest {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private SessionFactory sessionFactory;

    private final List<User> users = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();


    @BeforeEach
    void setUp() {
        users.add(new User("Иван Иванович Иванов", "7912341234", "ул. Пушкина г. Эквестрия", "some1@mail.ru"));
        users.add(new User("Иван Иванович Иванов", "8912341234", "ул. Фушкина г. Эквестрия", "some2@mail.ru"));
        users.add(new User("Рван Иванович Иванов", "9912341234", "ул. Аушкина г. Эквестрия", "some3@mail.ru"));

        orders.add(new Order(new HashSet<>(), users.get(0), Timestamp.valueOf("2023-01-01 10:12:12"), "ул. Пушкина г. Эквестрия", Order.Status.CANCELED));
        orders.add(new Order(new HashSet<>(), users.get(0), Timestamp.valueOf("2022-01-01 10:12:12"), "ул. Пушкина г. Эквестрия", Order.Status.DELIVERED));
        orders.add(new Order(new HashSet<>(), users.get(1), Timestamp.valueOf("2021-06-01 10:12:12"), "ул. Пушкина г. Эквестрия", Order.Status.SHIPPED));

        userDAO.saveCollection(users);
        orderDAO.saveCollection(orders);
    }

    @BeforeAll
    @AfterEach
    void tearDown() {
        users.clear();
        orders.clear();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE TABLE orders CASCADE").executeUpdate();
            session.createSQLQuery("TRUNCATE TABLE users CASCADE").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE hibernate_sequence RESTART WITH 1;").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void getByFullName() {
        List<User> res1 = userDAO.getByFullName("Иван Иванович Иванов");
        assertThat(res1).containsExactlyInAnyOrder(users.get(0), users.get(1));

        List<User> res2 = userDAO.getByFullName("Рван Иванович Иванов");
        assertThat(res2).containsExactlyInAnyOrder(users.get(2));

        List<User> res3 = userDAO.getByFullName("Ни одно имя не подходит");
        assertThat(res3).isEmpty();
    }

    @Test
    void getOrders() {
        List<Order> res1 = userDAO.getOrders(users.get(0).getId());
        assertThat(res1).containsExactly(orders.get(0), orders.get(1));

        List<Order> res2 = userDAO.getOrders(users.get(1).getId());
        assertThat(res2).containsExactly(orders.get(2));

        List<Order> res3 = userDAO.getOrders(users.get(2).getId());
        assertThat(res3).isEmpty();
    }
}