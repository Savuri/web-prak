package ru.savuri.webprak.model.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.savuri.webprak.model.dao.GoodDAO;
import ru.savuri.webprak.model.dao.OrderDAO;
import ru.savuri.webprak.model.dao.UserDAO;
import ru.savuri.webprak.model.entity.Good;
import ru.savuri.webprak.model.entity.Order;
import ru.savuri.webprak.model.entity.OrderGood;
import ru.savuri.webprak.model.entity.User;

import java.sql.Array;
import java.sql.Time;
import java.util.*;
import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
class GoodDAOImplTest {
    @Autowired
    private GoodDAO goodDAO;
    @Autowired
    private SessionFactory sessionFactory;

    private final List<Good> goods = new ArrayList<>();

    @BeforeEach
    void setUp() {
        goods.add(new Good("LG-123-100", Good.GoodType.TV, 100, 1000, "LG", "Китай", "Диагональ=32\nТехнологии умного дома с webOS"));
        goods.add(new Good("LG-123-101", Good.GoodType.TV, 110, 100, "LG", "Китай", "Диагональ=12"));
        goods.add(new Good("PANASONIC-33LgeqQ", Good.GoodType.KITCHEN, 110, 10, "PANASONIC", "Россия", "Загрузка=15кг\nСкорость отжима=1400 об/мин "));
        goods.add(new Good("Lenovo-yoga-LT12", Good.GoodType.COMPUTER, 110000, 10, "LENOVO", "Корея", "ОЗУ=8 гб\n"));

        goodDAO.saveCollection(goods);
    }

    @BeforeAll
    @AfterEach
    void tearDown() {
        goods.clear();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE goods RESTART IDENTITY CASCADE;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE hibernate_sequence RESTART WITH 1;").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void getByFilter() {
        GoodDAO.GoodFilter goodFilter;
        List<Good> res;

        goodFilter = GoodDAO.getFilterBuilder().build();
        res = goodDAO.getByFilter(goodFilter);
        assertThat(res).containsExactly(goods.get(0), goods.get(1), goods.get(2), goods.get(3));

        goodFilter = GoodDAO.getFilterBuilder().description("Диагональ=").build();
        res = goodDAO.getByFilter(goodFilter);
        assertThat(res).containsExactly(goods.get(0), goods.get(1));

        goodFilter = GoodDAO.getFilterBuilder().manufacturer("LG").build();
        res = goodDAO.getByFilter(goodFilter);
        assertThat(res).containsExactly(goods.get(0), goods.get(1));

        goodFilter = GoodDAO.getFilterBuilder().type(Good.GoodType.COMPUTER).build();
        res = goodDAO.getByFilter(goodFilter);
        assertThat(res).containsExactly(goods.get(3));

        goodFilter = GoodDAO.getFilterBuilder().type(Good.GoodType.KITCHEN).manufacturer("PANASONIC").description("Загрузка=15кг").build();
        res = goodDAO.getByFilter(goodFilter);
        assertThat(res).containsExactly(goods.get(2));
    }
}