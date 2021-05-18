package org.hswebframework.utils.file;

import java.io.File;

/**
 * 自动获取文件的编码
 */
public class EncodingDetect {
    static BytesEncodingDetect s = new BytesEncodingDetect();

    /**
     * 得到文件的编码
     *
     * @param filePath 文件路径
     * @return 文件的编码
     */
    public static String getJavaEncode(String filePath) {

        return BytesEncodingDetect.javaname[s.detectEncoding(new File(filePath))];
    }

}

