package cellsociety;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
        System.out.println("*");
        File dataFile = new File(xmlFilename);
        System.out.println("**");
        Element root = getRootElement(dataFile);
        if (!isValidFile(root)) {
            System.out.println("INVALID!!!");
            return null;
        }
        else{
            System.out.println("VALID!!!");
            return null;
        }
    }

    private boolean isValidFile(Element root){
        String possibleType = getAttribute(root, "simType");
        System.out.println(possibleType);
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
