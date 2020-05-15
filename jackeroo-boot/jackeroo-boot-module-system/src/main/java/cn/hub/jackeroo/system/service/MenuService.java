package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.system.entity.Menu;
import cn.hub.jackeroo.persistence.CrudService;
import cn.hub.jackeroo.system.dao.MenuDao;
import cn.hub.jackeroo.utils.UserUtils;
import cn.hub.jackeroo.vo.ZTree;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单信息Service
 * @author
 * @version 2018-11-14
 */
@Service
public class MenuService {
	/*@Resource
	private MenuDao dao;
	@Autowired
	private RedisTemplate redisTemplate;

	private static final String[] en_number = {"second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth"};

	public List<Menu> getAllMenuByRoleId(Long roleId){
		return dao.getAllMenuByRoleId(roleId);
	}

	*//**
	 * 获取所有菜单--以树状显示
	 * @param pid
	 * @return
	 * @author
	 * @date 2016年3月10日
	 *//*
	public List<Menu> getAllMenuTree(int pid, Long roleId){
		Menu m = new Menu();
		m.setRoleId(roleId);
		m.setParentId(pid);
		m.setIsShow("1");
		List<Menu> menus = dao.findByRoleId(m);
		if(CollectionUtils.isNotEmpty(menus)){
			for(Menu menu : menus){
				menu.setChilds(getAllMenuTree(menu.getId(), roleId));
			}
			return menus;
		}else{
			return null;
		}
	}

	*//**
	 * 获取菜单的html代码
	 * @return
	 * @author
	 * @date 2016年3月10日
	 *//*
	public String getMenuHtml(String adminPath){
		List<Menu> menus = this.getUserMenuTree();

		//List<Menu> menus = getAllMenuTree(1, 1L);

		StringBuffer html = new StringBuffer();
		if(CollectionUtils.isNotEmpty(menus)){
			for(int i = 0; i < menus.size(); i++){
				//isShow == "1"表示为菜单
				if(menus.get(i).getIsShow().equals("1")){
					html.append("<li>");
					html.append("<a href=\"#\">");
					if(StringUtils.isNotEmpty(menus.get(i).getIcon())){
						html.append("<i class=\"" + menus.get(i).getIcon() + "\"></i>");
					}
					html.append("<span class=\"nav-label\">" + menus.get(i).getName() + "</span>");
					if(CollectionUtils.isNotEmpty(menus.get(i).getChilds())){
						html.append("<span class=\"fa arrow\"></span>");
					}
					html.append("</a>");
					if(CollectionUtils.isNotEmpty(menus.get(i).getChilds())){
						loadChild(menus.get(i).getChilds(), html, adminPath);
					}
					html.append("</li>");
				}
			}
		}
		return html.toString();
	}

	public StringBuffer loadChild(List<Menu> menus, StringBuffer html, String adminPath){
		if(CollectionUtils.isNotEmpty(menus)){
			html.append("<ul class=\"nav nav-" + en_number[menus.get(0).getParentIds().split(",").length - 3] + "-level collapse\">");
			for(Menu menu : menus){
				//isShow == "1"表示为菜单
				if(menu.getIsShow().equals("1")){
					html.append("<li>");
					//存在子目录
					if(CollectionUtils.isNotEmpty(menu.getChilds())){
						html.append("<a href=\"#\">" + menu.getName() + "<span class=\"fa arrow\"></span></a>");
						loadChild(menu.getChilds(), html, adminPath);
					}else{
						//不存在子目录
						if(StringUtils.isEmpty(menu.getTarget())){
							html.append("<a class=\"J_menuItem\" href=\"" + adminPath + menu.getHref() + "\">" + menu.getName() + "</a>");
						}else {
							html.append("<a href=\"" + adminPath + menu.getHref() + "\" target=\"" + menu.getTarget() +"\">" + menu.getName() + "</a>");
						}
					}
					html.append("</li>");
				}
			}
			html.append("</ul>");
		}
		return html;
	}


	public void save(Menu menu){
		menu.setParent(this.get(menu.getParentId()));

		// 获取修改前的parentIds，用于更新子节点的parentIds
		//String oldParentIds = menu.getParentIds();

		menu.setParentId(menu.getParent().getId());
		menu.setParentIds(menu.getParent().getParentIds() + menu.getParent().getId() + ",");
		if(menu.getId() == null){
			menu.preInsert();
			dao.insert(menu);
		}else{
			menu.preUpdate();
			dao.update(menu);
		}

		// 更新子节点 parentIds
		*//*Menu m = new Menu();
		m.setParentIds("%,"+menu.getId()+",%");
		List<Menu> list = dao.findByParentIdsLike(m);
		for (Menu e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
			dao.updateParentIds(e);
		}*//*

		//更新缓存
	}

	*//**
	 * 根据pid获取子目录最大排序值
	 * @param pid
	 * @return
	 *//*
	public int getMaxSortByPid(Integer pid){
		return dao.getMaxSortByPid(pid);
	}


	*//**
	 * 获取当前用户的菜单列表
	 *//*
	public List<Menu> getUserMenuTree(){
		Long roleId = UserUtils.getUser().getRoleId();
		List<Menu> list;

		*//*if(redisService.lLen(UserUtils.USER_ROLE_MENULIST + roleId) > 0){
			list =  redisService.lRangeToObj(UserUtils.USER_ROLE_MENULIST + roleId, 0L, -1L);
		}else {
			list = this.getAllMenuTree(1, roleId);
		}*//*
		if(redisTemplate.opsForList().size(UserUtils.USER_ROLE_MENULIST + roleId) > 0){
			list =  redisTemplate.opsForList().range(UserUtils.USER_ROLE_MENULIST + roleId, 0L, -1L);
		}else {
			list = this.getAllMenuTree(1, roleId);

			redisTemplate.opsForList().rightPushAll(UserUtils.USER_ROLE_MENULIST + roleId, list);
		}
		return list;
	}

	*//**
	 * 更新用户的菜单列表
	 * @param roleId
	 *//*
	public void updateUserMenuTree(Long roleId){
		//先清空redis中的菜单列表
		*//*if(redisService.lLen(UserUtils.USER_ROLE_MENULIST + roleId) > 0){
			redisService.lLeftPop(UserUtils.USER_ROLE_MENULIST + roleId);
		}

		//储存
		redisService.lRightPushAll(UserUtils.USER_ROLE_MENULIST + roleId, this.getAllMenuTree(1, roleId));*//*

		if(redisTemplate.opsForList().size(UserUtils.USER_ROLE_MENULIST + roleId) > 0){
			redisTemplate.opsForList().trim(UserUtils.USER_ROLE_MENULIST + roleId, redisTemplate.opsForList().size(UserUtils.USER_ROLE_MENULIST + roleId) , -1);
		}

		//储存
		redisTemplate.opsForList().rightPushAll(UserUtils.USER_ROLE_MENULIST + roleId, this.getAllMenuTree(1, roleId));
	}

	*//**
	 * 根据角色查询菜单列表,查询所有的菜单，标记自己拥有的
	 * @param roleId
	 * @return
	 *//*
	public List<ZTree> findMenuByRole(Long roleId){
		Menu menu = new Menu();
		menu.setRoleId(roleId);
		List<Menu> menus = dao.findMenuByRole(menu);

		List<ZTree> trees = new ArrayList<ZTree>();
		for(Menu m : menus){
			ZTree tree = new ZTree();
			tree.setId(m.getId());
			tree.setpId(m.getParent().getId());
			tree.setName(m.getName());
			if(m.getIsCheck().equals(1)){
				tree.setChecked(true);
			}
			trees.add(tree);
		}
		return trees;
	}*/
}