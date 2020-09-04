package cn.hub.jackeroo.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 前端a-tree组件treeData所需要的数据结构模型
 * @author alex
 * @date 2020/09/03
 */
@Data
public class Tree {
    private Serializable key;
    private String title;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Tree> children;

    public Tree(Serializable key, String title) {
        this.key = key;
        this.title = title;
    }

    public Tree() {
    }
}
