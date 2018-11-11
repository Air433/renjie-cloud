package com.renjie.cloud.renjie.user.controller;

import com.renjie.cloud.renjie.common.annotation.SysLogAn;
import com.renjie.cloud.renjie.common.exception.RRException;
import com.renjie.cloud.renjie.common.utils.Constant;
import com.renjie.cloud.renjie.user.entity.SysMenu;
import com.renjie.cloud.renjie.user.service.ShiroService;
import com.renjie.cloud.renjie.user.service.SysMenuService;
import com.renjie.cloud.renjie.common.response.AirResult;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author oyg
 * @Date 2018/7/21/19:33
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController{

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private ShiroService shiroService;

    /**
     * 导航菜单
     * @return
     */
    @GetMapping("/nav")
    public AirResult nav(){
        List<SysMenu> menuList = sysMenuService.getUserMenuList(getUserId());
        Set<String> permissions = shiroService.getUserPermissions(getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("menuList", menuList);
        map.put("permissions", permissions);
        return AirResult.success(map);
    }
    /**
     * 菜单列表
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:menu:list")
    public List<SysMenu> list(){
        List<SysMenu> menuList = sysMenuService.selectList(null);
        menuList.forEach(x-> {
            SysMenu parentMenu = sysMenuService.selectById(x.getParentId());
            if (parentMenu != null){
                x.setParentName(parentMenu.getName());
            }
        });
        return menuList;
    }

    /**
     * 选择菜单
     * @return
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:menu:select")
    public AirResult select(){
        List<SysMenu> menuList = sysMenuService.queryNotButtonList();

        SysMenu root = new SysMenu();
        root.setMenuId(0L);
        root.setName("一级菜单");
        root.setParentId(-1L);
        root.setOpen(true);
        menuList.add(root);

        Map<String, Object> map = new HashMap<>();
        map.put("menuList", menuList);
        return AirResult.success(map);
    }

    /**
     * 菜单信息
     * @param menuId
     * @return
     */
    @GetMapping("/info/{menuId}")
    @RequiresPermissions("sys:menu:info")
    public AirResult info(@PathVariable("menuId") Long menuId){
        SysMenu sysMenu = sysMenuService.selectById(menuId);
        Map<String, Object> map = new HashMap<>();
        map.put("menu", sysMenu);
        return AirResult.success(map);
    }


    @SysLogAn("保存菜单")
    @PostMapping("/save")
    @RequiresPermissions("sys:menu:save")
    public AirResult save(@RequestBody SysMenu menu){
        //数据校验
        verifyForm(menu);

        sysMenuService.insert(menu);

        return AirResult.success();
    }

    @SysLogAn("修改菜单")
    @PostMapping("/update")
    @RequiresPermissions("sys:menu:update")
    public AirResult update(@RequestBody SysMenu menu){

        verifyForm(menu);

        sysMenuService.updateById(menu);

        return AirResult.success();

    }

    @SysLogAn("删除菜单")
    @PostMapping("/delete/{menuId}")
    @RequiresPermissions("sys:menu:delete")
    public AirResult delete(@PathVariable("menuId") long menuId){
        if (menuId <= 3l){
            return AirResult.error("系统菜单，不能删除");
        }

        List<SysMenu> menuList = sysMenuService.queryListParentId(menuId);
        if (menuList.size() > 0){
            return AirResult.error("请先删除子菜单或按钮");
        }

        boolean delete = sysMenuService.delete(menuId);

        return AirResult.success(delete);
    }

    @GetMapping("/exception")
    public void ex(){
        if (1==1){
            throw new LockedAccountException("测试异常");
        }
    }

    private void verifyForm(SysMenu menu){
        if (StringUtils.isBlank(menu.getName())){
            throw new RRException("菜单名称不能为空");
        }

        if (menu.getParentId() == null){
            throw new RRException("上级菜单不能为空");
        }

        //菜单
        if (menu.getType() == Constant.MenuType.MENU.getValue()){
            if (StringUtils.isBlank(menu.getUrl())){
                throw new RRException("菜单URL不能为空");
            }
        }

        //上级菜单类型
        int parentType = Constant.MenuType.CATALOG.getValue();
        if (menu.getParentId() != 0){
            SysMenu parentMenu = sysMenuService.selectById(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if (menu.getType() == Constant.MenuType.CATALOG.getValue() ||
            menu.getType() == Constant.MenuType.MENU.getValue()){
            if (parentType != Constant.MenuType.CATALOG.getValue()){
                throw new RRException("上级菜单只能为目录类型");
            }
            return;
        }

        //按钮
        if (menu.getType() == Constant.MenuType.BUTTON.getValue()){
            if (parentType != Constant.MenuType.MENU.getValue()){
                throw new RRException("上级菜单只能为菜单类型");
            }
            return;
        }
    }
}
