package org.mandfer.tools.xml;


import org.w3c.dom.Document;

import java.io.InputStream;

/**
 * @author marcandreuf on 24/10/2014.
 */
public interface DomLoader {

    /**
     * Load xml document from file name.
     *
     * @param fileName, file name of the xml document. The file needs to be located into the class path.
     * @return
     * @throws XMLException
     */
    public Document loadDocument(String fileName) throws XMLException;


    /**
     * Load xml document from input stream.
     *
     * @param isContent
     * @return
     * @throws XMLException
     */
    public Document loadDocumentFromInputStream(InputStream isContent) throws XMLException;


    /**
     * Load xml document from URL.
     *
     * @param url
     * @return
     * @throws XMLException
     */
    public Document loadDocumentFromURL(String url) throws XMLException;

    /**
     * Load xml document file from full path file name.
     *
     * @param fullPathFileName
     * @return
     */
    public Document loadDocumentFromPath(String fullPathFileName) throws XMLException;


    /**
     * Get num of xml cached documents.
     *
     * @return
     */
    public int getNumCachedDocuments();


    /**
     * Flush xml document from cache.
     *
     * @param pathFileName
     */
    public void flushDocument(String pathFileName);
}
