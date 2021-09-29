package com.lxyker.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxyker.entity.User;

public interface UserService extends IService<User> {
    User getByUsername(String username);

    String getUserAuthorityInfo(Integer userId);
}
