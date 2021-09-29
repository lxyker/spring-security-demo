package com.lxyker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxyker.entity.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT r.role_str FROM USER u LEFT JOIN user_role ur ON u.id = ur.user_id LEFT JOIN role r ON ur.role_id = r.id WHERE u.id = #{userId}")
    List<String> getRolesByUserId(Integer userId);

    @Select("SELECT DISTINCT p.permission_str FROM USER u LEFT JOIN user_role ur ON u.id = ur.user_id LEFT JOIN role r ON ur.role_id = r.id LEFT JOIN role_permission rp ON r.id = rp.role_id LEFT JOIN permission p ON rp.permission_id = p.id WHERE u.id = #{userId}")
    List<String> getPermissionsByUserId(Integer userId);

    @Select("SELECT id FROM user WHERE username = #{username}")
    Integer getIdByUsername(String username);
}
