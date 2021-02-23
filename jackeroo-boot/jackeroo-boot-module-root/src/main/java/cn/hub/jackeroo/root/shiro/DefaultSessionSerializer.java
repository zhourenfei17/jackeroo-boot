package cn.hub.jackeroo.root.shiro;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.apache.shiro.session.mgt.SimpleSession;

import java.lang.reflect.Type;

/**
 * @author alex
 * @date 2020/08/10
 */
public class DefaultSessionSerializer implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        if(type == SimpleSession.class){
            JSONObject jsonObject = parser.parseObject();
            SimpleSession simpleSession = new SimpleSession();
            return (T)simpleSession;
        }
        return null;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
