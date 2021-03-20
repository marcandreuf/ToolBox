package org.mandfer.tools.xml;

/**
 * @author marcandreuf
 */
public class XMLUtilsTest {

//    private Logger logger = LoggerFactory.getLogger(XMLUtilsTest.class);
//    private final String fileName = "test.xml";
//    private final String resourcesPath = "src/main/resources/";
//    private DomLoader domLoader = ToolsBoxFactory.createDomLoader();
//    //private DomCreator domCreator = ToolsBoxFactory.createDomCreator();
//    private XMLFinder xmlFinder = ToolsBoxFactory.createXMLFinder();
//    private XMLWriter xmlWriter = ToolsBoxFactory.createXMLWriter();
//
//
//    private Object DOCUMENT_DOESNT_EXISTS  = null;
//
//    private static final String RESOURCES_SCHEMA_NAMESPACE_PREFIX = "xmlns:xsi";
//    private static final String RESOURCES_SCHEMA_NAMESPACE_URL = "http://www.w3.org/2001/XMLSchema-instance";
//    private static final String RESOURCES_SCHEMA_ATTRIBUTE_KEY = "xsi:noNamespaceSchemaLocation";
//    private static final String RESOURCES_SCHEMA_ATTRIBUTE_VALUE = "resources.xsd";
//
//    private static final String xmlContent = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
//                "<events uid=\"70000085\" game_id=\"20\">" +
//                    "<event id=\"5829365\" eventVer=\"0\" appVer=\"1.2.0.01-SNAPSHOT\">" +
//                        "<event_date_time>2011-07-11 09:03:47</event_date_time>" +
//                        "<event_type>0</event_type>" +
//                        "<error_level>0</error_level>" +
//                        "<event_data>{\"d\":[{\"scenario_name\":\"The Graveyard\",\"turns\":\"1\",\"wins\":\"3\"},\"game-complete-map\",\"_\",1310371426748,70000085,\"100001523618143\",34]}</event_data>" +
//                        "<event_uid>70000085</event_uid>" +
//                    "</event>" +
//                    "<event id=\"5829363\" eventVer=\"0\" appVer=\"1.2.0.01-SNAPSHOT\">" +
//                        "<event_date_time>2011-07-11 09:03:47</event_date_time>" +
//                        "<event_type>10</event_type>" +
//                        "<error_level>0</error_level>" +
//                        "<event_data>{\"d\":[{\"scenario_name\":\"The Graveyard\",\"turns\":\"1\",\"wins\":\"3\"},\"game-complete-map\",\"_\",1310371426748,70000085,\"100001523618143\",34]}</event_data>" +
//                        "<event_uid>70000085</event_uid>" +
//                    "</event>" +
//                    "<event id=\"5829364\" eventVer=\"0\" appVer=\"1.2.0.01-SNAPSHOT\">" +
//                        "<event_date_time>2011-07-11 09:03:47</event_date_time>" +
//                        "<event_type>5</event_type>" +
//                        "<error_level>0</error_level>" +
//                        "<event_data>{\"d\":[{\"scenario_name\":\"The Graveyard\",\"turns\":\"1\",\"wins\":\"3\"},\"game-complete-map\",\"_\",1310371426748,70000085,\"100001523618143\",34]}</event_data>" +
//                        "<event_uid>70000085</event_uid>" +
//                    "</event>" +
//                "</events>";
//
//    @Test
//    public void loadDocumentFromFileNameOnly() throws Exception{
//        Document doc = domLoader.loadDocument(fileName);
//        assertEquals(false, doc == DOCUMENT_DOESNT_EXISTS);
//    }
//
//    @Test
//    public void loadDocumentFromPathFileName() throws Exception {
//        Document doc = domLoader.loadDocumentFromPath("src/main/resources/"+fileName);
//        assertEquals(false, doc == DOCUMENT_DOESNT_EXISTS);
//    }
//
//    @Test
//    public void removeDocumentFromCache() throws Exception{
//
//        String pathFileName =  "src/main/resources/"+fileName;
//        Document doc = domLoader.loadDocumentFromPath("src/main/resources/"+fileName);
//
//        int cachedDocumentsBefore = domLoader.getNumCachedDocuments();
//        assertEquals(true, cachedDocumentsBefore > 0);
//        logger.debug("Cached documents before: "+cachedDocumentsBefore);
//
//        domLoader.flushDocument(pathFileName);
//
//        int cachedDocumentsAfter = domLoader.getNumCachedDocuments();
//        logger.debug("Cached documents after: "+cachedDocumentsAfter);
//
//        assertEquals(true, cachedDocumentsBefore > cachedDocumentsAfter);
//    }
//
//    @Test
//    public void searchAnAssetByNameAttributeInAXmlDomTest() throws Exception {
//        Document doc = domLoader.loadDocument(fileName);
//
//        String nameNodeAttribute = "popupOkBtn";
//        Node node = findAssetByName(doc, nameNodeAttribute);
//
//        assertEquals(true, node != null);
//        logger.debug("xml element: " + node.getNodeName());
//    }
//
////    @Test
////    public void createOneAssetXmlDocumentTest() throws Exception {
////
////        Document emptyResourcesDocument = domCreator.createNewEmptyresourcesDocument("resources",
////                                                        RESOURCES_SCHEMA_NAMESPACE_PREFIX, RESOURCES_SCHEMA_NAMESPACE_URL,
////                                                        RESOURCES_SCHEMA_ATTRIBUTE_KEY, RESOURCES_SCHEMA_ATTRIBUTE_VALUE);
////
////        assertEquals(true, emptyResourcesDocument != null);
////        assertEquals(true, emptyResourcesDocument.getDocumentElement().getChildNodes().getLength() == 0);
////    }
//
//    @Test
//    public void addNodeToEmptyDocumentTest() throws Exception {
//
//        Document singleAssetDoc = newDocumentWithOneNodeSearched();
//        assertEquals(false, singleAssetDoc == null);
//        assertEquals(true, singleAssetDoc.getDocumentElement().getChildNodes().getLength() > 0);
//        logger.debug("test.xml: " + xmlWriter.writeXmlContent(singleAssetDoc));
//    }
//
//
//
//    @Test
//    public void searchByXpathTest() throws Exception{
//
//        String content = "<events game_id=\"20\" uid=\"70000085\">" +
//                "<maxEventId id=\"5752530\" version=\"1.2.0.01\"/>" +
//                "</events>";
//        InputStream is = new ByteArrayInputStream(content.getBytes("UTF-8"));
//        Document doc = domLoader.loadDocumentFromInputStream(is);
//        logger.debug("doc file : "+ xmlWriter.writeXmlContent(doc));
//
//        Node node = xmlFinder.findNodeByPath("/events/maxEventId", doc);
//        logger.debug("id: "+ node.getAttributes().getNamedItem("id").getNodeValue() );
//        assertEquals(true, node != null);
//
//        Node node_dose_not_exists = null;
//        node = xmlFinder.findNodeByPath("/events/undefinied", doc);
//        assertEquals(true, node == node_dose_not_exists);
//    }
//
//
//    @Test
//    public void searchTypeTest() throws Exception {
//
//        Node node = null;
//        InputStream is = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
//        Document doc = domLoader.loadDocumentFromInputStream(is);
//        logger.debug("doc file : "+ xmlWriter.writeXmlContent(doc));
//
//        //Find node type 0
//        node = xmlFinder.findNodeByPath("/events/event[event_type=0]", doc);
//        assertEquals(true, node != null);
//
//        //Try to find node type 2.
//        node = xmlFinder.findNodeByPath("/events/event[event_type=2]", doc);
//        assertEquals(true, node == null);
//
//        //Find node type 0 or node type 10
//        List<Node> nodes = null;
//        nodes = xmlFinder.findNodesByPath("/events/event[event_type=0] | /events/event[event_type=10]", doc);
//        assertEquals(true, nodes != null && nodes.size()==2);
//        assertEquals(true, nodes.get(0).getAttributes().getNamedItem("id").getNodeValue().equals("5829365"));
//
//        //Check empty list.
//        List<Node> emtyList = null;
//        emtyList = xmlFinder.findNodesByPath("/events/event[event_type=30] | /events/event[event_type=40]", doc);
//        assertEquals(true, emtyList!=null && emtyList.size()==0);
//    }
//
//
//    @Test
//    public void extractTypeTest() throws Exception{
//        InputStream is = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
//        Document doc = domLoader.loadDocumentFromInputStream(is);
//        logger.debug("doc file : "+ xmlWriter.writeXmlContent(doc));
//
//        //Find node type 0
//        Node node = xmlFinder.findNodeByPath("/events/event[event_type=0]", doc);
//        assertEquals(true, node != null);
//
//        Node nodeType = null;
//        nodeType = xmlFinder.findNodeByPath("event_type", node);
//        logger.debug("node: "+nodeType.getNodeName()+" value "+nodeType.getTextContent());
//        String type = nodeType.getTextContent();
//
//        assertEquals(false, nodeType == null);
//        assertEquals(true, type.equals("0"));
//
//    }
//
//    @Test
//    public void getEventNodes() throws Exception{
//        InputStream is = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
//        Document doc = domLoader.loadDocumentFromInputStream(is);
//        logger.debug("doc file : "+ xmlWriter.writeXmlContent(doc));
//
//        List<Node> nodes = null;
//        nodes = xmlFinder.findNodesByPath("/events/event",doc);
//        logger.debug("Nodes size: "+nodes.size());
//        assertEquals(true, nodes != null);
//        assertEquals(true, nodes.size() == 3);
//    }
//
//
//    @Test
//    public void getNodesByType() throws Exception{
//        InputStream is = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
//        Document doc = domLoader.loadDocumentFromInputStream(is);
//        List<Node> nodes = xmlFinder.findNodesByPath("//event",doc);
//        assertEquals(true, nodes.size() == 3);
//    }
//
//
//    @Test
//    @Ignore
//    public void PoCWriteNodeTestType() throws Exception{
//        String nodeType = "resource";
//        Document doc = domLoader.loadDocument(fileName);
//
//        List<Node> nodes = xmlFinder.findNodesByPath("//"+nodeType, doc);
//        if(!nodes.isEmpty()){
//            Node firstNode = nodes.get(0);
//
//            Node parent = firstNode.getParentNode();
//
//            Element newElement = doc.createElement("resource");
//            newElement.setAttribute("name","c");
//
//            parent.insertBefore(newElement, firstNode);
//
//            File outFile = new File(resourcesPath+fileName);
//            xmlWriter.writeDocumentToFile(doc, outFile);
//        }
//    }
//
//
//
//
//
//    // --------------------------- Private methods -------------------------
//
//    private Node findAssetByName(Document resourcesDoc, String attributeName)  throws Exception{
//
//        //Find asset element node.
//        Node assetNode = xmlFinder.findByNameAttribute(attributeName, resourcesDoc,
//                                RESOURCES_SCHEMA_NAMESPACE_PREFIX, RESOURCES_SCHEMA_NAMESPACE_URL);
//
//        return assetNode;
//    }
//
//    private Document newDocumentWithOneNodeSearched() throws Exception{
//        Document resourcesDoc = domLoader.loadDocument(fileName);
//        Node assetNode = findAssetByName(resourcesDoc, "popupOkBtn");
//        Document singleAssetDoc = domCreator.createNewEmptyresourcesDocument("resources",
//                                                        RESOURCES_SCHEMA_NAMESPACE_PREFIX, RESOURCES_SCHEMA_NAMESPACE_URL,
//                                                        RESOURCES_SCHEMA_ATTRIBUTE_KEY, RESOURCES_SCHEMA_ATTRIBUTE_VALUE);
//        domCreator.addNodeToDocumentRootElement(singleAssetDoc, assetNode);
//        return singleAssetDoc;
//    }



}
