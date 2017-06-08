package org.hswebframework.utils.file.callback;

/**
 * Created by 浩 on 2015-12-09 0009.
 */
public class ReadStringCallBack implements ReadCallBack {

    protected StringBuilder builder = new StringBuilder();

    @Override
    public String toString() {
        return builder.toString();
    }

    @Override
    public void readLine(int lineNumber, String line) {
        builder.append(line).append("\n");
    }
}
