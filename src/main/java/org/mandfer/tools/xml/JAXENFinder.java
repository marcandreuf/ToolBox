package org.mandfer.tools.xml;

import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import java.util.List;

/**
 * @author marcandreuf on 24/10/2014.
 */
public class JAXENFinder implements XMLFinder {

    private Logger logger = LoggerFactory.getLogger(JAXENFinder.class);
    private String UNDEFINED_NAMESPACE_PREFIX = "";
    private String UNDEFINED_NAMESPACE_URL = "";


    public Node findByNameAttribute(String nameAttribute, Object document, String schema_prefix, String schema_url) throws XMLException {
        Node retVal = null;
        String xPathSearchByName = "";
        xPathSearchByName = nameAttributeXPathQuery(nameAttribute);
        retVal = searchNodeByXpath(xPathSearchByName, document, schema_prefix, schema_url);
        return retVal;
    }

    @Override
    public Node findNodeByPath(String xpath, Object document) throws XMLException {
        return findNodeByPath(xpath, document, UNDEFINED_NAMESPACE_PREFIX, UNDEFINED_NAMESPACE_URL);
    }

    @Override
    public Node findNodeByPath(String xpath, Object document, String schema_prefix, String schema_url) throws XMLException {
        Node retVal = null;
        retVal = searchNodeByXpath(xpath, document, schema_prefix, schema_url);
        return retVal;
    }


    @Override
    public List<Node> findNodesByPath(String xpath, Object document) throws XMLException {
        return findNodesByPath(xpath, document, UNDEFINED_NAMESPACE_PREFIX, UNDEFINED_NAMESPACE_URL);
    }

    @Override
    public List<Node> findNodesByPath(String xpath, Object document, String schema_prefix, String schema_url) throws XMLException {
        List<Node> nodes = null;

        nodes = searchNodesByXpath(xpath, document, schema_prefix, schema_url);

        return nodes;
    }


    // -------------------- Protected methods ----------------------------

    protected String nameAttributeXPathQuery(String nameAttribute) {
        String retVal = "";

        //Simple or complex name.
        String[] names = nameAttribute.split("/");
        if (names.length == 1) {
            //It is a simple name, so search any node with this name attribute.
            retVal = "//*[@name='" + nameAttribute + "']";
        } else {
            //It is comlpex name, so search any node with this name attribute into the xPathName xml structure.
            String xPathName = nameAttribute.substring(0, nameAttribute.lastIndexOf("/"));
            String elementName = nameAttribute.substring(nameAttribute.lastIndexOf("/") + 1, nameAttribute.length());
            retVal = "//" + xPathName + "[@name='" + elementName + "']";
        }

        return retVal;
    }


    // -------------------- Private methods ----------------------------

    private Node searchNodeByXpath(String xPath, Object document, String schema_prefix, String schema_url) throws XMLException {
        Node retVal = null;

        try {
            logger.debug("xPath query to search by attribute name = " + xPath);
            //Using JAXEN to search with the XPath.
            XPath expression = new DOMXPath(xPath);

            if ((schema_prefix != null && !schema_prefix.equals("")) &&
                    (schema_url != null && !schema_url.equals(""))) {
                expression.addNamespace(schema_prefix, schema_url);
            }
            retVal = (Node) expression.selectSingleNode(document);

        } catch (JaxenException e) {
            throw new XMLException(e.getMessage(), e);
        }

        return retVal;
    }

    private List<Node> searchNodesByXpath(String xPath, Object document, String schema_prefix, String schema_url) throws XMLException {
        List<Node> retVal = null;

        try {
            logger.debug("xPath query to search nodes " + xPath);
            XPath expression = new DOMXPath(xPath);

            if ((schema_prefix != null && !schema_prefix.equals("")) &&
                    (schema_url != null && !schema_url.equals(""))) {
                expression.addNamespace(schema_prefix, schema_url);
            }
            retVal = (List<Node>) expression.selectNodes(document);

        } catch (JaxenException e) {
            throw new XMLException(e.getMessage(), e);
        }


        return retVal;
    }

}
