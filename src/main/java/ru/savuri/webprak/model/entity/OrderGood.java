package ru.savuri.webprak.model.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "ordergoods")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class OrderGood implements SuperEntity<Long>  {
    @Id
    @Column(nullable = false, name = "order_good_id")
    @GeneratedValue
    private Long id = null;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "order_id")
    @ToString.Exclude
    @NonNull
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "good_id")
    @ToString.Exclude
    @NonNull
    private Good good;

    @Column(nullable = false, name = "purchase_quantity")
    @NonNull
    Integer purchaseQuantity;

    @Column(nullable = false, name = "purchase_price")
    @NonNull
    Integer purchasePrice; // in $

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderGood orderGood = (OrderGood) o;
        return Objects.equals(id, orderGood.id) && purchaseQuantity.equals(orderGood.purchaseQuantity) 
                && purchasePrice.equals(orderGood.purchasePrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, purchaseQuantity, purchasePrice);
    }
}
