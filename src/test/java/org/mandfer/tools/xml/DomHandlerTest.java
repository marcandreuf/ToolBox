package org.mandfer.tools.xml;


import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mandfer.tools.guice.ToolsBoxFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author marcandreuf on 24/10/2014.
 */
public class DomHandlerTest {


    private final Logger logger = LoggerFactory.getLogger(DomHandlerTest.class);
    private final String fileName = "test.xml";
    private final DomLoader domLoader = ToolsBoxFactory.getInstance(DomLoader.class);
    private final DomHandler domHandler = ToolsBoxFactory.getInstance(DomHandler.class);
    private final XMLFinder xmlFinder = ToolsBoxFactory.getInstance(XMLFinder.class);


//    @Test
//    public void addElementNodeToADocument() throws Exception{
//        String elementType = "resource";
//        Document doc = domLoader.loadDocument(fileName);
//
//        List<Node> listBefore = xmlFinder.findNodesByPath("//"+elementType, doc);
//        int sizeBefore = getListSize(listBefore);
//
//        Element element = createTestElement(elementType, doc);
//        domHandler.addElementToDocument(element, doc);
//
//        List<Node> listAfter = xmlFinder.findNodesByPath("//"+elementType, doc);
//        int sizeAfter = getListSize(listAfter);
//
//        assertEquals(true, sizeBefore < sizeAfter);
//    }
//
//    private Element createTestElement(String elementType, Document doc) {
//        Element element = doc.createElement(elementType);
//        element.setAttribute("name","c");
//        return element;
//    }
//
//    private int getListSize(List<Node> listNode) throws Exception {
//        int sizeBefore = listNode.size();
//        logger.debug("List size: "+ sizeBefore);
//        return sizeBefore;
//    }
//
//
//    @Test
//    public void addFirstNodeToADocument() throws Exception{
//        String elementType = "newResourceTag";
//        Document doc = domLoader.loadDocument(fileName);
//
//        List<Node> listBefore = xmlFinder.findNodesByPath("//"+elementType, doc);
//        int sizeBefore = getListSize(listBefore);
//        assertEquals(true, sizeBefore == 0);
//
//        Element element = createTestElement(elementType, doc);
//        domHandler.addElementToDocument(element, doc);
//
//        List<Node> listAfter = xmlFinder.findNodesByPath("//"+elementType, doc);
//        int sizeAfter = getListSize(listAfter);
//
//        assertEquals(true, sizeAfter == 1);
//    }

}
