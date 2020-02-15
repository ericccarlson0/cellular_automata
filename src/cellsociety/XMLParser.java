package cellsociety;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cellsociety.backend.gridstructures.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XMLParser object that is created in the Menu that allows for parsing and handling of XML Files
 */
public class XMLParser {
    private final DocumentBuilder DOCUMENT_BUILDER;
    private ArrayList<String> validSimTypes;

    /**
     * Constructor for XML Parser
     */
    public XMLParser() {
        DOCUMENT_BUILDER = getDocumentBuilder();
        SimulationMenu.SimulationType[] validSims = SimulationMenu.SimulationType.values();
        validSimTypes = new ArrayList<>();
        for (SimulationMenu.SimulationType sim : validSims){
            validSimTypes.add(sim.name());
        }
    }

    /**
     * generates a GridStructure object based on xml file and specified values if the xml file is of a valid format
     * @param xmlFilename String representing the filename of the xml file this structure object is being created based off of.
     * @param isTorus boolean representing whether the new sim being created should follow torus functionality
     * @param neighborhoodType int representing neighborhood type for simulation being created
     * @return GridStructure object created based on specifications of xml filename and parameters passed in, null if invalid
     */
    public GridStructure generateGrid (String xmlFilename, boolean isTorus, int neighborhoodType) {
        File dataFile = new File(xmlFilename);
        Element root = getRootElement(dataFile);
        if (isValidFile(root)) {
            SimulationMenu.SimulationType simType = SimulationMenu.SimulationType.valueOf
                    (getAttribute(root,"simType"));
            int dimensions = getDimensions(root);
            ArrayList<Double> percents = getPercents(root);
            ArrayList<String> states = getStates(root);
            ArrayList<Double> misc = getMisc(root);
            return generateGridStructure(simType,dimensions,percents,states,misc, isTorus,neighborhoodType);
        } else {
            return null;
        }
    }


    private GridStructure generateGridStructure(SimulationMenu.SimulationType simType, int dimensions,
                                                ArrayList<Double> percents, ArrayList<String> states, ArrayList<Double> misc, boolean isTorus, int neighborhoodType) {
        GridStructure grid = null;
        //TODO implement rowNum and colNum in XML (instead of "dimensions")
        int rowNum = dimensions+1;
        int colNum = dimensions+1;
        switch(simType){
            case LIFE:
                //TODO figure out how to specify number of neighbors based on shape of cells
                grid = new LifeGrid(rowNum, colNum, percents, states, isTorus, neighborhoodType);
                break;
            case FIRE:
                grid = new FireGrid(rowNum, colNum, percents, states, isTorus,neighborhoodType, misc.get(0));
                break;
            case PERCOLATION:
                grid = new PercolationGrid(rowNum, colNum, percents, states, isTorus,neighborhoodType,misc.get(0));
                break;
            case SEGREGATION:
                grid = new SegregationGrid(rowNum, colNum, percents, states, isTorus,neighborhoodType,misc.get(0));
                break;
            case PRED_PREY:
                grid = new PredPreyGrid(dimensions, dimensions, percents, states, isTorus,neighborhoodType, misc.get(0), misc.get(1), misc.get(2));
                break;
            case RPS:
                grid = new RockPaperScissorsGrid(rowNum, colNum, percents,states, isTorus,neighborhoodType,misc.get(0));
                break;
        }
        return grid;
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
