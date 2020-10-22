package cn.hub.jackeroo.root.api;

import cn.hub.jackeroo.api.ISystemApi;
import cn.hub.jackeroo.system.entity.SysModule;
import cn.hub.jackeroo.system.service.SysDictService;
import cn.hub.jackeroo.system.service.SysModuleService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统模块提供给其他模块的接口 实现类
 * @author alex
 * @date 2020/10/10
 */
@Service
public class SystemApiImpl implements ISystemApi {

    @Autowired
    private SysModuleService moduleService;
    @Autowired
    private SysDictService dictService;

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
        return dictService.findDictItemByDictCode(dictCode).stream().map(item -> (JSONObject)JSONObject.toJSON(item)).collect(Collectors.toList());
    }
}
