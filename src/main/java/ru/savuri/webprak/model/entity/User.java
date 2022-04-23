package ru.savuri.webprak.model.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class User implements SuperEntity<Long> {
    @Id
    @Column(nullable = false, name = "user_id")
    @GeneratedValue
    private Long id = null;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Order> orders;

    @Column(nullable = false, name = "full_name", length = 100)
    @NonNull
    private String fullName;

    @Column(nullable = false, name = "phone_number", length = 20)
    @NonNull
    private String phoneNumber;

    @Column(nullable = false, name = "address", length = 200)
    @NonNull
    private String address;

    @Column(nullable = false, name = "email", length = 100)
    @NonNull
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User other = (User) o;
        return Objects.equals(id, other.id) && fullName.equals(other.fullName)  && email.equals(other.email) && phoneNumber.equals(other.phoneNumber) && address.equals(other.address);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
