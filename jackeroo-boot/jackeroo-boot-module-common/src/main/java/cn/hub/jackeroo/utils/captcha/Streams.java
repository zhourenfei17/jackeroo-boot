package cn.hub.jackeroo.utils.captcha;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author alex
 * @date 2020/11/10
 */
public class Streams {
    /**
     * 关闭输入流
     *
     * @param in 输入流
     */
    public static void close(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException ioex) {
                // ignore
            }
        }
    }

    /**
     * 关闭输出流
     *
     * @param out 输出流
     */
    public static void close(OutputStream out) {
        if (out != null) {
            try {
                out.flush();
            } catch (IOException ioex) {
                // ignore
            }
            try {
                out.close();
            } catch (IOException ioex) {
                // ignore
            }
        }
    }
}
