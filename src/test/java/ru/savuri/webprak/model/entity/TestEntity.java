package ru.savuri.webprak.model.entity;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "entity_for_test")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class TestEntity implements SuperEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @NonNull
    private String testContent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestEntity that = (TestEntity) o;
        return Objects.equals(id, that.id) && testContent.equals(that.testContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, testContent);
    }
}