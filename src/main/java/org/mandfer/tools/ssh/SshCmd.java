package org.mandfer.tools.ssh;

import java.io.InputStream;
import java.net.URL;

/**
 * Ssh commands definition.
 *
 * @author marcandreuf on 24/10/2014.
 */
public interface SshCmd {


    public void initLocalSideForwardedSession(String sessionHost, String sessionUser, String password,
                                              String forwardingHost, int localPort, int forwardingPort)
            throws SshCmdException;


    public void initLocalSideForwardedSession(String sessionHost, String sessionUser, URL privateKeyFileName,
                                              String passPhrase, String forwardingHost, int localPort, int forwardingPort)
            throws SshCmdException;

    public void initLocalSideForwardedSession(String sessionHost, String sessionUser, InputStream privateKeyInputStream,
                                              String passPhrase, String forwardingHost, int localPort, int forwardingPort)
            throws SshCmdException;


    public void initSession(String host, String user, String password) throws SshCmdException;

    public void initSession(String host, String user, InputStream privateKeyInputStream, String passPhrase) throws SshCmdException;

    public void initSession(String host, String user, URL privateKeyFileName, String passPhrase) throws SshCmdException;

    public void disconnectSession() throws SshCmdException;

    public CommandResponse exec(String cmd) throws SshCmdException;

    public boolean isConnected();

    public CommandResponse scpTo(String local_file_name, String remote_file_name) throws SshCmdException;

    public String getRemoteProperty(String remoteFileName, String propertyName) throws SshCmdException;

    public CommandResponse setRemoteProperty(String remoteFileName, String propertyName, String push_value, String propValue) throws SshCmdException;

}
