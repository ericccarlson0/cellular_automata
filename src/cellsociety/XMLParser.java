package cellsociety;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class XMLParser {
    private final DocumentBuilder DOCUMENT_BUILDER;
    private ArrayList<String> validSimTypes;

    public XMLParser(){
        DOCUMENT_BUILDER = getDocumentBuilder();
        SimulationRunner.SimulationType[] validSims = SimulationRunner.SimulationType.values();
        validSimTypes = new ArrayList<String>();
        for(SimulationRunner.SimulationType sim : validSims){
            validSimTypes.add(sim.name());
        }
        System.out.println(validSimTypes.get(0));
    }

    public Grid generateGrid(String xmlFilename){
        File dataFile = new File(xmlFilename);
        Element root = getRootElement(dataFile);
        if (isValidFile(root)) {
            SimulationRunner.SimulationType simType = SimulationRunner.SimulationType.valueOf(getAttribute(root,"simType"));
            int dimensions = Integer.parseInt(getTextValue(root,"size"));
            double percentActive = Double.parseDouble(getTextValue(root,"numActive"));
            return new Grid(simType,dimensions,percentActive);
        }
        else{
            return null;
        }
    }

    private String getTextValue (Element e, String tagName) {
        NodeList nodeList = e.getElementsByTagName(tagName);
        if (nodeList != null && nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        } else {
            // FIXME: empty string or exception? In some cases it may be an error to not find any text
            return "";
        }
    }

    private boolean isValidFile(Element root){
        String possibleType = getAttribute(root, "simType");
        return validSimTypes.contains(possibleType);
    }

    private String getAttribute (Element e, String attributeName) {
        return e.getAttribute(attributeName);
    }

    private Element getRootElement (File xmlFile) {
        try {
            DOCUMENT_BUILDER.reset();
            Document xmlDocument = DOCUMENT_BUILDER.parse(xmlFile);
            return xmlDocument.getDocumentElement();
        }
        catch (SAXException | IOException e) {
            throw new XMLException(e);
        }
    }

    private DocumentBuilder getDocumentBuilder () {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            throw new XMLException(e);
        }
    }

}