package ru.savuri.webprak.model.dao.impl;

import org.springframework.stereotype.Repository;
import ru.savuri.webprak.model.dao.TestEntityDAO;
import ru.savuri.webprak.model.entity.TestEntity;

@Repository
public class TestEntityDAOImpl extends SuperDAOImpl<TestEntity, Long> implements TestEntityDAO {
}
