package cellsociety;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
            int dimensions = getDimensions(root);
            ArrayList<Double> percents = getPercents(root);
            ArrayList<String> states = getStates(root);
            double misc = Double.parseDouble(getTextValue(root,"misc"));
            return new Grid(simType,dimensions,percents,states,misc);
        }
        else{
            return null;
        }
    }

    private int getDimensions(Element root){
        return Integer.parseInt(getTextValue(root,"size"));
    }

    private ArrayList<Double> getPercents(Element root){
        String[] percent = (getTextValue(root,"percents")).split(",");
        ArrayList<Double> percents = new ArrayList<Double>();
        for(int i = 0; i < percent.length; i++){
            percents.add(Double.parseDouble(percent[i]));
        }
        return percents;
    }

    private ArrayList<String> getStates(Element root){
        String[] state = (getTextValue(root,"states")).split(",");
        return new ArrayList<>(Arrays.asList(state));
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
