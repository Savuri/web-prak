package ru.savuri.webprak.web.DTO;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;



/*
 * По науке, как я понимаю, нужно для каждого form dto писать. Но зачем если у меня в других случаях всё прекрасно
 * через @RequestParam делалось.
 */
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class OrderDTO {
    public Long customerId;
    public Timestamp deliveryTime;
    public String deliveryPlace;
    @NonNull
    public List<Integer> list;

    public void addToList(Integer someInteger) {
        list.add(someInteger);
    }
}
