package org.hswebframework.utils.file.callback;

/**
 * Created by 浩 on 2015-12-09 0009.
 */
public abstract class AbstractScanCallBack implements ScanCallBack {

    private boolean exit = false;

    @Override
    public void exit() {
        exit=true;
    }

    @Override
    public boolean isExit() {
        return exit;
    }

}
