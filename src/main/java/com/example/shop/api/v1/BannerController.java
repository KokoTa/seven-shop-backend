package com.example.shop.api.v1;

import com.example.shop.core.annotation.ScopeLevel;
import com.example.shop.exception.http.NotFoundException;
import com.example.shop.model.Banner;
import com.example.shop.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping("/name/{name}")
    @ScopeLevel
    public Banner getByName(@PathVariable String name) {
        Banner banner = bannerService.getByName(name);
        if (banner == null) throw new NotFoundException(30005);
        return banner;
    }

}
