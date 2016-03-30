package org.mandfer.tools.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author marcandreuf on 24/10/2014.
 */
public interface DomHandler {


    /**
     * Add element to document before the elements group of the same type.
     * If is the first node of its type will be added as first child of the root element.
     *
     * @param elementToAdd
     * @param document
     */
    public void addElementToDocument(Element elementToAdd, Document document) throws XMLException;
}
