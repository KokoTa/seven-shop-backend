package com.example.shop.service;

import com.example.shop.model.Theme;
import com.example.shop.repository.ThemeRepository;
import com.example.shop.vo.ThemePureVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ThemeService {

    @Autowired
    ThemeRepository themeRepository;

    public List<Theme> getThemeByNames(List<String> names) {
        return themeRepository.findByNames(names);
    }

    public Optional<Theme> getThemeByName(String name) {
        return themeRepository.findByName(name);
    }
}
