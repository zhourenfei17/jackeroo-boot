package cn.hub.jackeroo.utils;

import cn.hub.jackeroo.enums.ResultStatusCode;
import cn.hub.jackeroo.vo.Result;
import com.alibaba.fastjson.JSON;
import org.springframework.http.MediaType;

import javax.servlet.ServletResponse;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * @author alex
 * @date 2020/12/11
 */
public class ResultUtil {
    /**
     * 输出json数据给前端
     * @param response
     * @param obj
     */
    public static void writeJson(ServletResponse response, Object obj){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            Writer out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
            out.write(JSON.toJSONString(new Result<>(ResultStatusCode.KICK_OUT_ERROR)));
            out.flush();
            out.close();
        }
        catch (Exception e) {
            System.err.println("KickoutSessionFilter.class 输出JSON异常，可以忽略。");
        }
    }
}
