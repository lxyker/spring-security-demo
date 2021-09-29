package com.lxyker;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.lxyker.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class MydemoApplicationTests {
    @Autowired
    private UserMapper userMapper;


    @Test
    void contextLoads() {
        String[] aa = {"a1", "a2"};
        String[] bb = {"b1", "b2", "b3"};
        List<String> a = Arrays.stream(aa).toList();
        List<String> b = Arrays.stream(bb).toList();
        String result = String.join(",", CollUtil.union(a, b));

        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("result = " + result);
    }

    @Test
    void a() {
        //        根据userID查询出所有角色
        List<String> roles = userMapper.getRolesByUserId(5);
        List<String> roleStrings = roles.stream().map(role -> "ROLE_" + role).collect(Collectors.toList());

        List<String> permissions = userMapper.getPermissionsByUserId(5);

        String result = String.join(",", CollUtil.union(roleStrings, permissions));

        System.out.println(result);
    }

    @Test
    void b(){
        String s = StrUtil.addPrefixIfNot("role", "ROLE_");
        System.out.println(s);
    }
}
