package com.lxyker;

import cn.hutool.Hutool;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class NoSpringTest {
    @Test
    void a() {
        DateTime offset = DateUtil.offset(new Date(), DateField.SECOND, 30);

        String token = JWT.create()
                .setExpiresAt(offset)
                .setKey("1234".getBytes(StandardCharsets.UTF_8))
                .sign();

        System.out.println(token);
    }

    @Test
    void b() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MzI3MjE2ODN9.55IR0-8O_HF6SD4iYnNW8xKhiJKXMOHoGA16CFcovOI";

        boolean isExpired = false;
        try {
            JWTValidator.of(token).validateAlgorithm(JWTSignerUtil.hs256("123".getBytes(StandardCharsets.UTF_8))).validateDate();
        } catch (ValidateException e) {
            System.out.println("错误");
        }

    }

    @Test
    void c(){
//        BUtil
    }
}
