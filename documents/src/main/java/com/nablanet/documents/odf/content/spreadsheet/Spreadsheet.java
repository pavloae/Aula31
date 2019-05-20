package com.nablanet.documents.odf.content.spreadsheet;

import com.nablanet.documents.odf.content.BaseElement;
import com.nablanet.documents.odf.content.spreadsheet.summary.DataSummary;
import com.nablanet.documents.odf.content.spreadsheet.summary.SummarySheet;
import com.nablanet.documents.odf.content.spreadsheet.track.DataTrack;
import com.nablanet.documents.odf.content.spreadsheet.track.TrackSheet;
import com.nablanet.documents.odf.content.spreadsheet.work.DataWork;
import com.nablanet.documents.odf.content.spreadsheet.work.WorkSheet;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

public class Spreadsheet extends BaseElement {

    public SummarySheet summarySheetModel;
    public TrackSheet trackSheetModel;
    public WorkSheet workSheetModel;

    public Spreadsheet(Element element) {
        super(element);
        summarySheetModel = getSummarySheetModel();
        trackSheetModel = getTrackSheetModel();
        workSheetModel = getWorkSheetModel();
    }

    public void setData(DataSummary dataSummary, DataTrack dataTrack, List<DataWork> dataWorks) {

        if (dataSummary != null)
            summarySheetModel.setData(dataSummary);
        else
            removeChild(summarySheetModel);

        if (dataTrack != null)
            trackSheetModel.setData(dataTrack);
        else
            removeChild(summarySheetModel);

        if (dataWorks != null && !dataWorks.isEmpty()) {
            WorkSheet workSheet;
            for (DataWork dataWork : dataWorks) {
                workSheet = new WorkSheet(workSheetModel.clone(true));
                workSheet.setData(dataWork);
                insertBefore(workSheet, workSheetModel);
            }
        }
        removeChild(workSheetModel);
    }

    private SummarySheet getSummarySheetModel() {
        try {
            Node node = (Node) getXPath().evaluate(
                    "./table:table[1]",
                    getElement(), XPathConstants.NODE
            );
            return new SummarySheet((Element) node);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private TrackSheet getTrackSheetModel() {
        try {
            Node node = (Node) getXPath().evaluate(
                    "./table:table[2]",
                    getElement(), XPathConstants.NODE
            );
            return new TrackSheet((Element) node);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private WorkSheet getWorkSheetModel() {
        try {
            Element element = getElement();
            Node node = (Node) getXPath().evaluate(
                    "./table:table[3]",
                    element, XPathConstants.NODE
            );
            return new WorkSheet((Element) node);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
