package com.lxyker.security;

import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import com.lxyker.common.R;
import com.lxyker.util.RedisUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();

        String token = request.getHeader("token");
        Object id = JWTUtil.parseToken(token).getPayload("id");
        System.out.println(id);
//        将redis中存储的权限信息清空
        RedisUtil.KeyOps.delete("authorityString:" + id);

        R r = R.ok().message("退出登录成功！");
        outputStream.write(JSONUtil.toJsonStr(r).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
