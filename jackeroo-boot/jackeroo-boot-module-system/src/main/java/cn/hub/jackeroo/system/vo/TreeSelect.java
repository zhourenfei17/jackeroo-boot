package cn.hub.jackeroo.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 前端a-tree-select组件treeData所需要的数据结构模型
 * @author alex
 * @date 2020/07/31
 */
@Data
public class TreeSelect {
    private String key;
    private String value;
    private String title;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;
}
