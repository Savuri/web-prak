package ru.savuri.webprak.model.dao;

import lombok.Builder;
import lombok.Getter;
import ru.savuri.webprak.model.entity.Good;
import ru.savuri.webprak.model.entity.User;

import java.util.List;

public interface GoodDAO extends SuperDAO<Good, Long> {
    List<Good> getByFilter(GoodFilter filter);

    @Getter
    @Builder
     class GoodFilter {
        private Good.GoodType type;
        private String manufacturer;
        private String description;
    }

    static GoodFilter.GoodFilterBuilder getFilterBuilder() {
        return GoodFilter.builder();
    }
}
