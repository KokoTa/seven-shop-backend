package com.example.shop.api.v1;

import com.example.shop.exception.http.NotFoundException;
import com.example.shop.model.Theme;
import com.example.shop.service.ThemeService;
import com.example.shop.vo.ThemePureVo;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/theme")
@Validated
public class ThemeController {

    @Autowired
    ThemeService themeService;

    @GetMapping("/names")
    public List<ThemePureVo> getThemeByNames(
            @RequestParam(name = "names") String names
    ) {
        List<String> nameList = Arrays.asList(names.split(","));
        List<Theme> themeList = themeService.getThemeByNames(nameList);
        List<ThemePureVo> voList = new ArrayList<>();
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();


        themeList.forEach(t -> {
            ThemePureVo vo = mapper.map(t, ThemePureVo.class);
            voList.add(vo);
        });

        return voList;
    }

    @GetMapping("/name/{name}/with_spu")
    public Theme getThemeByName(
            @PathVariable(name = "name") String name
    ) {
        Optional<Theme> optionalTheme = themeService.getThemeByName(name);
        return optionalTheme.orElseThrow(() -> new NotFoundException(10000));
    }

}
