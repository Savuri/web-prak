package ru.savuri.webprak.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.savuri.webprak.model.dao.*;
import ru.savuri.webprak.model.entity.Good;
import ru.savuri.webprak.model.entity.Order;
import ru.savuri.webprak.model.entity.OrderGood;
import ru.savuri.webprak.model.entity.User;
import ru.savuri.webprak.web.DTO.OrderCreationDTO;

import java.sql.Time;
import java.util.*;

@Controller
public class OrderController {
    private final OrderDAO orderDAO;
    private final GoodDAO goodDAO;
    private final OrderGoodDAO orderGoodDAO;

    private final UserDAO userDAO;

    OrderController(OrderDAO orderDAO, GoodDAO goodDAO, OrderGoodDAO orderGoodDAO, UserDAO userDAO) {
        this.orderDAO = orderDAO;
        this.goodDAO = goodDAO;
        this.orderGoodDAO = orderGoodDAO;
        this.userDAO = userDAO;
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        List<Order> orderList = orderDAO.getAll();
        model.addAttribute("orderList", orderList);
        return "orders";
    }

    @GetMapping("/orderInfo")
    public String orderInfo(@RequestParam(name = "orderId") Long orderId,
                            Model model) {
        Order order = orderDAO.getById(orderId);
        model.addAttribute("order", order);

        return "orderInfo";
    }

    @GetMapping("/orderCreate")
    public String orderCreate(Model model) {
        List<Good> goodList = goodDAO.getAll();
        List<Integer> purchaseQuantity = new ArrayList<Integer>(goodList.size());

        for (int i = 0; i < goodList.size(); ++i) purchaseQuantity.add(0);

        OrderCreationDTO orderCreationDTO = new OrderCreationDTO();
        orderCreationDTO.setPurchaseQuantity(purchaseQuantity);

        model.addAttribute("goodList", goodList);
        model.addAttribute("form", orderCreationDTO);

        return "orderCreate";
    }

    @PostMapping("orderSave")
    public String orderSave(@ModelAttribute(name = "form") OrderCreationDTO orderCreationDTO, Model model) {
        Order order = new Order();

        List<Integer> purchaseQuantity = orderCreationDTO.getPurchaseQuantity();
        List<Good> goodList = goodDAO.getAll();

        List<Good> goodsToUpdate = new ArrayList<>();
        List<OrderGood> orderGoodsToLoad = new ArrayList<>();

        for (int i = 0; i < purchaseQuantity.size(); ++i) {
            if (purchaseQuantity.get(i) > 0) {
                Good good = goodList.get(i);
                OrderGood orderGood = new OrderGood(order, goodList.get(i), purchaseQuantity.get(i), goodList.get(i).getPrice());

                goodsToUpdate.add(good);
                orderGoodsToLoad.add(orderGood);

                Set<OrderGood> thisGoodOrders = good.getOrderGoods();
                thisGoodOrders.add(orderGood);
                good.setOrderGoods(thisGoodOrders);
            }
        }

        User customer = userDAO.getById(orderCreationDTO.getCustomerId());

        Set<OrderGood> tmp = new HashSet<>(orderGoodsToLoad.size());

        order.setUser(customer);
        order.setOrderGoods(tmp);
        order.setDeliveryPlace(orderCreationDTO.getDeliveryPlace());
        order.setDeliveryTime(orderCreationDTO.getDeliveryDateTime());
        order.setStatus(Order.Status.PROCESSING);

        orderDAO.save(order);

        for (int i = 0; i < goodList.size(); ++i) {
            goodDAO.update(goodList.get(i));
        }

        return String.format("redirect:/orderInfo?orderId=%d", order.getId());
    }

    @PostMapping("orderDelete")
    public String orderDelete(@RequestParam(name = "orderId") Long orderId) {
        orderDAO.delete(orderDAO.getById(orderId));


        return "redirect:/orders";
    }
}
