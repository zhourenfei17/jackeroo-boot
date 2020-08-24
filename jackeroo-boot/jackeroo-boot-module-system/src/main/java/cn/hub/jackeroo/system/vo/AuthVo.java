package cn.hub.jackeroo.system.vo;

import lombok.Data;

import java.util.Objects;

/**
 * @author alex
 * @date 2020/08/24
 */
@Data
public class AuthVo {
    private String value;
    private String label;

    public AuthVo() {

    }

    public AuthVo(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthVo authVo = (AuthVo) o;
        return Objects.equals(value, authVo.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
