package com.lxyker.security;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.lxyker.entity.User;
import com.lxyker.service.UserService;
import com.lxyker.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Value("${jwt.expire}")
    Integer expire;

    final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        数据库查询出用户对象，校验是否存在
        User user = userService.getByUsername(username);
        if (ObjectUtil.isEmpty(user)) {
            throw new UsernameNotFoundException("用户名不存在！");
        }
//        return new org.springframework.security.core.userdetails.User(
//                user.getUsername(), user.getPassword(),
//                getUserAuthority(user.getId())
//        );
        return new AccountUser(
                user.getId(), user.getUsername(), user.getPassword(),
                getUserAuthority(user.getId())
        );
    }

    // 根据userID查询出其权限信息
    List<GrantedAuthority> getUserAuthority(Integer userId) {
//        根据userId从redis中查询其权限字符串
        String authorityString = RedisUtil.StringOps.get("authorityString:" + userId);
        if (StrUtil.isBlank(authorityString)) {
            log.info("userId->【{}】没有缓存权限信息，则去数据库查询，然后存入缓存", userId);
            authorityString = userService.getUserAuthorityInfo(userId);
            RedisUtil.StringOps.setEx("authorityString:" + userId, authorityString, expire, TimeUnit.SECONDS);
        }
//        角色、菜单操作权限字符串
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authorityString);
    }
}
