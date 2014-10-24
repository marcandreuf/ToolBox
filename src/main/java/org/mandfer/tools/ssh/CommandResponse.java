package org.mandfer.tools.ssh;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Class to represent console for the ssh util.
 *
 * @author marcandreuf on 24/10/2014.
 */
public class CommandResponse {


    public static final String OUTPUT_ENCODING = "UTF-8";
    private InputStream in = System.in;
    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    private ByteArrayOutputStream err = new ByteArrayOutputStream();
    private int exitStatus = 0;


    public OutputStream getErr() {
        return err;
    }

    public void setErr(ByteArrayOutputStream err) {
        this.err = err;
    }

    public InputStream getIn() {
        return in;
    }

    public void setIn(InputStream in) {
        this.in = in;
    }

    public OutputStream getOut() {
        return out;
    }

    public void setOut(ByteArrayOutputStream out) {
        this.out = out;
    }

    public int getExitStatus() {
        return exitStatus;
    }

    public void setExitStatus(int exitStatus) {
        this.exitStatus = exitStatus;
    }

    @Override
    public String toString() {
        return "CommandResponse{" +
                "err=" + err.size() + " bytes" +
                ", out=" + out.size() + " bytes" +
                ", exit status code=" + exitStatus +
                '}';
    }

    public String getOutput() throws UnsupportedEncodingException {
        return out.toString(OUTPUT_ENCODING);
    }

    public String getError() throws UnsupportedEncodingException {
        return err.toString(OUTPUT_ENCODING);
    }
}
