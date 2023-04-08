package com.example.xiaosage.controller.dto;

import com.example.xiaosage.entity.SysMenu;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String phone;
    private String salt;
    private String role;
    private List<SysMenu> menus;
}
