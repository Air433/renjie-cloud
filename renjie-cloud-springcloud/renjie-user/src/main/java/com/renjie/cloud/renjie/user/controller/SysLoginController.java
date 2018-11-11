package com.renjie.cloud.renjie.user.controller;

import com.renjie.cloud.renjie.common.annotation.SysLogNotUser;
import com.renjie.cloud.renjie.common.lock.RedisLock;
import com.renjie.cloud.renjie.common.validator.ValidatorUtils;
import com.renjie.cloud.renjie.user.entity.SysUser;
import com.renjie.cloud.renjie.user.form.RegiserUserReq;
import com.renjie.cloud.renjie.user.request.SysLoginRequest;
import com.renjie.cloud.renjie.user.service.SysUserService;
import com.renjie.cloud.renjie.user.service.SysUserTokenService;
import com.renjie.cloud.renjie.common.response.AirResult;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @Author oyg
 * @Date 2018/7/21/12:52
 */
@RestController
@Scope(value = "request")
public class SysLoginController extends AbstractController{

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private RedisLock redisLock;

    @PostMapping("/sys/login")
    public AirResult login(@RequestBody SysLoginRequest request){
        if (request == null){
            return AirResult.error("请求信息为空，请检查");
        }

        if (request.getUsername() == null || request.getUsername().trim().equals("")){
            return AirResult.error("用户名不能为空");
        }
        SysUser user = sysUserService.queryByUserName(request.getUsername());

        if (user == null || !user.getPassword().equals(new Sha256Hash(request.getPassword(), user.getSalt()).toHex())){
            return AirResult.error("账号或密码不正确");
        }

        if (user.getStatus() == 0){
            return AirResult.error("账号已被锁定，请联系管理员");
        }

        AirResult result = sysUserTokenService.createToekn(user.getUserId());

        return result;
    }

    @PostMapping("/sys/logout")
    public AirResult logout(){
        sysUserTokenService.logout(getUserId());
        return AirResult.success("退出成功");
    }


    @SysLogNotUser("注册用户")
    @PostMapping("/sys/regier")
    public AirResult regiser(@RequestBody RegiserUserReq userReq){
        ValidatorUtils.validateEntity(userReq);

        sysUserService.save(userReq);

        return AirResult.success();
    }

    @GetMapping("/test-redis")
    public AirResult testRedis(){
        System.err.println(redisLock);
        redisLock.setLockKey("test-redis");
        redisLock.setLockRequestId(UUID.randomUUID().toString()+Thread.currentThread().getId());
        redisLock.lock();
        redisLock.sleepBySecond(60);
        redisLock.unlock();
        return null;
    }
}
