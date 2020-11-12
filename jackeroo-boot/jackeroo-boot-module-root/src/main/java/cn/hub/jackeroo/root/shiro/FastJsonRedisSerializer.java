package cn.hub.jackeroo.root.shiro;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.TypeUtils;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.session.mgt.SimpleSession;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * @author alex
 * @date 2020/05/31
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer<T> {
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private final static ParserConfig defaultRedisConfig = new ParserConfig();
    static {
        JSON.DEFAULT_GENERATE_FEATURE = SerializerFeature.config(
                JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.SkipTransientField, false
        );
        // 自定义oauth2序列化，
        defaultRedisConfig.setAutoTypeSupport(true);
        defaultRedisConfig.putDeserializer(SimpleSession.class, new DefaultSessionSerializer());
        defaultRedisConfig.addAccept("org.apache.shiro.session.mgt");
        TypeUtils.addMapping("org.apache.shiro.session.mgt.SimpleSession", SimpleSession.class);

        defaultRedisConfig.addAccept("org.apache.shiro.authc");
        TypeUtils.addMapping("org.apache.shiro.authc.SimpleAuthenticationInfo", SimpleAuthenticationInfo.class);
    }

    protected Class<T> clazz;

    public FastJsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (null == t) {
            return new byte[0];
        }
       /* SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.config(SerializerFeature.SkipTransientField, false);
        serializer.write(t);
        return out.toBytes("utf-8");*/
        return JSON.toJSONBytes(t, SerializerFeature.WriteClassName,
                SerializerFeature.DisableCircularReferenceDetect);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (null == bytes || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);
        return (T) JSON.parseObject(new String(bytes, IOUtils.UTF8), clazz, defaultRedisConfig);
    }
}
