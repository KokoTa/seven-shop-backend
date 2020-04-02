package com.example.shop.core.interceptor;

import com.auth0.jwt.interfaces.Claim;
import com.example.shop.core.annotation.ScopeLevel;
import com.example.shop.core.local.LocalUser;
import com.example.shop.exception.http.AuthException;
import com.example.shop.exception.http.ForbiddenException;
import com.example.shop.exception.http.NotFoundException;
import com.example.shop.model.User;
import com.example.shop.service.UserService;
import com.example.shop.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class PermissionInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

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

        Boolean valid = this.hasPermission(scopeLevel, map);

        if (valid) {
            this.setToThreadLocal(map);
        }

        return valid;
    }

    // 视图渲染前执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    // 结束拦截后清理资源
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清空线程数据
        LocalUser.clear();
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

    // 存储本地用户
    private void setToThreadLocal(Map<String, Claim> map) {
        Long uid = map.get("uid").asLong();
        Integer scope = map.get("scope").asInt();
        User user = userService.getUserById(uid);

        if (user == null) throw new NotFoundException(10000);

        LocalUser.set(user, scope);
    }
}
