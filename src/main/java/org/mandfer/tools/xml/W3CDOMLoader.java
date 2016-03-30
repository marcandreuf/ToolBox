package org.mandfer.tools.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author marcandreuf on 24/10/2014.
 *
 */
public class W3CDOMLoader implements DomLoader {

    Logger logger = LoggerFactory.getLogger(W3CDOMLoader.class);

    //For now it is not required a synchronized or concurrent map.
    private static Map<String, Document> domObjects_cache = new Hashtable<String, Document>();


    public Document loadDocument(String fileName) throws XMLException {
        Document doc;

        if(domObjects_cache.containsKey(fileName)){
             doc = domObjects_cache.get(fileName);
        }else{
            doc = loadDocumentFromInputStream(W3CDOMLoader.class.getClassLoader().getResourceAsStream(fileName));

            if(doc !=null){
                URL url = this.getClass().getClassLoader().getResource(fileName);
                logger.debug("Document loaded from: "+url);
                domObjects_cache.put(fileName, doc);
            }
        }

        return doc;
    }

    @Override
    public Document loadDocumentFromInputStream(InputStream isContent) throws XMLException {
        Document doc;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(isContent);
        } catch (Exception e) {
            throw new XMLException(e.getMessage(),e);
        }
        return doc;
    }


    @Override
    public Document loadDocumentFromURL(String url) throws XMLException {
        Document doc;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(url);
        } catch (Exception e) {
            throw new XMLException(e.getMessage(),e);
        }
        return doc;
    }

    @Override
    public Document loadDocumentFromPath(String fullPathFileName) throws XMLException {
        Document doc;

        try{
            if(domObjects_cache.containsKey(fullPathFileName)){
                doc = domObjects_cache.get(fullPathFileName);
            }else{
                FileInputStream fileInputStream = new FileInputStream(new File(fullPathFileName));
                doc = loadDocumentFromInputStream(fileInputStream);
                if(doc!=null){
                    logger.debug("Document loaded from path: "+fullPathFileName);
                }
                domObjects_cache.put(fullPathFileName, doc);
            }
        } catch (Exception e) {
            throw new XMLException(e.getMessage(),e);
        }

        return doc;
    }

    @Override
    public int getNumCachedDocuments() {
        return domObjects_cache.size();
    }

    @Override
    public void flushDocument(String xmlDocumentFileName) {
        domObjects_cache.remove(xmlDocumentFileName);
    }
}
