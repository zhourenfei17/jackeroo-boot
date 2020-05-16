package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.system.dao.UserDao;
import cn.hub.jackeroo.system.entity.User;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 系统用户信息Service
 * @author
 * @version 2018-11-14
 */
@Service
public class UserService extends ServiceImpl<UserDao, User> {
	/*@Resource
	private UserDao dao;

	public User getUserByName(String name){
		return dao.getByName(name);
	}

	public void save(User user){
		if(user.getId() == null){
			//新增
			user.setHeadImg("https://webappcommon.oss-cn-beijing.aliyuncs.com/common%2FdefultAvatar.png");
			user.setPassword(MD5Util.encrypt("666666"));
			user.setCreateTime(new Date());
			user.setUpdateTime(new Date());
			dao.insert(user);

			//添加角色

			//添加区域

		}else{
			//更新
		}
	}*/

}