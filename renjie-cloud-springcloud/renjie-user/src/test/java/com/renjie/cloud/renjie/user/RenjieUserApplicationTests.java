package com.renjie.cloud.renjie.user;

import com.renjie.cloud.renjie.user.dao.SysUserMapper;
import com.renjie.cloud.renjie.user.entity.SysUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RenjieUserApplication.class)
public class RenjieUserApplicationTests {

  @Test
  public void contextLoads() {
  }
  @Resource
  private SysUserMapper sysUserMapper;

  @Test
  public void t1(){
    SysUser sysUser = sysUserMapper.selectById(1);
    System.err.println(sysUser.getUsername());
  }
}
