package cn.hub.jackeroo.api;

import com.alibaba.fastjson.JSONObject;

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
}
