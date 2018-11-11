package com.renjie.cloud.renjie.user.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.renjie.cloud.renjie.common.annotation.RedisLock;
import com.renjie.cloud.renjie.common.exception.RRException;
import com.renjie.cloud.renjie.common.utils.Constant;
import com.renjie.cloud.renjie.common.utils.MapUtils;
import com.renjie.cloud.renjie.common.utils.PageUtils;
import com.renjie.cloud.renjie.common.utils.Query;
import com.renjie.cloud.renjie.user.dao.SysRoleMapper;
import com.renjie.cloud.renjie.user.dao.SysUserMapper;
import com.renjie.cloud.renjie.user.entity.SysUser;
import com.renjie.cloud.renjie.user.form.RegiserUserReq;
import com.renjie.cloud.renjie.user.service.SysLogService;
import com.renjie.cloud.renjie.user.service.SysUserRoleService;
import com.renjie.cloud.renjie.user.service.SysUserService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author oyg
 * @Date 2018/7/21/16:26
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysLogService sysLogService;
    @Resource
    private SysRoleMapper roleMapper;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public SysUser queryByUserName(String username) {
        return baseMapper.queryByUserName(username);
    }

    @Override
    public List<Long> queryAllMenuId(Long userId) {
        return null;
    }

    @Override
    public void testAspect(ProceedingJoinPoint point) throws Throwable {
        System.err.println("-------------进入a的方法里---------------");
        point.proceed();

    }

    @Override
    public void testBspect(ProceedingJoinPoint point) throws Throwable {
        System.err.println("-------------进入b的方法里---------------");
        point.proceed();
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String username = (String) params.get("username");
        Long createUserId = (Long) params.get("createUserId");

        Page<SysUser> page = this.selectPage(
                new Query<SysUser>(params).getPage(),
                new EntityWrapper<SysUser>()
                        .like(StringUtils.isNotBlank(username), "username", username)
                        .eq(createUserId != null, "create_user_id", createUserId)
        );

        return new PageUtils(page);
    }

    @Override
    public boolean updatePassword(Long userId, String password, String newPassword) {
        SysUser sysUser = new SysUser();
        sysUser.setPassword(newPassword);
        return this.update(sysUser,
                new EntityWrapper<SysUser>().eq("user_id", userId).eq("password", password));
    }

    @Override
    @Transactional
    public void update(SysUser user) {
        if (StringUtils.isBlank(user.getPassword())){
            user.setPassword(null);
        }else {
            user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
        }
        this.updateById(user);

        //检查角色是否越权

    }

    @Transactional
    @Override
    public void save(SysUser user) {
        user.setCreateTime(new Date());

        String salt = RandomStringUtils.randomAlphabetic(20);
        user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
        user.setSalt(salt);

        checkRole(user);


    }

    @Transactional(rollbackFor = Exception.class, timeout = 5000)
    @Override
    public void save(RegiserUserReq userReq) {

        String username = userReq.getUsername();

        List list = this.selectByMap(new MapUtils().put("username", username));

        if (list!=null && list.size() >0){
            throw new RRException("用户名已存在");
        }

        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        String salt = RandomStringUtils.randomAlphabetic(20);
        sysUser.setPassword(new Sha256Hash(userReq.getPassword(), salt).toHex());
        sysUser.setSalt(salt);

        List<Long> roleId = new ArrayList<>();
        roleId.add(2l);
        sysUser.setRoleIdList(roleId);
        this.insert(sysUser);

        sysUserRoleService.saveOrUpdate(sysUser.getUserId(), sysUser.getRoleIdList());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(lockName = "SysUserServiceImpl.testRedis",expire = 15000)
    public void testRedis() throws Exception {
        //Thread.sleep(10000);

        SysUser sysUser = new SysUser();
        sysUser.setUsername("苏杉杉33333");
        sysUser.setPassword("3433443");
        baseMapper.insert(sysUser);

        if (1==1){
            throw new Exception("异常。。。。。");
        }
        //Thread.sleep(20000);
        System.err.println("-------------------超时------------------");
    }

    private void checkRole(SysUser user){
        if (user.getRoleIdList() == null || user.getRoleIdList().size() == 0){
            return;
        }
        //如果不是超级管理员，则需要判断用户的角色是否自己创建
        if (user.getCreateUserId() == Constant.SUPER_ADMIN){
            return;
        }

    }


}
