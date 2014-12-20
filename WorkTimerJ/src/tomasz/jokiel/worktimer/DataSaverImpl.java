package tomasz.jokiel.worktimer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.SAXException;

import tomasz.jokiel.worktimer.DataReader.WorkTimmerSummary;

public class DataSaverImpl implements DataSaver {

    @Override
    public void storeReleasedTimersValues(boolean isEndOfWeek, boolean isEndOfMonth, 
                                          ArrayList<DayliTimerTaskGroup> dayliTimerTaskGroupContainerList, 
                                          DayliTimerTaskGroup sumatrDayliTimerTaskGroupContainer) {

        WorkTimmerSummary workTimmerSummary = DataReader.readDataBase();

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc;

            File dataBaseFile = new File(DataBaseScheme.DATA_BASE_FILE_NAME);

            Element WT_SUMMARY = null;

            if(dataBaseFile.exists()) {
                System.out.println("Updating");

                doc = docBuilder.parse(dataBaseFile);

                WT_SUMMARY = (Element) doc.getElementsByTagName(DataBaseScheme.WT_SUMMARY_TAG_NAME).item(0);
            } else {
                System.out.println("New file");
                doc = docBuilder.newDocument();

                ProcessingInstruction processingInstruction = doc.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"WT_style.xsl\"");
                doc.appendChild(processingInstruction);

                WT_SUMMARY = doc.createElement(DataBaseScheme.WT_SUMMARY_TAG_NAME);
                doc.appendChild(WT_SUMMARY);

                appendWtListingTag(doc, WT_SUMMARY);
            }

            workTimmerSummary.globalDelta += sumatrDayliTimerTaskGroupContainer.getDayliTimer().getCounterValue();
            workTimmerSummary.weeklyDelta += sumatrDayliTimerTaskGroupContainer.getDayliTimer().getCounterValue();
            
            appendDaySummaryTag(doc, WT_SUMMARY, dayliTimerTaskGroupContainerList, sumatrDayliTimerTaskGroupContainer, workTimmerSummary);

            if(isEndOfWeek) {
                appendEndOfWeekTag(doc, WT_SUMMARY, workTimmerSummary);
            }

            if(isEndOfMonth) {
                workTimmerSummary.thisDbGlobalDelta += sumatrDayliTimerTaskGroupContainer.getDayliTimer().getCounterValue();
                appendEndOfMonthTag(doc, WT_SUMMARY, workTimmerSummary);
            }

            appendFooter(doc, WT_SUMMARY);
            appendConfigData(doc, WT_SUMMARY, isEndOfWeek, dayliTimerTaskGroupContainerList, workTimmerSummary);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(dataBaseFile);
            transformer.transform(source, result);
     
            System.out.println("File saved!");

        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void appendEndOfMonthTag(Document doc, Element WT_SUMMARY, WorkTimmerSummary workTimmerSummary) {
        Element WT_MONTH_SUMMARY = doc.createElement(DataBaseScheme.WT_MONTH_SUMMARY_TAG_NAME);
        WT_SUMMARY.appendChild(WT_MONTH_SUMMARY);
        
        Element WT_MONTH_SUMMARY_RECORD = doc.createElement(DataBaseScheme.WT_MONTH_SUMMARY_RECORD_TAG_NAME);
        WT_MONTH_SUMMARY.appendChild(WT_MONTH_SUMMARY_RECORD);
        

        Element WT_MONTH_SUMMARY_DELTA_HOURS = doc.createElement(DataBaseScheme.WT_MONTH_SUMMARY_DELTA_HOURS_TAG_NAME);
        WT_MONTH_SUMMARY_DELTA_HOURS.appendChild(doc.createTextNode(Utils.formatTimerFromSeconds(workTimmerSummary.thisDbGlobalDelta)));
        WT_MONTH_SUMMARY_RECORD.appendChild(WT_MONTH_SUMMARY_DELTA_HOURS);
    }

    private void appendEndOfWeekTag(Document doc, Element WT_SUMMARY, WorkTimmerSummary workTimmerSummary) {
        Element WT_WEEK_SUMMARY = doc.createElement(DataBaseScheme.WT_WEEK_SUMMARY_TAG_NAME);
        WT_SUMMARY.appendChild(WT_WEEK_SUMMARY);
        
        Element WT_WEEK_SUMMARY_RECORD = doc.createElement(DataBaseScheme.WT_WEEK_SUMMARY_RECORD_TAG_NAME);
        WT_WEEK_SUMMARY.appendChild(WT_WEEK_SUMMARY_RECORD);
        
        Element WT_WEEK_SUMMARY_GLOBAL_DELTA = doc.createElement(DataBaseScheme.WT_WEEK_SUMMARY_GLOBAL_DELTA_TAG_NAME);
        WT_WEEK_SUMMARY_GLOBAL_DELTA.appendChild(doc.createTextNode(Utils.formatTimerFromSeconds(workTimmerSummary.globalDelta)));
        WT_WEEK_SUMMARY_RECORD.appendChild(WT_WEEK_SUMMARY_GLOBAL_DELTA);

        Element WT_WEEK_SUMMARY_WEEKLY_DELTA = doc.createElement(DataBaseScheme.WT_WEEK_SUMMARY_WEEKLY_DELTA_TAG_NAME);
        WT_WEEK_SUMMARY_WEEKLY_DELTA.appendChild(doc.createTextNode(Utils.formatTimerFromSeconds(workTimmerSummary.weeklyDelta)));
        WT_WEEK_SUMMARY_RECORD.appendChild(WT_WEEK_SUMMARY_WEEKLY_DELTA);
    }
    
    private void appendFooter(Document doc, Element WT_SUMMARY) {
        Node oldFooter = doc.getElementsByTagName(DataBaseScheme.WT_FOOTER_TAG_NAME).item(0);
        
        if(oldFooter != null) {
            WT_SUMMARY.removeChild(oldFooter);
        }
        
        Element WT_FOOTER = doc.createElement(DataBaseScheme.WT_FOOTER_TAG_NAME);
        WT_FOOTER.appendChild(doc.createTextNode("File Generated By WorkTimerJ  v0.0.x - " + (new Date())));
        WT_SUMMARY.appendChild(WT_FOOTER);
    }

    private void appendConfigData(Document doc, Element WT_SUMMARY, boolean isEndOfWeek,
                                  ArrayList<DayliTimerTaskGroup> dayliTimerTaskGroupContainerList,
                                  WorkTimmerSummary workTimmerSummary) {
        Node oldConfigData = doc.getElementsByTagName(DataBaseScheme.WT_CONFIG_DATA_TAG_NAME).item(0);

        if(oldConfigData != null) {
            // read old data
            WT_SUMMARY.removeChild(oldConfigData);
        }

        Element WT_CONFIG_DATA = doc.createElement(DataBaseScheme.WT_CONFIG_DATA_TAG_NAME);
        WT_SUMMARY.insertBefore(WT_CONFIG_DATA, doc.getElementsByTagName(DataBaseScheme.WT_LISTING_TAG_NAME).item(0));
        
        Element WT_CONFIG_DATA_GLOBAL_DELTA = doc.createElement(DataBaseScheme.WT_CONFIG_DATA_GLOBAL_DELTA_TAG_NAME);
        WT_CONFIG_DATA_GLOBAL_DELTA.appendChild(doc.createTextNode(String.valueOf(workTimmerSummary.globalDelta)));
        WT_CONFIG_DATA.appendChild(WT_CONFIG_DATA_GLOBAL_DELTA);

        Element WT_CONFIG_DATA_WEEK_DELTA = doc.createElement(DataBaseScheme.WT_CONFIG_DATA_WEEK_DELTA_TAG_NAME);
        WT_CONFIG_DATA_WEEK_DELTA.appendChild(doc.createTextNode(isEndOfWeek ? "0" : String.valueOf(workTimmerSummary.weeklyDelta)));
        WT_CONFIG_DATA.appendChild(WT_CONFIG_DATA_WEEK_DELTA);

        Element WT_CONFIG_DATA_TIMERS = doc.createElement(DataBaseScheme.WT_CONFIG_DATA_TIMERS_TAG_NAME);
        WT_CONFIG_DATA.appendChild(WT_CONFIG_DATA_TIMERS);

        for(int i = 1; i <= dayliTimerTaskGroupContainerList.size(); i++) {
            Element H_TIMER_x = doc.createElement("H_TIMER_" + i);
            String timerDescription = dayliTimerTaskGroupContainerList.get(i-1).getTaskDescriptionJTextField().getText();
            H_TIMER_x.appendChild(doc.createTextNode(timerDescription));
            WT_CONFIG_DATA_TIMERS.appendChild(H_TIMER_x);
        }

    }

    private void appendWtListingTag(Document doc, Element WT_SUMMARY) {
        Element WT_LISTING = doc.createElement(DataBaseScheme.WT_LISTING_TAG_NAME);
        WT_SUMMARY.appendChild(WT_LISTING);
    }

    private void appendDaySummaryTag(Document doc, Element rootElement, 
                                     ArrayList<DayliTimerTaskGroup> dayliTimerTaskGroupContainerList, 
                                     DayliTimerTaskGroup sumatrDayliTimerTaskGroupContainer, 
                                     WorkTimmerSummary workTimmerSummary) {
        Element WT_DAY_SUMMARY = doc.createElement(DataBaseScheme.WT_DAY_SUMMARY_TAG_NAME);
        rootElement.appendChild(WT_DAY_SUMMARY);

        Element WT_HEADER = doc.createElement(DataBaseScheme.WT_HEADER_TAG_NAME);
        WT_DAY_SUMMARY.appendChild(WT_HEADER);

        for(int i = 1; i <= dayliTimerTaskGroupContainerList.size(); i++) {
            Element H_TIMER_x = doc.createElement("H_TIMER_" + i); // TODO tag can have just H_TIMER name, then can evaluate all at once in all document
            String timerDescription = dayliTimerTaskGroupContainerList.get(i-1).getTaskDescriptionJTextField().getText();
            timerDescription = dayliTimerTaskGroupContainerList.get(i-1).getDayliTimer().getCounterValue() == 0 ? "" : timerDescription;
            H_TIMER_x.appendChild(doc.createTextNode(timerDescription));
            WT_HEADER.appendChild(H_TIMER_x);
        }

        Element WT_RECORD = doc.createElement(DataBaseScheme.WT_RECORD_TAG_NAME);
        WT_DAY_SUMMARY.appendChild(WT_RECORD);
        
        Element DATE = doc.createElement(DataBaseScheme.DATE_TAG_NAME);
        DATE.appendChild(doc.createTextNode(Utils.getCurrentDateFormatted()));
        WT_RECORD.appendChild(DATE);
        
        Element DAILY_DELTA = doc.createElement(DataBaseScheme.DAILY_DELTA_TAG_NAME);
        String dayliTimerDescription = sumatrDayliTimerTaskGroupContainer.getDayliTimerValueJTextPane().getText();
        DAILY_DELTA.appendChild(doc.createTextNode(dayliTimerDescription));
        WT_RECORD.appendChild(DAILY_DELTA);

        Element GLOBAL_DELTA = doc.createElement(DataBaseScheme.GLOBAL_DELTA_TAG_NAME);
        GLOBAL_DELTA.appendChild(doc.createTextNode(Utils.formatTimerFromSeconds(workTimmerSummary.globalDelta)));
        WT_RECORD.appendChild(GLOBAL_DELTA);

        for(int i = 1; i <= dayliTimerTaskGroupContainerList.size(); i++) {
            Element TIMER_x = doc.createElement("TIMER_" + i); // TODO tag can have just TIMER name, then can evaluate all at once in all document
            String timerValue = dayliTimerTaskGroupContainerList.get(i-1).getDayliTimerValueJTextPane().getText();
            TIMER_x.appendChild(doc.createTextNode(timerValue));
            WT_RECORD.appendChild(TIMER_x);
        }
    }

}
