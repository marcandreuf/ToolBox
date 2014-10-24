package org.mandfer.tools.xml;

/**
 * @author marcandreuf on 24/10/2014.
 */
public class XMLException extends Exception {

    public XMLException(String msg) {
        super(msg);
    }

    public XMLException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
