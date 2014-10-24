package org.mandfer.tools.ssh;


/**
 * @author marcandreuf on 24/10/2014.
 */
public class SshCmdException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Create a new Exception.
     *
     * @param msg Message text of the exception
     */
    public SshCmdException(String msg) {
        super(msg);
    }

    /**
     * re-throw an Exception with a new message.
     *
     * @param cause Exception that caused this exception
     * @param msg   Message text of the exception
     */
    public SshCmdException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * re-throw an Exception.
     *
     * @param cause Exception that caused this exception
     */
    public SshCmdException(Throwable cause) {
        super(cause);
    }
}
