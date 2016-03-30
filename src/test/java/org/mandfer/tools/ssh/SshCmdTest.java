package org.mandfer.tools.ssh;

import org.mandfer.tools.guice.ToolsBoxFactory;

/**
 * @author marcandreuf on 24/10/2014.
 */
public class SshCmdTest {


//    public static final String PRIVATE_KEY_FILE_NAME = "....";
//
//    public static final String HOST_QA00 = ".....";
//    public static final String USER_NAME_QA00 = ".....";
//
//    public static final String HOST_CI01 = "...";
//    public static final String USER_NAME_CI01 = ".....";
//    public static final String PASSWORD_CI01 = "..........";
//
//    public static final String HOST_DEV00 = "............";
//    public static final String USERNAME_DEV00 = "................";
//
//
//    public static final String TEST_FILE_TO_SEND = "............";
//    private Logger logger = LoggerFactory.getLogger(SshTest.class);
//    private static final String EMPTY_PASS_PHRASE=null;
//
//    @Test
//    public void initSessionWithPrivateKeyInputStreamTest() throws Exception {
//        InputStream input = this.getClass().getClassLoader()
//          .getResourceAsStream(PRIVATE_KEY_FILE_NAME);
//        SshCmd sshcmd = ToolsBoxFactory.getInstance(SshCmd.class);
//        sshcmd.initSession(HOST_DEV00, USERNAME_DEV00, input, EMPTY_PASS_PHRASE);
//
//        assertEquals(true, sshcmd.isConnected());
//        sshcmd.disconnectSession();
//        assertEquals(false, sshcmd.isConnected());
//    }
//
//    @Test
//    public void initSessionWithPrivateKeyFileNameTest() throws Exception {
//        SshCmd sshcmd = connectWithPrivateKey();
//
//        assertEquals(true, sshcmd.isConnected());
//        sshcmd.disconnectSession();
//        assertEquals(false, sshcmd.isConnected());
//    }
//
//    private SshCmd connectWithPrivateKey() throws SshCmdException {
//        URL privateKeyFileName = this.getClass().getClassLoader()
//          .getResource(PRIVATE_KEY_FILE_NAME);
//        SshCmd sshcmd = ToolsBoxFactory.getInstance(SshCmd.class);
//        sshcmd.initSession(HOST_DEV00, USERNAME_DEV00, privateKeyFileName, EMPTY_PASS_PHRASE);
//        return sshcmd;
//    }
//
//    @Test
//    public void initSessionWithPrivateKeyUrlAndPassPhraseTest() throws Exception {
//        URL privateKeyFileName = this.getClass().getClassLoader()
//          .getResource("jmaOpen");
//        SshCmd sshcmd = ToolsBoxFactory.getInstance(SshCmd.class);
//        sshcmd.initSession("dev-fbc-int01.development.sample.com",
//          "jma", privateKeyFileName, "2235345");
//
//        assertEquals(true, sshcmd.isConnected());
//        sshcmd.disconnectSession();
//        assertEquals(false, sshcmd.isConnected());
//    }
//
//
//    @Test
//    public void initSessionWithPasswordTest() throws Exception {
//        SshCmd sshcmd = ToolsBoxFactory.getInstance(SshCmd.class);
//        sshcmd.initSession(HOST_CI01, USER_NAME_CI01, PASSWORD_CI01);
//
//        assertEquals(true, sshcmd.isConnected());
//        sshcmd.disconnectSession();
//        assertEquals(false, sshcmd.isConnected());
//    }
//
//
//    @Test
//    public void initForwardedSessionTest() throws Exception {
//        SshCmd sshcmd = ToolsBoxFactory.getInstance(SshCmd.class);
//        URL privateKeyFileName = this.getClass().getClassLoader()
//          .getResource(PRIVATE_KEY_FILE_NAME);
//        String forwardingHost = "sample.host.com";
//        int localPort = 63306;
//        int forwardingPort = 3306;
//        sshcmd.initLocalSideForwardedSession(
//          HOST_DEV00, USERNAME_DEV00, privateKeyFileName, EMPTY_PASS_PHRASE,
//          forwardingHost, localPort, forwardingPort);
//
//        assertEquals(true, sshcmd.isConnected());
//        sshcmd.disconnectSession();
//        assertEquals(false, sshcmd.isConnected());
//    }
//
//
//    @Test
//    public void initForwardedSessionWithInputStreamKeyTest() throws Exception {
//        SshCmd sshcmd = ToolsBoxFactory.getInstance(SshCmd.class);
//        InputStream privateKeyInputStream = this.getClass().getClassLoader()
//          .getResourceAsStream(PRIVATE_KEY_FILE_NAME);
//        String forwardingHost = "sample.host.com";
//        int localPort = 63306;
//        int forwardingPort = 3306;
//
//        sshcmd.initLocalSideForwardedSession(
//          HOST_DEV00, USERNAME_DEV00, privateKeyInputStream, EMPTY_PASS_PHRASE,
//          forwardingHost, localPort, forwardingPort);
//
//        assertEquals(true, sshcmd.isConnected());
//        sshcmd.disconnectSession();
//        assertEquals(false, sshcmd.isConnected());
//    }
//
//    @Test
//    public void run_single_commnad_Test() throws Exception {
//        SshCmd sshcmd = connectWithPrivateKey();
//        CommandResponse consle = sshcmd.exec("ls -la");
//        sshcmd.disconnectSession();
//
//        logger.debug(consle.toString());
//        logger.debug("output: " + consle.getOutput());
//        assertEquals(true, consle.getExitStatus() == 0);
//        assertEquals(false, consle.getOutput().equals(""));
//    }
//
//    @Test
//    public void cd_to_wrong_folder_Test() throws Exception {
//        SshCmd sshcmd = connectWithPrivateKey();
//        CommandResponse console = sshcmd.exec("cd temp2");
//        sshcmd.disconnectSession();
//
//        logger.debug(console.toString());
//        logger.debug("error: " + console.getError());
//        assertEquals(true, console.getExitStatus() == 1);
//        assertEquals(false, console.getError().equals(""));
//    }
//
//
//    @Test
//    public void scp_file_to_remote_host_Test() throws Exception {
//        SshCmd sshcmd = connectWithPrivateKey();
//        URL fileUrl = SshTest.class.getClassLoader().getResource(TEST_FILE_TO_SEND);
//
//        System.out.println("File name: " + fileUrl.getPath());
//        CommandResponse console = sshcmd.scpTo(fileUrl.getPath(),
//          "sample.properties");
//        sshcmd.disconnectSession();
//
//        assertEquals(true, console.getExitStatus() == 0);
//        logger.debug(console.toString());
//    }
//
//    @Test
//    public void getPropertyFileTest() throws Exception {
//        SshCmd sshcmd = connectWithPrivateKey();
//
//        String propertyValue = sshcmd
//          .getRemoteProperty("sample.properties", 
//            "prop1");
//
//        logger.debug("Property value: "+propertyValue);
//        assertEquals(false, propertyValue.equals(""));
//    }
//
//    @Test
//    public void setPropertyValueRemoteFileTest() throws Exception {
//        SshCmd sshcmd = connectWithPrivateKey();
//
//        String remoteFileName = "sample.properties";
//        String propertyName = "prop1";
//        String prop_value = "1111";
//
//        String push_value = "1000000011111111:"+prop_value;
//
//        CommandResponse response = sshcmd
//          .setRemoteProperty(remoteFileName, propertyName, push_value, prop_value );
//
//        logger.debug(response.toString());
//        logger.debug("Output: "+response.getOutput());
//        logger.debug("Error: "+response.getError());
//    }
//
//
//    @Test
//    @Ignore
//    public void prove_of_concept() throws Exception {
//
//        JSch jsh = new JSch();
//
//        InputStream input = this.getClass().getClassLoader()
//          .getResourceAsStream(PRIVATE_KEY_FILE_NAME);
//        final byte[] prvkey = IOUtils.toByteArray(input); 
//        final byte[] emptyPassPhrase = new byte[0];
//        jsh.addIdentity(
//                USER_NAME_QA00,
//                prvkey,
//                null,
//                emptyPassPhrase
//        );
//
//
//        Session session = jsh.getSession(USER_NAME_QA00, HOST_QA00, 22);
//
//        session.setUserInfo(new LocalUserInfo());
//        session.connect();
//
//        Channel channel = session.openChannel("exec");
//
//        //First command --------------------------------------
//        ((ChannelExec) channel).setCommand("cd temp");
//
//        CommandResponse commandResponse = new CommandResponse();
//        channel.setInputStream(commandResponse.getIn());
//        channel.setOutputStream(commandResponse.getOut());
//        ((ChannelExec) channel).setErrStream(commandResponse.getErr());
//
//        //InputStream in = channel.getInputStream();
//        channel.connect();
//
//        //Wait until command is finished.
//        while (!channel.isClosed()) {
//            try {
//                Thread.sleep(1000);
//            } catch (Exception ee) {
//                throw ee;
//            }
//        }
//        commandResponse.setExitStatus(channel.getExitStatus());
//        logger.debug("Comand1 out: " + commandResponse.getOutput());
//        logger.debug("Comand1 err: " + commandResponse.getError());
//
//
//        logger.debug("");
//
//
//        //second command on the same channel----------------------
//        channel = session.openChannel("exec");
//        ((ChannelExec) channel).setCommand("pwd");
//
//        CommandResponse consoleSsh2 = new CommandResponse();
//        channel.setInputStream(consoleSsh2.getIn());
//        channel.setOutputStream(consoleSsh2.getOut());
//        ((ChannelExec) channel).setErrStream(consoleSsh2.getErr());
//
//        //InputStream in = channel.getInputStream();
//        channel.connect();
//
//        //Wait until command is finished.
//        while (!channel.isClosed()) {
//            try {
//                Thread.sleep(1000);
//            } catch (Exception ee) {
//                throw ee;
//            }
//        }
//        commandResponse.setExitStatus(channel.getExitStatus());
//        logger.debug("Comand2 out: " + consoleSsh2.getOutput());
//        logger.debug("Comand2 err: " + consoleSsh2.getError());
//
//
//        channel.disconnect();
//
//    }
//
//
//    @Test
//    @Ignore
//    public void scpto_prove_of_concept() throws Exception {
//
//        URL fileUrl = SshTest.class.getClassLoader().getResource(TEST_FILE_TO_SEND);
//        FileInputStream fis = null;
//        try {
//
//            String lfile = fileUrl.getPath();
//            String rfile = "sample.txt";
//
//            JSch jsh = new JSch();
//
//            InputStream input = this.getClass().getClassLoader()
//              .getResourceAsStream(PRIVATE_KEY_FILE_NAME);
//            final byte[] prvkey = IOUtils.toByteArray(input); 
//            final byte[] emptyPassPhrase = new byte[0];
//            jsh.addIdentity(
//                    USER_NAME_QA00,
//                    prvkey,
//                    null,
//                    emptyPassPhrase
//            );
//
//            Session session = jsh.getSession(USER_NAME_QA00, HOST_QA00, 22);
//
//
//            
//            UserInfo ui = new LocalUserInfo();
//            session.setUserInfo(ui);
//            session.connect();
//
//
//            // exec 'scp -t rfile' remotely
//            String command = "scp -p -t " + rfile;
//            Channel channel = session.openChannel("exec");
//            ((ChannelExec) channel).setCommand(command);
//
//            // get I/O streams for remote scp
//            OutputStream out = channel.getOutputStream();
//            InputStream in = channel.getInputStream();
//
//            channel.connect();
//
//            if (checkAck(in) != 0) {
//                System.exit(0);
//            }
//
//            // send "C0644 filesize filename", where filename should not include '/'
//            long filesize = (new File(lfile)).length();
//            command = "C0644 " + filesize + " ";
//            if (lfile.lastIndexOf('/') > 0) {
//                command += lfile.substring(lfile.lastIndexOf('/') + 1);
//            } else {
//                command += lfile;
//            }
//            command += "\n";
//            out.write(command.getBytes());
//            out.flush();
//            if (checkAck(in) != 0) {
//                System.exit(0);
//            }
//
//            // send a content of lfile
//            fis = new FileInputStream(lfile);
//            byte[] buf = new byte[1024];
//            while (true) {
//                int len = fis.read(buf, 0, buf.length);
//                if (len <= 0) break;
//                out.write(buf, 0, len); //out.flush();
//            }
//            fis.close();
//            fis = null;
//            // send '\0'
//            buf[0] = 0;
//            out.write(buf, 0, 1);
//            out.flush();
//            if (checkAck(in) != 0) {
//                System.exit(0);
//            }
//            out.close();
//
//            channel.disconnect();
//            session.disconnect();
//
//            System.exit(0);
//        } catch (Exception e) {
//            System.out.println(e);
//            try {
//                if (fis != null) fis.close();
//            } catch (Exception ee) {
//            }
//        }
//    }
//
//
//    private static int checkAck(InputStream in) throws IOException {
//        int b = in.read();
//        // b may be 0 for success,
//        //          1 for error,
//        //          2 for fatal error,
//        //          -1
//        if (b == 0) return b;
//        if (b == -1) return b;
//
//        if (b == 1 || b == 2) {
//            StringBuffer sb = new StringBuffer();
//            int c;
//            do {
//                c = in.read();
//                sb.append((char) c);
//            }
//            while (c != '\n');
//            if (b == 1) { // error
//                System.out.print(sb.toString());
//            }
//            if (b == 2) { // fatal error
//                System.out.print(sb.toString());
//            }
//        }
//        return b;
//    }
//
//
//
//    @Test
//    @Ignore
//    public void poCjSchConnectionTest() throws Exception{
//
//        try{
//            JSch jsch=new JSch();
//
//            String privateKey = this.getClass().getClassLoader()
//              .getResource(PRIVATE_KEY_FILE_NAME).getPath();
//            logger.debug("Private key : "+privateKey);
//            jsch.addIdentity(privateKey);
//
//            String host=HOST_DEV00;
//            String user=USERNAME_DEV00;
//
//
//            Session session=jsch.getSession(user, host, 22);
//
//            // username and passphrase will be given via UserInfo interface.
//            UserInfo ui=new LocalUserInfo();
//            session.setUserInfo(ui);
//            session.connect();
//
//            logger.debug("Is Connected: "+session.isConnected());
//
//        }
//        catch(Exception e){
//            System.out.println(e);
//        }
//    }
}
