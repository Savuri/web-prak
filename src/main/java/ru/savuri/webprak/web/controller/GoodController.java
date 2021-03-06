package ru.savuri.webprak.web.controller;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.savuri.webprak.model.dao.GoodDAO;
import ru.savuri.webprak.model.dao.impl.GoodDAOImpl;
import ru.savuri.webprak.model.entity.Good;

import java.util.List;
import java.util.Objects;

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

    @PostMapping("/goodSave")
    public String goodSave(@RequestParam(name = "goodId", required = false) Long goodId,
                           @RequestParam(name = "assemblyPlace") String assemblyPlace,
                           @RequestParam(name = "manufacturer") String manufacturer,
                           @RequestParam(name = "model") String model,
                           @RequestParam(name = "price") Integer price,
                           @RequestParam(name = "quantity") Integer quantity,
                           @RequestParam(name = "typeInput") Good.GoodType typeInput,
                           @RequestParam(name = "description") String description,
                           Model prjModel) {
        if (goodId == null) {
            // create
            Good good = new Good(model, typeInput, price, quantity, manufacturer, assemblyPlace, description);
            goodDAO.save(good);

            return String.format("redirect:/goodInfo?goodId=%d", good.getId());
        } else {
            // update
            Good good = goodDAO.getById(goodId);
            good.setModel(model);
            good.setType(typeInput);
            good.setPrice(price);
            good.setQuantity(quantity);
            good.setManufacturer(manufacturer);
            good.setAssemblyPlace(assemblyPlace);
            good.setDescription(description);

            goodDAO.update(good);

            return String.format("redirect:/goodInfo?goodId=%d", good.getId());
        }
    }

    @PostMapping("/goodDelete")
    public String goodDelete(@RequestParam(name = "goodId") Long goodId, Model model) {
        try {
            goodDAO.delete(goodDAO.getById(goodId));
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            model.addAttribute("errorMsg", "This good can not be deleted because exist order which contain this good.");
            return "errorPage";
        }

        return "redirect:/goods";
    }

    @PostMapping("/goodSearch")
    public String goodSearch(@RequestParam(name = "typeInput", required = false) String type,
                             @RequestParam(name = "description", required = false) String description,
                             @RequestParam(name = "manufacturer", required = false) String manufacturer,
                             @NonNull Model model) {
        Good.GoodType typeInput = null;
        if (!Objects.equals(type, "")) {
            type = type.substring(0, type.length() - 1);
            typeInput = Good.GoodType.valueOf(type);
        }


        List<Good> goodList = goodDAO.getByFilter(GoodDAO.GoodFilter.builder()
                .manufacturer(manufacturer)
                .description(description)
                .type(typeInput)
                .build());


        model.addAttribute("goodList", goodList);

        return "/goods";
    }
}
