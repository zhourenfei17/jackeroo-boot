package cn.hub.jackeroo.root.api;

import cn.hub.jackeroo.api.ISystemApi;
import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.constant.RedisKeyPrefix;
import cn.hub.jackeroo.root.shiro.ShiroRealm;
import cn.hub.jackeroo.service.RedisService;
import cn.hub.jackeroo.system.entity.SysModule;
import cn.hub.jackeroo.system.entity.SysUser;
import cn.hub.jackeroo.system.service.SysDictService;
import cn.hub.jackeroo.system.service.SysModuleService;
import cn.hub.jackeroo.system.service.SysUserService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统模块提供给其他模块的接口 实现类
 * @author alex
 * @date 2020/10/10
 */
@Service("systemApi")
public class SystemApiImpl implements ISystemApi {

    @Autowired
    private SysModuleService moduleService;
    @Autowired
    private SysDictService dictService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SysUserService userService;

    /**
     * 通过id获取模块信息
     * @param moduleId
     * @return
     */
    @Override
    public JSONObject getModuleById(Long moduleId){
        SysModule module = moduleService.getById(moduleId);
        if(module == null){
            return null;
        }
        return (JSONObject)JSONObject.toJSON(module);
    }

    /**
     * 通过字典code获取字典项信息
     * @param dictCode
     * @return
     */
    public List<JSONObject> getDictItemByCode(String dictCode){
        List list = dictService.findDictItemByDictCode(dictCode);
        return JSONArray.parseArray(JSONArray.toJSONString(list), JSONObject.class);
    }

    /**
     * 清除当前用户缓存
     */
    public void clearAuthorizationCache(){
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager)SecurityUtils.getSecurityManager();
        ShiroRealm realm = (ShiroRealm)securityManager.getRealms().iterator().next();
        realm.clearAllCachedAuthorizationInfo();
    }

    /**
     * 踢用户下线。直接删除redis的session和shiro缓存
     * @param userId
     */
    public void kickOutUser(Serializable userId){
        SysUser user = userService.findById(userId);
        if(user != null){
            List<String> sessionList = redisService.getList(RedisKeyPrefix.SAME_USER_SESSION_LIST + user.getAccount(), String.class);
            if(CollectionUtils.isNotEmpty(sessionList)){
                List<String> deleteKeys = new ArrayList<>();
                for (String sessionId : sessionList) {
                    // 用户的session信息
                    deleteKeys.add(RedisKeyPrefix.USER_SESSION + sessionId);
                }
                // 用户的身份认证信息
                deleteKeys.add(RedisKeyPrefix.USER_CACHE + RedisKeyPrefix.AUTHENTICATION_NAME + Constant.SPLIT_SECURITY + user.getAccount());
                // 用户的授权信息
                deleteKeys.add(RedisKeyPrefix.USER_CACHE + RedisKeyPrefix.AUTHORIZATION_NAME + Constant.SPLIT_SECURITY + user.getAccount());

                redisService.deleteKey(deleteKeys);
            }
        }
    }
}
