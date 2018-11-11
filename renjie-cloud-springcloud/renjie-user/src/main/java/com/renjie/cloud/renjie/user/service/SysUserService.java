package com.renjie.cloud.renjie.user.service;

import com.baomidou.mybatisplus.service.IService;
import com.renjie.cloud.renjie.common.utils.PageUtils;
import com.renjie.cloud.renjie.user.entity.SysUser;
import com.renjie.cloud.renjie.user.form.RegiserUserReq;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.List;
import java.util.Map;

/**
 * @
 */
public interface SysUserService extends IService<SysUser> {

    SysUser queryByUserName(String username);

    /**
     * 查询用户的所有菜单ID
     * @param userId
     * @return
     */
    List<Long> queryAllMenuId(Long userId);

    void testAspect(ProceedingJoinPoint point) throws Throwable;

    void testBspect(ProceedingJoinPoint point) throws Throwable;


    PageUtils queryPage(Map<String, Object> params);

    boolean updatePassword(Long userId, String password, String newPassword);

    void update(SysUser user);

    void save(SysUser sysUser);

    void save(RegiserUserReq userReq);

    void testRedis() throws Exception;
}
