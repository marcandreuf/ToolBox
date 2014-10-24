package org.mandfer.tools.xml;

import com.google.inject.Inject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

/**
 * @author marcandreuf on 24/10/2014.
 */
public class DomHandlerImpl implements DomHandler {

    private final XMLFinder xmlFinder;

    @Inject
    public DomHandlerImpl(XMLFinder xmlFinder) {
        this.xmlFinder = xmlFinder;
    }

    @Override
    public void addElementToDocument(Element elementToAdd, Document document) throws XMLException {
        String tafName = elementToAdd.getTagName();
        List<Node> nodes = xmlFinder.findNodesByPath("//" + tafName, document);
        Node referenceNode;

        if (!nodes.isEmpty()) {
            referenceNode = nodes.get(0);
            Node parent = referenceNode.getParentNode();
            parent.insertBefore(elementToAdd, referenceNode);
        } else {
            referenceNode = document.getDocumentElement().getChildNodes().item(0);
            document.getDocumentElement().insertBefore(elementToAdd, referenceNode);
        }
    }
}
