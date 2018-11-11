package com.renjie.cloud.renjie.user.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.renjie.cloud.renjie.user.dao.SysRoleMenuMapper;
import com.renjie.cloud.renjie.user.entity.SysRoleMenu;
import com.renjie.cloud.renjie.user.service.SysRoleMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author oyg
 * @Date 2018/7/29/21:03
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {
    @Override
    public List<Long> queryMenuIdList(Long roleId) {
        return baseMapper.queryMenuIdList(roleId);
    }

    @Override
    public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
        //先删除角色与菜单关系
        deleteBatch(new Long[]{roleId});
    }

    @Override
    public int deleteBatch(Long[] roleIds) {
        return baseMapper.deleteBatch(roleIds);
    }
}
