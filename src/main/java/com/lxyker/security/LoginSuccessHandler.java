package com.lxyker.security;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import com.lxyker.common.R;
import com.lxyker.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${jwt.expire}")
    Integer expire;
    @Value("${jwt.secret}")
    String secret;

    final UserMapper userMapper;

    @Autowired
    public LoginSuccessHandler(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();

//        生成jwt并放置到 响应头 中
        String token = JWT.create()
                .setKey(secret.getBytes(StandardCharsets.UTF_8))
                .setExpiresAt(DateUtil.offset(DateUtil.date(), DateField.SECOND, expire))
                .setPayload("username", authentication.getName())
                .setPayload("id", userMapper.getIdByUsername(authentication.getName()))
                .sign();

        httpServletResponse.setHeader("Authorization", token);

        R r = R.ok().message("登录成功");
        outputStream.write(JSONUtil.toJsonStr(r).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
