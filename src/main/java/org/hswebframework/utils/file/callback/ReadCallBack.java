package org.hswebframework.utils.file.callback;

/**
 * Created by 浩 on 2015-12-09 0009.
 */
public interface ReadCallBack extends CanExitCallBack {

    void readLine(int lineNumber, String line);

    default void error(Throwable e) {
        e.printStackTrace();
    }

    default void done(int total) {
    }

}
