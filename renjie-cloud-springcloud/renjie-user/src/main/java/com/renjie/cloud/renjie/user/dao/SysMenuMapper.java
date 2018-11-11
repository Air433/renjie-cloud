package com.renjie.cloud.renjie.user.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.renjie.cloud.renjie.user.entity.SysMenu;

import java.util.List;

/**
 * <p>
 * 菜单管理 Mapper 接口
 * </p>
 *
 * @author oyg
 * @since 2018-07-15
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> queryListParentId(Long parentId);

    List<SysMenu> queryNotButtonList();

}
