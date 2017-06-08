package org.hswebframework.utils.file.callback;

/**
 * Created by 浩 on 2015-12-09 0009.
 */
public interface CanExitCallBack {

    default void exit() {
    }

    default boolean isExit() {
        return false;
    }
}
