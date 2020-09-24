package cn.hub.jackeroo.online.param;

import cn.hub.jackeroo.online.entity.OnlineScheme;
import cn.hub.jackeroo.online.entity.OnlineTable;
import cn.hub.jackeroo.online.entity.OnlineTableField;
import lombok.Data;

import java.util.List;

/**
 * @author alex
 * @date 2020/09/24
 */
@Data
public class GenerateTableDetail {

    private OnlineTable onlineTable;
    private OnlineScheme onlineScheme;
    private List<OnlineTableField> onlineTableField;
}
