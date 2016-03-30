package org.mandfer.tools.xml;

import org.w3c.dom.Document;

import javax.xml.transform.TransformerException;
import java.io.File;

/**
 * @author marcandreuf on 24/10/2014.
 */
public interface XMLWriter {

    /**
     * Extract the content of a Document instance into a String.
     *
     * @param doc
     * @throws XMLException
     */
    public String writeXmlContent(Document doc) throws XMLException;


    /**
     * Save the xml document into a file on the disc.
     *
     * @param doc
     * @param file
     * @throws TransformerException
     */
    public void writeDocumentToFile(Document doc, File file) throws TransformerException;
}
