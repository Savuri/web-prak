package ru.savuri.webprak.model.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class Order implements SuperEntity<Long> {
    @Id
    @Column(nullable = false, name = "order_id")
    @GeneratedValue
    private Long id = null;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order")
    @ToString.Exclude
    @NonNull
    private Set<OrderGood> orderGoods;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, name = "delivery_time")
    @NonNull
    private Timestamp deliveryTime;

    @Column(nullable = false, name = "delivery_place")
    @NonNull
    private String deliveryPlace;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @NonNull
    private Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order other = (Order) o;
        return Objects.equals(id, other.id) && deliveryTime.equals(other.deliveryTime) &&
                deliveryPlace.equals(other.deliveryPlace) && status.equals(other.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deliveryTime, deliveryPlace, status);
    }

    //TODO
    public enum Status {
        PLACEHOLDER1,
        PLACEHOLDER2
    }
}
