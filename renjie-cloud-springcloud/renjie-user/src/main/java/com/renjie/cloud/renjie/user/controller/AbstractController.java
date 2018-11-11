package com.renjie.cloud.renjie.user.controller;

import com.renjie.cloud.renjie.user.entity.SysUser;
import com.renjie.cloud.renjie.user.entity.SysUser;
import com.renjie.cloud.renjie.common.response.AirResult;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author oyg
 * @Date 2018/7/21/12:53
 */
public abstract class AbstractController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected SysUser getUser(){
        return (SysUser) SecurityUtils.getSubject().getPrincipal();
    }

    protected Long getUserId(){
        return getUser().getUserId();
    }

    @ExceptionHandler
    public AirResult exp(HttpServletRequest request, Exception ex){

        return AirResult.error(ex.getMessage());
    }
}
