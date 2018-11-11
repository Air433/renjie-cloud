package com.renjie.cloud.renjie.user.service;

import com.renjie.cloud.renjie.user.entity.SysUser;
import com.renjie.cloud.renjie.user.entity.SysUserToken;

import java.util.Set;

public interface ShiroService {

    Set<String> getUserPermissions(long userId);

    SysUserToken queryByToken(String token);

    SysUser queryUser(Long userId);
}
