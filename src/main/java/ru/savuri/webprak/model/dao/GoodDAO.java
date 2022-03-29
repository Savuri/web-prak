package ru.savuri.webprak.model.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.savuri.webprak.model.entity.Good;
import ru.savuri.webprak.model.entity.User;

import java.util.List;

public interface GoodDAO extends SuperDAO<Good, Long> {
    @AllArgsConstructor
    @Getter
    class GoodFilter {
        private Good.GoodType type;
        private String manufacturer;
        private String description;
    }

    List<User> getByFilter(GoodFilter filter);// TODO: 27/03/2022
}
