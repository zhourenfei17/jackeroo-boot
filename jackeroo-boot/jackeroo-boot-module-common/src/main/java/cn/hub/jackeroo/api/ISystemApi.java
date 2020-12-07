package cn.hub.jackeroo.api;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 系统模块提供给其他模块的接口
 * 用于夸模块接口调用
 * @author alex
 * @date 2020/10/10
 */
public interface ISystemApi {
    /**
     * 通过id获取模块信息
     * @param moduleId
     * @return
     */
    JSONObject getModuleById(Long moduleId);

    /**
     * 通过字典code获取字典项信息
     * @param dictCode
     * @return
     */
    List<JSONObject> getDictItemByCode(String dictCode);

    /**
     * 清除当前用户权限缓存
     */
    void clearAuthorizationCache();
}
