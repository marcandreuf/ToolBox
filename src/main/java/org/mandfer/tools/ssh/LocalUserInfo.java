package org.mandfer.tools.ssh;

import com.jcraft.jsch.UserInfo;

/**
 * @author marcandreuf on 24/10/2014.
 */
public class LocalUserInfo implements UserInfo {
    private String passwd;

    public String getPassword() {
        return passwd;
    }

    public boolean promptYesNo(String str) {
        return true;
    }

    public String getPassphrase() {
        return null;
    }

    public boolean promptPassphrase(String message) {
        return true;
    }

    public boolean promptPassword(String message) {
        return true;
    }

    public void showMessage(String message) {
    }
}
