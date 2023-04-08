package com.example.xiaosage.config.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.xiaosage.common.Constants;
import com.example.xiaosage.config.AuthAccess;
import com.example.xiaosage.entity.SysUser;
import com.example.xiaosage.exception.ServiceException;
import com.example.xiaosage.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private SysUserServiceImpl sysUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");
        //如果不是映射到方法则直接通过
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod h = (HandlerMethod)handler;
        AuthAccess authAccess = (AuthAccess)h.getMethodAnnotation(AuthAccess.class);
        if (authAccess != null){
            return true;
        }
        //执行验证
        if (StrUtil.isBlank(token)){
            throw new ServiceException(Constants.CODE_401,"无token，请重新登录");
        }
        //获取token中的userId
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException e) {
            throw new ServiceException(Constants.CODE_401,"token验证失败，请重新登录");
        }
        //根据token中的userId查询数据库
        SysUser sysUser = sysUserService.getById(userId);
        if (sysUser == null) {
            throw new ServiceException(Constants.CODE_401,"用户不存在，请重新登录");
        }
        //用户密码加签验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(sysUser.getPassword())).build();
        try {
            jwtVerifier.verify(token);  //验证token
        } catch (JWTVerificationException e) {
            throw new ServiceException(Constants.CODE_401,"token验证失败，请重新登录");
        }
        return true;
    }
}
