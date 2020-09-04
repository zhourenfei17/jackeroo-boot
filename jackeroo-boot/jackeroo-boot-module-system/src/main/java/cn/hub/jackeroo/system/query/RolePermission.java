package cn.hub.jackeroo.system.query;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author alex
 * @date 2020/09/04
 */
@Data
public class RolePermission {
    @NotBlank
    private String roleId;
    @NotNull
    private List<String> permissionList;
}
