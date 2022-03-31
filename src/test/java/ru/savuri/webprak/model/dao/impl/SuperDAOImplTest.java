package ru.savuri.webprak.model.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.test.context.TestPropertySource;
import ru.savuri.webprak.model.dao.TestEntityDAO;
import ru.savuri.webprak.model.entity.TestEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
class SuperDAOImplTest {
    private SessionFactory sessionFactory;

    @Autowired
    private TestEntityDAO testEntityDAO;

    @Autowired
    public void setSessionFactory(LocalSessionFactoryBean sessionFactory) {
        this.sessionFactory = sessionFactory.getObject();
    }

    @BeforeAll
    @AfterEach
    void setup() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE entity_for_test RESTART IDENTITY;").executeUpdate();
            session.createSQLQuery("ALTER SEQUENCE entity_for_test_id_seq RESTART WITH 1;").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void getById() {
        List<TestEntity> entityList = new ArrayList<>();
        entityList.add(new TestEntity("123"));
        entityList.add(new TestEntity("321"));
        entityList.add(new TestEntity("213"));

        testEntityDAO.saveCollection(entityList);

        TestEntity res = testEntityDAO.getById(1L);
        assertNotEquals(null, res); // fails if both null
        assertEquals("123", res.getTestContent());

        res = testEntityDAO.getById(2L);
        assertNotEquals(null, res);
        assertEquals("321", res.getTestContent());

        res = testEntityDAO.getById(3L);
        assertNotEquals(null, res);
        assertEquals("213", res.getTestContent());

        res = testEntityDAO.getById(4L);
        assertNull(res);
    }

    @Test
    void getAll() {
        List<TestEntity> entityList = new ArrayList<>();
        entityList.add(new TestEntity("123"));
        entityList.add(new TestEntity("321"));
        entityList.add(new TestEntity("213"));

        testEntityDAO.saveCollection(entityList);
        assertEquals(testEntityDAO.getAll(), entityList);
    }

    @Test
    void save() {
        TestEntity entity0 = new TestEntity("123");
        TestEntity entity1 = new TestEntity("321");

        entity1.setId(1000L);

        testEntityDAO.save(entity0);
        testEntityDAO.save(entity1);

        List<TestEntity> res = testEntityDAO.getAll();
        assertEquals(2, res.size());
        assertEquals(entity0, res.get(0));
        assertEquals(2, res.get(1).getId());
    }

    @Test
    void saveCollection() {
        List<TestEntity> testEntityList = new ArrayList<>();
        testEntityList.add(new TestEntity("123"));
        testEntityList.add(new TestEntity("321"));

        testEntityList.get(1).setId(1000L);

        testEntityDAO.saveCollection(testEntityList);

        List<TestEntity> res = testEntityDAO.getAll();
        assertEquals(2, res.size());
        assertEquals(testEntityList.get(0), res.get(0));
        assertEquals(2, res.get(1).getId());
    }

    @Test
    void update() {
        TestEntity entity = new TestEntity("123");

        testEntityDAO.save(entity);

        entity.setTestContent("New");
        testEntityDAO.update(entity);

        assertEquals("New", testEntityDAO.getById(1L).getTestContent());
    }

    @Test
    void delete() {
        List<TestEntity> entities = new ArrayList<>();
        entities.add(new TestEntity("123"));
        entities.add(new TestEntity("321"));
        entities.add(new TestEntity("213"));

        testEntityDAO.saveCollection(entities);

        testEntityDAO.delete(entities.get(1));

        assertEquals(2, testEntityDAO.getAll().size());
        assertEquals("123", testEntityDAO.getAll().get(0).getTestContent());
        assertEquals("213", testEntityDAO.getAll().get(1).getTestContent());
    }

    @Test
    void getSearchPattern() {
        assertEquals("%123%", testEntityDAO.getSearchPattern("123"));
    }
}