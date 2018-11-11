package com.renjie.cloud.renjie.user.service;

import com.baomidou.mybatisplus.service.IService;
import com.renjie.cloud.renjie.user.entity.SysUserToken;
import com.renjie.cloud.renjie.common.response.AirResult;

/**
 * @Author oyg
 * @Date 2018/7/21/16:46
 */
public interface SysUserTokenService extends IService<SysUserToken> {

    AirResult createToekn(long userId);

    void logout(Long userId);
}
