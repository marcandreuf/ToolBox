package org.mandfer.tools.xml;

import org.w3c.dom.Node;

import java.util.List;

/**
 * @author marcandreuf on 24/10/2014.
 */
public interface XMLFinder {

    /**
     * Look for attribute with a given name into the given document. It is possible to provide information about the
     * schema prefix and schema url to find elements with specific namespace.
     *
     * @param nameAttribute
     * @param document
     * @param schema_prefix
     * @param schema_url
     * @throws XMLException
     */
    public Node findByNameAttribute(String nameAttribute, Object document, String schema_prefix, String schema_url) throws XMLException;

    /**
     * Look up for a node with a given Xpath name.
     *
     * @param xpath
     * @param document
     * @throws XMLException
     */
    public Node findNodeByPath(String xpath, Object document) throws XMLException;

    /**
     * Look up for a node with a given Xpath name, providing specific prefixes and schema url in order to find
     * nodes specific namespace.
     *
     * @param xpath
     * @param document
     * @param schema_prefix
     * @param schema_url
     * @throws XMLException
     */
    public Node findNodeByPath(String xpath, Object document, String schema_prefix, String schema_url) throws XMLException;

    /**
     * Look up for nodes with a given Xpath name.
     *
     * @param xpath
     * @param document
     * @throws XMLException
     */
    public List<Node> findNodesByPath(String xpath, Object document) throws XMLException;


    /**
     * Look up for nodes with a given Xpath name, providing specific prefixes and schema url in order to find
     * nodes specific namespace.
     *
     * @param xpath
     * @param document
     * @param schema_prefix
     * @param schema_url
     * @throws XMLException
     */
    public List<Node> findNodesByPath(String xpath, Object document, String schema_prefix, String schema_url) throws XMLException;


}
