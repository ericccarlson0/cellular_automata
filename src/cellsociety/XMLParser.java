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

    public XMLParser() {
        DOCUMENT_BUILDER = getDocumentBuilder();
        SimulationRunner.SimulationType[] validSims = SimulationRunner.SimulationType.values();
        validSimTypes = new ArrayList<>();
        for (SimulationRunner.SimulationType sim : validSims){
            validSimTypes.add(sim.name());
        }
    }

    public Grid generateGrid (String xmlFilename, String mode) {
        File dataFile = new File(xmlFilename);
        Element root = getRootElement(dataFile);
        if (isValidFile(root)) {
            SimulationRunner.SimulationType simType = SimulationRunner.SimulationType.valueOf
                    (getAttribute(root,"simType"));
            int dimensions = getDimensions(root);
            ArrayList<Double> percents = getPercents(root);
            ArrayList<String> states = getStates(root);
            ArrayList<Double> misc = getMisc(root);
            return new Grid(simType, dimensions, percents, states, misc, mode);
        } else {
            return null;
        }
    }

    private ArrayList<Double> getMisc(Element root) {
        String[] misc = getTextValue(root,"misc").split(",");
        ArrayList<Double> mList = new ArrayList<Double>();
        for(String m : misc){
            m = m.strip();
            mList.add(Double.parseDouble(m));
        }
        return mList;
    }

    private int getDimensions(Element root) {
        String dimString = getTextValue(root, "size");
        dimString = dimString.strip();
        return Integer.parseInt(dimString);
    }

    private ArrayList<Double> getPercents (Element root) {
        String[] percentages = getTextValue(root, "percents").split(",");
        ArrayList<Double> percentList = new ArrayList<Double>();
        for (String percent: percentages) {
            percent = percent.strip();
            percentList.add(Double.parseDouble(percent));
        }
        return percentList;
    }

    private ArrayList<String> getStates (Element root){
        String[] states = getTextValue(root,"states").split(",");
        ArrayList<String> stateList = new ArrayList<>();
        for (String state: states) {
            state = state.strip();
            stateList.add(state);
        }
        return stateList;
    }

    private String getTextValue (Element e, String tagName) {
        NodeList nodeList = e.getElementsByTagName(tagName);
        if (nodeList != null && nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        } else {
            // FIXME: EMPTY STRING or EXCEPTION? In some cases it may be an error to not find any text...
            return "";
        }
    }

    private boolean isValidFile (Element root){
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
        } catch (SAXException | IOException e) {
            throw new XMLException(e);
        }
    }

    private DocumentBuilder getDocumentBuilder () {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new XMLException(e);
        }
    }
}
