package org.mandfer.tools.email;

/**
 * @author marcandreuf
 */
//TODO: Clean up and use guice to inject dependencies.
public class MailAttachment {

    private String filename;
    private String fileType;

    public MailAttachment(String filename, String fileType) {
        this.filename = filename;
        this.fileType = fileType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}