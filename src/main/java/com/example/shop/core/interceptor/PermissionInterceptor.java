package com.example.shop.core.interceptor;

import com.auth0.jwt.interfaces.Claim;
import com.example.shop.core.annotation.ScopeLevel;
import com.example.shop.exception.http.AuthException;
import com.example.shop.exception.http.ForbiddenException;
import com.example.shop.util.JwtToken;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class PermissionInterceptor extends HandlerInterceptorAdapter {
    public PermissionInterceptor() {
        super();
    }

    // 进入 Controller 前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ScopeLevel scopeLevel = this.getScopeLevel(handler);
        if (scopeLevel == null) return true; // 如果接口没有 ScopeLevel 注解，则表示是公开接口

        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.isEmpty(bearerToken) || !bearerToken.startsWith("Bearer ")) throw new AuthException(10004);

        String token = bearerToken.split(" ")[1];
        Map<String, Claim> map = JwtToken.getClaims(token);

        return this.hasPermission(scopeLevel, map);
    }

    // 视图渲染前执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    // 结束拦截后清理资源
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    // 获取注解
    private ScopeLevel getScopeLevel(Object handler) {
        if (handler instanceof  HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            return handlerMethod.getMethod().getAnnotation(ScopeLevel.class);
        }
        return null;
    }

    // 判断是否有权限
    private Boolean hasPermission(ScopeLevel scopeLevel, Map<String, Claim> map) {
        Integer methodScopeLevel = scopeLevel.value();
        Integer tokenScopeLevel = map.get("scope").asInt();
        if (tokenScopeLevel < methodScopeLevel) {
            throw new ForbiddenException(10006);
        }
        return true;
    }
}
