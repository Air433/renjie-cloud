package com.renjie.cloud.renjie.user.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.renjie.cloud.renjie.user.entity.SysUserToken;

/**
 * <p>
 * 系统用户Token Mapper 接口
 * </p>
 *
 * @author oyg
 * @since 2018-07-15
 */
public interface SysUserTokenMapper extends BaseMapper<SysUserToken> {

    SysUserToken queryByToken(String token);
}
