package com.example.shop.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtToken {

    private static String jwtKey;
    private static Integer expiredTime;
    private static Integer userScope = 1;

    @Value("${condition.jwt-key}")
    public void setJwtKey(String jwtKey) {
        JwtToken.jwtKey = jwtKey;
    }
    @Value("${condition.token-expired}")
    public void setExpiredTime(Integer tokenExpired) {
        JwtToken.expiredTime = tokenExpired;
    }

    public static String makeToken(Long uid, Integer scope) {
        return getToken(uid, scope);
    }

    public static String makeToken(Long uid) {
        return getToken(uid, null);
    }

    private static String getToken(Long uid, Integer scope) {
        if (scope != null) userScope = scope;
        Map<String, Date> map = JwtToken.calculateExpiredTime();
        Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtKey); // 指定算法
        String token = JWT.create()
                .withClaim("uid", uid)
                .withClaim("scope", userScope)
                .withIssuedAt(map.get("now")) // 签发时间
                .withExpiresAt(map.get("expiredTime")) // 过期时间，这里传入的是具体过期的时间
                .sign(algorithm);
        return token;
    }

    private static Map<String, Date> calculateExpiredTime() {
        Map<String, Date> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.SECOND, JwtToken.expiredTime);
        map.put("now", now);
        map.put("expiredTime", calendar.getTime());
        return map;
    }
}
