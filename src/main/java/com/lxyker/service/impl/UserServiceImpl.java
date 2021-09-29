package com.lxyker.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxyker.entity.User;
import com.lxyker.mapper.UserMapper;
import com.lxyker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User getByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username).last("limit 1");
        return getOne(wrapper);
    }

    /**
     * 根据userId查询出所有角色和权限，并拼接成字符串返回
     *
     * @param userId 用户id
     * @return "ROLE_admin,ROLE_hr,user:add,salary:read"
     */
    @Override
    public String getUserAuthorityInfo(Integer userId) {
//        根据userID查询出所有角色
        List<String> roles = userMapper.getRolesByUserId(userId);
        List<String> roleStrings = roles.stream().map(role -> "ROLE_" + role).collect(Collectors.toList());
//        根据userID查询出所有权限
        List<String> permissions = userMapper.getPermissionsByUserId(userId);

        return String.join(",", CollUtil.union(roleStrings, permissions));
    }
}
