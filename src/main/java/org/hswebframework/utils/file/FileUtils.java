package org.hswebframework.utils.file;


import org.hswebframework.utils.StringUtils;
import org.hswebframework.utils.file.callback.ReadCallBack;
import org.hswebframework.utils.file.callback.ReadStringCallBack;
import org.hswebframework.utils.file.callback.ScanCallBack;

import java.io.*;

/**
 * Created by 浩 on 2015-12-09 0009.
 */
public class FileUtils extends Resources {

    /**
     * 指定目录,扫描文件.可指定最大扫描深度
     *
     * @param path     要扫描的目录
     * @param maxDeep  最大扫描深度
     * @param callBack 扫描回掉
     */
    public static void scanFile(String path, int maxDeep, ScanCallBack callBack) {
        scanFile(path, true, maxDeep, 0, callBack);
    }

    /**
     * 指定目录,扫描文件.可指定是否递归往下扫描
     *
     * @param path     指定要扫描的目录
     * @param depth    是否递归往下
     * @param callBack 扫描回掉
     */
    public static void scanFile(String path, boolean depth, ScanCallBack callBack) {
        scanFile(path, depth, -1, 0, callBack);
    }

    /**
     * 指定目录,扫描文件.只扫描指定一层
     *
     * @param path     指定要扫描的目录
     * @param callBack 扫描回掉
     */
    public static void scanFile(String path, ScanCallBack callBack) {
        scanFile(path, false, callBack);
    }

    /**
     * 深度优先,扫描文件
     *
     * @param path     扫描目录
     * @param depth    是否往下递归
     * @param maxDeep  扫描最大深度
     * @param deep     当前深度
     * @param callBack 扫描回掉
     */
    private static void scanFile(String path, boolean depth, int maxDeep, int deep, ScanCallBack callBack) {
        if ((maxDeep != -1 && deep > maxDeep) || callBack.isExit()) {
            return;
        }
        File file = new File(path);
        try {
            if (file.isFile()) {
                callBack.accept(deep, file);
            } else {
                deep++;
                callBack.accept(deep, file);
                File[] files = file.listFiles();
                for (File file2 : files) {
                    if (callBack.isExit()) return;
                    // 递归
                    if (depth)
                        scanFile(file2.getAbsolutePath(), depth, maxDeep, deep, callBack);
                        // 调用回调
                    else if (file2.isFile())
                        callBack.accept(deep, file2);
                    else if (file2.isDirectory())
                        callBack.accept(deep, file2);
                }
            }
        } catch (Throwable e) {
            callBack.error(deep, file, e);
        }
    }

    /**
     * 获取文件编码,如果获取失败,使用默认编码utf8
     *
     * @param fileName 文件名
     * @return 文件编码
     */
    public static final String getFileEncode(String fileName) {
        String encode = EncodingDetect.getJavaEncode(fileName);
        if (StringUtils.isNullOrEmpty(encode)) {
            encode = "utf8";
        }
        return encode;
    }

    /**
     * 回掉方式按行读取文件
     *
     * @param callBack 读取回掉
     * @throws Exception 读取异常
     */
    public static final void readFile(Reader reader, ReadCallBack callBack) throws IOException {
        // 指定文件编码读取
        BufferedReader bufferedReader = new BufferedReader(reader);
        int number = 0;
        try {
            while (bufferedReader.ready()) {
                if (callBack.isExit()) break;
                // 读取一行 回掉
                callBack.readLine(number++, bufferedReader.readLine());
            }
        } catch (Throwable e) {
            callBack.error(e);
        } finally {
            bufferedReader.close();
        }
        callBack.done(number);
    }

    /**
     * 读取文件为字符串,将自动获取文件编码
     *
     * @return 读取结果
     * @throws Exception 读取异常
     */
    public static final String reader2String(String fileName) throws IOException {
        ReadStringCallBack callBack = new ReadStringCallBack();
        Reader reader;
        try {
            reader = getResourceAsReader(fileName);
        } catch (Exception e) {
            reader = new InputStreamReader(new FileInputStream(fileName));
        }
        readFile(reader, callBack);
        return callBack.toString();
    }

    public static final void readerFile(String fileName, ReadCallBack callBack) throws Exception {
        readFile(getResourceAsReader(fileName), callBack);
    }


    /**
     * 读取文件为字符串,将自动获取文件编码
     *
     * @return 读取结果
     * @throws Exception 读取异常
     */
    public static final String reader2String(Reader reader) throws Exception {
        ReadStringCallBack callBack = new ReadStringCallBack();
        readFile(reader, callBack);
        return callBack.toString();
    }


    /**
     * 读取文件为对象
     *
     * @param fileName 文件名
     * @param <T>      对象泛型
     * @return 读取结果
     * @throws Exception 读取异常
     */
    public static final <T> T readFile2Obj(String fileName) throws Exception {
        try (InputStream input = new FileInputStream(fileName)) {
            return readStream2Obj(input);
        }
    }

    /**
     * 读取输入流为对象
     *
     * @param inputStream 输入流
     * @param <T>         对象泛型
     * @return 读取结果
     * @throws Exception 读取异常
     */
    public static final <T> T readStream2Obj(InputStream inputStream) throws Exception {
        ObjectInputStream stream = null;
        try {
            stream = new ObjectInputStream(inputStream);
            return (T) stream.readObject();
        } finally {
            if (stream != null)
                stream.close();
        }
    }

    /**
     * 写出对象到文件(序列化)
     *
     * @param obj      需要写出的对象
     * @param fileName 文件名称
     * @throws Exception 写出异常
     */
    public static final void writeObj2File(Serializable obj, String fileName) throws Exception {
        try (OutputStream out = new FileOutputStream(fileName)) {
            writeObj2Steam(obj, out);
        }
    }

    public static final void writeString2File(String str, String fileName, String encode) throws Exception {
        File file = new File(fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (Writer out = new OutputStreamWriter(new FileOutputStream(fileName), encode)) {
            out.write(str);
            out.flush();
        }
    }

    /**
     * 写出对象到输出流
     *
     * @param obj          需要写出的对象
     * @param outputStream 输出流
     * @throws Exception 写出异常
     */
    public static final void writeObj2Steam(Serializable obj, OutputStream outputStream) throws Exception {
        ObjectOutputStream stream = new ObjectOutputStream(outputStream);
        stream.writeObject(obj);
        stream.flush();
    }

    /**
     * 获取文件后缀名
     *
     * @param file 文件对象
     * @return 文件后缀名
     */
    public static String getSuffix(File file) {
        return getSuffix(file.getName());
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName 文件名
     * @return 文件后缀名
     */
    public static String getSuffix(String fileName) {
        if (fileName == null)
            return "";
        if (fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        }
        return "";
    }
}
