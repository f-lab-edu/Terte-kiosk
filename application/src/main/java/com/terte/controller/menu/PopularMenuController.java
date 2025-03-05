package com.terte.controller.menu;

import com.terte.service.menu.PopularMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PopularMenuController {

    private final PopularMenuService popularMenuService;
    @GetMapping
    public List<Long> getPopularMenus(@RequestParam(defaultValue = "5") int topN) {
        Long storeId = 1L;
        return popularMenuService.getPopularMenus(storeId, topN);
    }
}
