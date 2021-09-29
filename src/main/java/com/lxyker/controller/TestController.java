package com.lxyker.controller;

import com.lxyker.common.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @PreAuthorize("hasAuthority('user:add')")
    @RequestMapping("a")
    public R a() {
        return R.ok().message("新增员工");
    }

    @PreAuthorize("hasRole('hr')")
    @RequestMapping("b")
    public R b() {
        return R.ok().message("有人事的角色");
    }

    @PreAuthorize("hasRole('admin')")
    @RequestMapping("aaa")
    public R f(Authentication authentication) {
        System.out.println("----------------");
        System.out.println(authentication.getAuthorities());
        System.out.println("----------------");
        System.out.println(authentication.getName());
        System.out.println("----------------");
        authentication.getDetails();
        System.out.println("----------------");
        return R.ok().message("fff");
    }
}
