package org.mandfer.tools.xml;

import org.w3c.dom.Document;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;

/**
 * @author marcandreuf on 24/10/2014.
 */
public class XMLWriterImpl implements XMLWriter {

    public String writeXmlContent(Document doc) throws XMLException {

        StringWriter sw = null;

        try {
            //set up a transformer
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            //create string from xml tree
            sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);

        } catch (TransformerConfigurationException e) {
            throw new XMLException(e.getMessage(), e);
        } catch (TransformerException e) {
            throw new XMLException(e.getMessage(), e);
        }

        return sw.toString();
    }

    @Override
    public void writeDocumentToFile(Document doc, File file) throws TransformerException {
        DOMSource source = new DOMSource(doc);
        Result result = new StreamResult(file.toURI().getPath());
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, result);
    }

}
