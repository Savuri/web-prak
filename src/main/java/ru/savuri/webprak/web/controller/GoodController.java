package ru.savuri.webprak.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.savuri.webprak.model.dao.GoodDAO;
import ru.savuri.webprak.model.dao.impl.GoodDAOImpl;
import ru.savuri.webprak.model.entity.Good;

import java.util.List;

@Controller
public class GoodController {
    private final GoodDAO goodDAO;

    /*
    Можно через @Autowired. Т.е.:
        @Autowired
        private final GoodDAO goodDAO;
     */
    GoodController(GoodDAO goodDAO) {
        this.goodDAO = goodDAO;
    }

    @GetMapping("/goods")
    public String goods(Model model) {
        List<Good> goodList = goodDAO.getAll();
        model.addAttribute("goodList", goodList);
        return "goods";
    }

    @GetMapping("/goodInfo")
    public String goodInfo(@RequestParam(name = "goodId") Long goodId, Model model) {
        Good good = goodDAO.getById(goodId);
        model.addAttribute("thisGood", good);

        return "goodInfo";
    }

    @GetMapping("/goodEdit")
    public String goodEdit(@RequestParam(name = "goodId", required = false) Long goodId, Model model) {
        if (goodId != null) {
            Good good = goodDAO.getById(goodId);

            model.addAttribute("good", good);
            model.addAttribute("isEditGood", true);
        } else {
            model.addAttribute("good", null);
            model.addAttribute("isEditGood", false);
        }

        return "goodEdit";
    }
}
