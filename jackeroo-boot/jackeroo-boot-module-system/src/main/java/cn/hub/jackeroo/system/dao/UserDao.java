package cn.hub.jackeroo.system.dao;


import cn.hub.jackeroo.system.entity.User;
import cn.hub.jackeroo.persistence.CrudDao;
import cn.hub.jackeroo.vo.IUser;

/**
 * 系统用户信息DAO接口
 * @author
 * @version 2018-11-14
 */
public interface UserDao extends CrudDao<IUser, Long> {
    /**
     * 通过登录名获取帐号信息
     * @param name
     * @return
     */
    User getByName(String name);
}