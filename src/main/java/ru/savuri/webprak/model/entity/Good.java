package ru.savuri.webprak.model.entity;


import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "goods")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class Good implements SuperEntity<Long> {
    @Id
    @Column(nullable = false, name = "good_id")
    @GeneratedValue
    private Long id = null;

    @OneToMany(mappedBy = "good", fetch = FetchType.EAGER)
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<OrderGood> orderGoods;

    @Column(nullable = false, name = "model", length = 100)
    @NonNull
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @NonNull
    private GoodType type;

    @Column(nullable = false, name = "price")
    @NonNull
    private Integer price;

    @Column(nullable = false, name = "quantity")
    @NonNull
    private Integer quantity;

    @Column(nullable = false, name = "manufacturer", length = 100)
    @NonNull
    private String manufacturer;

    @Column(nullable = false, name = "assembly_place", length = 200)
    @NonNull
    private String assemblyPlace;

    @Column(nullable = false, name = "description", length = 10000)
    @NonNull
    private String description;

    @AllArgsConstructor
    @Getter
    public enum GoodType {
        KITCHEN("Kitchen"),
        BATHROOM("Bathroom"),
        TV("TV"),
        COMPUTER("Computer"),
        OTHER("Other");

        private final String name;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    /*
    id сравнивается через Objects из-за случая объектов с id = null
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Good other = (Good) o;
        return Objects.equals(id, other.id) && model.equals(other.model) && type.equals(other.type) &&
                price.equals(other.price) && quantity.equals(other.quantity) && manufacturer.equals(other.manufacturer)
                && assemblyPlace.equals(other.assemblyPlace) && description.equals(other.description);
    }
}
