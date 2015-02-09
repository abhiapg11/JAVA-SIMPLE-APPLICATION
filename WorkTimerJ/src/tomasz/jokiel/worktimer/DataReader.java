package tomasz.jokiel.worktimer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DataReader {

    public static WorkTimmerSummary readDataBase() {
        WorkTimmerSummary workTimmerSummary = new WorkTimmerSummary();

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc;

            File dataBaseFile = new File(DataBaseScheme.DATA_BASE_FILE_NAME);

            if(dataBaseFile.exists()) {
                doc = docBuilder.parse(dataBaseFile);
                doc.getDocumentElement().normalize();
                
                int globalDelta = getSingleGlobalIntElementValue(doc, DataBaseScheme.WT_CONFIG_DATA_GLOBAL_DELTA_TAG_NAME);
                workTimmerSummary.globalDelta = globalDelta;
                System.out.println("##_DataReader, global delta = " + globalDelta);

                int weeklyDelta = getSingleGlobalIntElementValue(doc, DataBaseScheme.WT_CONFIG_DATA_WEEK_DELTA_TAG_NAME);
                workTimmerSummary.weeklyDelta = weeklyDelta;
                System.out.println("##_DataReader, week delta = " + weeklyDelta);

                int dayliTimerValue = getSingleGlobalIntElementValue(doc, DataBaseScheme.WT_WINDOW_CLOSE_CONFIG_DATA_DAYLI_TIMER_TAG_NAME);
                workTimmerSummary.dayliTimerValue = dayliTimerValue;

                List<String> timersDescriptions = getStringListContent(doc, DataBaseScheme.WT_WINDOW_CLOSE_CONFIG_DATA_TIMERS_TAG_NAME);
                workTimmerSummary.timersDescriptions = timersDescriptions;

                List<Integer> timersCounterValues = getIntegerListContent(doc, DataBaseScheme.WT_WINDOW_CLOSE_CONFIG_DATA_TIMERS_VAL_TAG_NAME);
                workTimmerSummary.timersCounterValues = timersCounterValues;

                int thisDbGlobalDelta = calculateDbGlobalDelta(doc);
                workTimmerSummary.thisDbGlobalDelta = thisDbGlobalDelta;
            }
        } catch (ParserConfigurationException /*| TransformerException*/ e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return workTimmerSummary;
    }

    private static int calculateDbGlobalDelta(Document doc) {
        NodeList daySummaryNodesList = doc.getElementsByTagName(DataBaseScheme.WT_RECORD_TAG_NAME);
        int dayRecordsCount = 0;
        int secondsRegisteresInDays = 0;

        for (int i = 0; i < daySummaryNodesList.getLength(); i++) {
            Node recordNode = daySummaryNodesList.item(i);

            if(!isNewLineNode(recordNode)) {
                dayRecordsCount++;
                NodeList recordContentList = recordNode.getChildNodes();
                
                for (int j = 0; j < recordContentList.getLength(); j++) {
                    Node node = recordContentList.item(j);

                    if(!isNewLineNode(node)) {
                        String nodeName = node.getNodeName();

                        if(DataBaseScheme.DATE_TAG_NAME.equals(nodeName)) {
                            // do date stufs
                        } else if(DataBaseScheme.DAILY_DELTA_TAG_NAME.equals(nodeName)) {
                            // do dayl delta stuffs
                        } else if(DataBaseScheme.GLOBAL_DELTA_TAG_NAME.equals(nodeName)) {
                            // do global delta stuffs
                        } else if(nodeName.startsWith("TIMER_")) {  // TODO tag can have just TIMER name, then can evaluate all at once in all document
                            // do timers stuffs
                            String textContent = node.getTextContent();
                            int seconds = parseTimerToSeconds(textContent);
                            secondsRegisteresInDays += seconds;
                        }
                    }
                }
            }
        }
        
        final int SECONDS_PER_DAY = 8 * 60 * 60;
        final int thisDbDelta = secondsRegisteresInDays - (dayRecordsCount * SECONDS_PER_DAY);

        System.out.println("Days: " + dayRecordsCount + ", should be: " + (dayRecordsCount * SECONDS_PER_DAY) + ", registered: " + secondsRegisteresInDays);
        System.out.println("DeltA: " + thisDbDelta);
        return thisDbDelta;
    }

    private static int parseTimerToSeconds(String textContent) {
//        System.out.println("textContent= " + textContent);
        Pattern pattern = Pattern.compile("([+-]*)(\\d+):(\\d+):(\\d+)");
        Matcher matcher = pattern.matcher(textContent);
        matcher.matches();
        String sign = matcher.group(1);
        String hours = matcher.group(2);
        String minutes = matcher.group(3);
        String seconds = matcher.group(4);
        
        
        int resultInSeconds = Integer.parseInt(seconds) + (60 * Integer.parseInt(minutes)) + (60 * 60 *Integer.parseInt(hours));

//        System.out.println("## "+textContent+" in seconds= "+resultInSeconds+" -> hours: " + hours + ", minutes" + minutes + ", seconds" + seconds);
        
        if("-".equals(sign)) {
            resultInSeconds = -resultInSeconds;
        }

        return resultInSeconds;
    }

    private static int getSingleGlobalIntElementValue(Document doc, String tagName) {
        String stringNodeValue = getSingleGlobalStringElementValue(doc, tagName);
        stringNodeValue = stringNodeValue.isEmpty() ? "0" : stringNodeValue;
        return Integer.parseInt(stringNodeValue);
    }

    private static String getSingleGlobalStringElementValue(Document doc, String tagName) {
        Node glbalDeltaElement = doc.getElementsByTagName(tagName).item(0);
        return glbalDeltaElement.getTextContent();
    }

    
    private static List<String> getStringListContent(Document doc, String tagName) {
        List<String> stringListContent = new ArrayList<String>();
        Node timerDescriptionElement = doc.getElementsByTagName(tagName).item(0);
        NodeList timerDescriptionNodes = timerDescriptionElement.getChildNodes();
        
        for (int i = 0; i < timerDescriptionNodes.getLength(); i++) {
            if(!isNewLineNode(timerDescriptionNodes.item(i))) {
                String textContent = timerDescriptionNodes.item(i).getTextContent();
                stringListContent.add(textContent);
            }
        }
        
        return stringListContent;
    }

    private static List<Integer> getIntegerListContent(Document doc, String tagName) {
        List<Integer> stringListContent = new ArrayList<Integer>();
        Node timerCounterValueElement = doc.getElementsByTagName(tagName).item(0);
        NodeList timerDescriptionNodes = timerCounterValueElement.getChildNodes();

        for (int i = 0; i < timerDescriptionNodes.getLength(); i++) {
            if(!isNewLineNode(timerDescriptionNodes.item(i))) {
                int intContent = Integer.valueOf(timerDescriptionNodes.item(i).getTextContent());
                stringListContent.add(intContent);
            }
        }

        return stringListContent;
    }

    private static boolean isNewLineNode(Node node) {
        return "#text".equals(node.getNodeName());
    }

    static class WorkTimmerSummary{
        int thisDbGlobalDelta;
        int globalDelta;
        int weeklyDelta;
        Integer dayliTimerValue = null;
        List<String> timersDescriptions = new ArrayList<String>();
        List<Integer> timersCounterValues = new ArrayList<Integer>();
    }
}
