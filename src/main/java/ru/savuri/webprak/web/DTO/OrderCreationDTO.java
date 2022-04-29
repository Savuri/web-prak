package ru.savuri.webprak.web.DTO;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.savuri.webprak.model.entity.Good;

import java.time.LocalDateTime;
import java.util.List;


/*
 * По науке, как я понимаю, нужно для каждого формы dto писать. Но зачем если у меня в других случаях всё прекрасно
 * через @RequestParam делалось.
 */
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class OrderCreationDTO {
    @NonNull
    private Long customerId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deliveryDateTime;
    @NonNull
    private String deliveryPlace;
    @NonNull
    private List<Integer> purchaseQuantity;
}
