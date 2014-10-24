package org.mandfer.tools.ssh;


import com.google.inject.Inject;
import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.StringTokenizer;


/**
 * @author marcandreuf on 24/10/2014.
 */
//TODO: Clean up and use guice to inject dependencies.
public class SchCommandJcraft implements SshCmd {

    private JSch jsh;
    private UserInfo userInfo;
    private int SCP_PORT = 22;
    private boolean scpped;
    private int retryLimit;
    private Session session;
    private Channel channel;


    private Logger logger = LoggerFactory.getLogger(SchCommandJcraft.class);

    @Inject
    public SchCommandJcraft(JSch jsh, UserInfo userInfo) {
        this.jsh = jsh;
        this.userInfo = userInfo;
        this.retryLimit = 3;
        this.scpped = true;
    }


    @Override
    public void initLocalSideForwardedSession(String sessionHost, String sessionUser, URL privateKeyFileName,
                                              String passPhrase, String forwardingHost, int localPort,
                                              int forwardingPort) throws SshCmdException {
        try {
            initSession(sessionHost, sessionUser, privateKeyFileName, passPhrase);
            createLocalTunnel(forwardingHost, localPort, forwardingPort);
        } catch (Exception e) {
            throw new SshCmdException(e);
        }
    }

    private void createLocalTunnel(String forwardingHost, int localPort, int forwardingPort) throws JSchException {
        session.setPortForwardingL(localPort, forwardingHost, forwardingPort);
        for (String port : session.getPortForwardingL()) {
            logger.debug("Forwarding port config: " + port);
        }
    }


    @Override
    public void initLocalSideForwardedSession(String sessionHost, String sessionUser, String password,
                                              String forwardingHost, int localPort, int forwardingPort)
            throws SshCmdException {
        try {
            initSession(sessionHost, sessionUser, password);
            createLocalTunnel(forwardingHost, localPort, forwardingPort);
        } catch (Exception e) {
            throw new SshCmdException(e);
        }
    }


    @Override
    public void initLocalSideForwardedSession(String sessionHost, String sessionUser, InputStream privateKeyInputStream,
                                              String passPhrase, String forwardingHost, int localPort,
                                              int forwardingPort) throws SshCmdException {
        try {
            initSession(sessionHost, sessionUser, privateKeyInputStream, passPhrase);
            createLocalTunnel(forwardingHost, localPort, forwardingPort);
        } catch (Exception e) {
            throw new SshCmdException(e);
        }
    }

    @Override
    public void initSession(String host, String user, String password) throws SshCmdException {
        try {
            createSession(host, user);
            setPasswordToSession(password);
            setUserInfoToSession();
            connect();
        } catch (Exception e) {
            throw new SshCmdException(e);
        }
    }


    @Override
    public void initSession(String host, String user, InputStream privateKeyInputStream, String passPhrase)
            throws SshCmdException {
        try {
            setPrivateKey(user, privateKeyInputStream, passPhrase);
            createSession(host, user);
            setUserInfoToSession();
            connect();
        } catch (Exception e) {
            throw new SshCmdException(e);
        }
    }

    private void setPrivateKey(String user, InputStream privateKeyInputStream, String passPhrase) throws IOException, JSchException {
        final byte[] publicKey = null;
        final byte[] privateKey = IOUtils.toByteArray(privateKeyInputStream); // Private key must be byte array
        byte[] bPassPhrase = null;
        if (passPhrase != null && !passPhrase.equals("")) {
            bPassPhrase = passPhrase.getBytes("UTF-8");
        }
        logger.debug("Add private key through input stream.");
        jsh.addIdentity(
                user,
                privateKey,
                publicKey,
                bPassPhrase
        );
    }

    @Override
    public void initSession(String host, String user, URL privateKeyFileName, String passPhrase)
            throws SshCmdException {
        try {
            setPrivateKey(privateKeyFileName, passPhrase);
            createSession(host, user);
            setUserInfoToSession();
            connect();
        } catch (Exception e) {
            throw new SshCmdException(e);
        }
    }

    private void createSession(String host, String user) throws JSchException {
        logger.debug("Create jsh session to " + host + ":" + SCP_PORT + " with user " + user);
        session = jsh.getSession(user, host, SCP_PORT);
    }

    private void setPasswordToSession(String password) {
        session.setPassword(password);
    }

    private void connect() throws JSchException {
        logger.debug("Connecting to " + session.getHost());
        session.connect();
    }

    private void setUserInfoToSession() {
        session.setUserInfo(userInfo);
    }

    private void setPrivateKey(URL privateKeyFileName, String passPhrase) throws IOException, JSchException {
        logger.debug("Add private key file: " + privateKeyFileName.getPath());
        jsh.addIdentity(privateKeyFileName.getPath(), passPhrase);
    }


    @Override
    public void disconnectSession() throws SshCmdException {
        try {
            if (channel != null) {
                logger.debug("Disconnect channel " + channel.getId());
                channel.disconnect();
            }

            if (session != null) {
                logger.debug("Disconnect session " + session.getHost());
                session.disconnect();
            }
        } catch (Exception e) {
            throw new SshCmdException(e);
        }
    }


    @Override
    public CommandResponse exec(String cmd) throws SshCmdException {
        CommandResponse commandResponse = new CommandResponse();
        try {
            logger.debug("Exec: " + cmd);
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(cmd);
            channel.setInputStream(commandResponse.getIn());
            channel.setOutputStream(commandResponse.getOut());
            ((ChannelExec) channel).setErrStream(commandResponse.getErr());
            //InputStream in = channel.getInputStream();
            channel.connect();
            //Wait until command is finished.
            while (!channel.isClosed()) {
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                    throw ee;
                }
            }
            commandResponse.setExitStatus(channel.getExitStatus());
            channel.disconnect();
        } catch (Exception e) {
            throw new SshCmdException(e);
        }
        return commandResponse;
    }

    @Override
    public boolean isConnected() {
        return session != null && session.isConnected();
    }

    @Override
    public CommandResponse scpTo(String local_file_name, String remote_file_name) throws SshCmdException {
        CommandResponse response = initDefaultCommandResponse();

        int retry_count;
        for (retry_count = 1; retry_count <= getRetryLimit(); retry_count++) {
            try {
                response = tryScpTo(local_file_name, remote_file_name);
                break;
            } catch (Exception e) {
                if (exceedMaxRetry(retry_count)) {
                    throw new SshCmdException(e.getMessage(), e);
                } else {
                    logger.info("scp to fail " + e.getMessage() + ". Trying again for " + retry_count + " times ....");
                }
            }
        }
        if (exceedMaxRetry(retry_count)) {
            throw new SshCmdException("ScpTo failed after all retries. ");
        }
        return response;
    }

    private boolean exceedMaxRetry(int retry) {
        return retry > getRetryLimit();
    }

    private CommandResponse initDefaultCommandResponse() {
        CommandResponse response = new CommandResponse();
        response.setExitStatus(1);
        return response;
    }

    private CommandResponse tryScpTo(String local_file_name, String remote_file_name) throws SshCmdException {
        CommandResponse response = initDefaultCommandResponse();

        try {
            setScpCommand(remote_file_name);
            OutputStream channel_send_data = channel.getOutputStream();
            InputStream channel_response_data = channel.getInputStream();
            ((ChannelExec) channel).setErrStream(response.getErr());
            channel.connect();

            checkScpOpenConnection(channel_response_data);
            sendFileHeader(local_file_name, channel_send_data, channel_response_data);
            sendFileContent(local_file_name, response, channel_send_data, channel_response_data);

            channel.disconnect();
        } catch (Exception e) {
            throw new SshCmdException(e);
        }

        return response;
    }

    private void setScpCommand(String remote_file_name) throws JSchException {
        String command = "scp -p -t " + remote_file_name;
        channel = session.openChannel("exec");
        logger.debug("command: " + command);
        ((ChannelExec) channel).setCommand(command);
    }

    private void checkScpOpenConnection(InputStream channel_response_data) throws IOException, SshCmdException {
        if (checkCommunicationFeedback(channel_response_data) != 0) {
            throw new SshCmdException("SCP Open Error");
        }
    }

    private void sendFileHeader(String local_file_name, OutputStream channel_send_data, InputStream channel_response_data) throws IOException, SshCmdException {
        // send "C0644 file_size filename", where filename should not include'/'
        long file_size = (new File(local_file_name)).length();
        String command_header = "C0644 " + file_size + " ";
        if (local_file_name.lastIndexOf('/') > 0) {
            command_header += local_file_name.substring(local_file_name.lastIndexOf('/') + 1);
        } else {
            command_header += local_file_name;
        }
        command_header += "\n";
        channel_send_data.write(command_header.getBytes());
        channel_send_data.flush();
        if (checkCommunicationFeedback(channel_response_data) != 0) {
            throw new SshCmdException("SCP Write Error");
        }
    }

    private void sendFileContent(String local_file_name, CommandResponse response, OutputStream channel_send_data, InputStream channel_response_data) throws IOException, SshCmdException {
        // send a content of lfile
        FileInputStream fis = new FileInputStream(local_file_name);
        byte[] buf = new byte[1024];
        while (true) {
            int len = fis.read(buf, 0, buf.length);
            if (len <= 0)
                break;
            channel_send_data.write(buf, 0, len); // channel_send_data.flush();
        }
        fis.close();
        fis = null;
        // send '\0'
        buf[0] = 0;
        channel_send_data.write(buf, 0, 1);
        channel_send_data.flush();

        int communication_feedbak = checkCommunicationFeedback(channel_response_data);
        if (communication_feedbak != 0) {
            throw new SshCmdException("SCP Write Error");
        } else {
            response.setExitStatus(communication_feedbak);
        }
        channel_send_data.close();
    }

    private int checkCommunicationFeedback(InputStream in) throws IOException {
        int b = in.read(); // 0 for success, 1 for error, 2 for fatal error, -1
        // Bugger
        if (b == 0)
            return b;
        if (b == -1)
            return b;
        if (b == 32)
            return 0;
        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            } while (c != '\n');
            logger.error("ssh reported error: " + sb.toString());
        }
        return b;
    }


    @Override
    public String getRemoteProperty(String remoteFileName, String propertyName) throws SshCmdException {
        String value = "";
        try {
            String command = "grep " + propertyName + " " + remoteFileName;
            CommandResponse response = exec(command);
            String propertyLine = response.getOutput();
            int length_equals_signal = 0;
            int offset_inclusive = 1;
            if (!propertyLine.equals("")) {
                value = propertyLine.substring(propertyName.length() + length_equals_signal + offset_inclusive, propertyLine.length());
            }
        } catch (Exception e) {
            throw new SshCmdException(e.getMessage(), e);
        }
        return value;
    }

    @Override
    public CommandResponse setRemoteProperty(String remoteFileName, String propertyName, String push_value, String propValue) throws SshCmdException {
        CommandResponse response = initDefaultCommandResponse();

        try {
            push_value = push_value.replaceAll("\\r\\n|\\r|\\n|", " ").replaceAll(" ", "").replaceAll("\\t", "");
            propValue = propValue.replaceAll("\\r\\n|\\r|\\n|", " ").replaceAll(" ", "").replaceAll("\\t", "");

            String command = "sed -e 's/" + propertyName + "=" + propValue + "/" + propertyName + "=" + push_value + "/g' " + remoteFileName + " > ~/props";
            logger.debug("Command: " + command);
            response = exec(command);
            logger.debug("Response out: " + response.getOutput() + ". Error: " + response.getError());

            command = "cp -f ~/props " + remoteFileName;
            logger.debug("Command: " + command);
            response = exec(command);
            logger.debug("Response out: " + response.getOutput() + ". Error: " + response.getError());

        } catch (Exception e) {
            throw new SshCmdException(e.getMessage(), e);
        }

        return response;
    }


    /**
     * Set number of scp retries to perform.
     *
     * @param retries
     */
    public void setRetryLimit(int retries) {
        retryLimit = retries;
    }

    /**
     * Return retry limit
     *
     * @return retry limit
     */
    public int getRetryLimit() {
        return retryLimit;
    }

    public void scpItemFrom(String localFilename, String remoteFilename) throws SshCmdException {
        FileOutputStream fos = null;
        scpped = true;
        try {
            String command = "scp -f " + remoteFilename;
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();
            channel.connect();

            byte[] buf = new byte[1024];

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
            while (true) {
                int a = 0; // checkCommunicationFeedback(in);
                if (a != 0) {
                    throw new SshCmdException("SCP Write Error");
                    // break;
                }
                // read '0644 '
                in.read(buf, 0, 5);

                a = checkCommunicationFeedback(in);
                if (a != 0) {
                    throw new SshCmdException("SCP Read Error");
                    // break;
                }

                long filesize = 0L;
                while (true) {
                    if (in.read(buf, 0, 1) < 0) {
                        // error
                        break;
                    }
                    if (buf[0] == ' ')
                        break;
                    filesize = filesize * 10L + (long) (buf[0] - '0');
                }

                String file = null;
                for (int i = 0; ; i++) {
                    in.read(buf, i, 1);
                    if (buf[i] == (byte) 0x0a) {
                        file = new String(buf, 0, i);
                        break;
                    }
                }

                // send '\0'
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();

                // read a content of localFilename
                fos = new FileOutputStream(localFilename);
                int foo;
                while (true) {
                    if (buf.length < filesize)
                        foo = buf.length;
                    else
                        foo = (int) filesize;
                    foo = in.read(buf, 0, foo);
                    if (foo < 0) {
                        scpped = false;
                        break;
                    }
                    fos.write(buf, 0, foo);
                    filesize -= foo;
                    if (filesize == 0L)
                        break;
                }
                fos.close();
                fos = null;

                if (checkCommunicationFeedback(in) != 0) {
                    scpped = false;
                    break;
                }
                break;

            }
        } catch (Exception e) {
            throw new SshCmdException(e);
        }
    }

    public void scpFrom(String localFilename, String remoteFilename) throws SshCmdException {
        Exception exceptionLog = null;
        for (int retry = 0; retry < getRetryLimit(); retry++) {
            try {
                scpItemFrom(localFilename, remoteFilename);
            } catch (Exception e) {
                exceptionLog = e;
            } finally {
                if (exceptionLog == null) {
                    break;
                } else {
                    if (retry < getRetryLimit()) { // retry scp
                        exceptionLog = null;
                    } else {
                        throw new SshCmdException(exceptionLog);
                    }
                }
            }
        }
    }


    public File relativePathToAbsolute(String relativePath) throws IOException {
        File absolutePath = new File(System.getProperty("user.dir"));
        StringTokenizer pathComponents = new StringTokenizer(relativePath, "/");

        while (pathComponents.hasMoreTokens()) {
            String pathComponent = pathComponents.nextToken();

            if (pathComponent.startsWith(".")) {
                throw new IllegalArgumentException("path component begins with a dot: " + relativePath);
            }

            absolutePath = new File(absolutePath, pathComponent);
        }

        return absolutePath;
    }

}
