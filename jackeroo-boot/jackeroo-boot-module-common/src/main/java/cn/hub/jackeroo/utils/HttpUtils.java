package cn.hub.jackeroo.utils;

import javax.servlet.ServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @author alex
 * @date 2020/09/02
 */
public class HttpUtils {

    public static String getBodyString(ServletRequest request){
        StringBuilder sb = new StringBuilder();

        try(
            InputStream inputStream = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")))
        ){
            String line;
            while ((line = reader.readLine()) != null){
                sb.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}
