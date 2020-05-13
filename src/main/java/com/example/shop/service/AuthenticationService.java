package com.example.shop.service;

import com.example.shop.exception.http.JsonConvertException;
import com.example.shop.exception.http.ParameterException;
import com.example.shop.model.User;
import com.example.shop.repository.UserRepository;
import com.example.shop.util.JwtToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${wx.code2session}")
    private String code2sessionUrl;
    @Value("${wx.appId}")
    private String appId;
    @Value("${wx.appSecret}")
    private String appSecret;

    @Autowired
    private UserRepository userRepository;

    public String code2session(String code) {
        String url = MessageFormat.format(this.code2sessionUrl, this.appId, this.appSecret, code);
        String token = null;

        // 发送请求
        RestTemplate restTemplate = new RestTemplate();
        String sessionText = restTemplate.getForObject(url, String.class);

        try {
            Map<String, Object> session = objectMapper.readValue(sessionText, HashMap.class);
            token = registerUser(session);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonConvertException(90001);
        }

        return token;
    }

    private String registerUser(Map<String, Object> session) {
        String openid = (String) session.get("openid");
        if (openid == null)
            throw new ParameterException(20004);

        User user = userRepository.findByOpenid(openid);
        if (user != null) {
            return JwtToken.makeToken(user.getId());
        }
        User newUser = User.builder().openid(openid).build();
        userRepository.save(newUser);
        Long id = newUser.getId();

        return JwtToken.makeToken(id);
    }
}
