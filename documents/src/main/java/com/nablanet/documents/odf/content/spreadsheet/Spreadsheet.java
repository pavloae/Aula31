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
    }

    public void setData(DataSummary dataSummary, DataTrack dataTrack, List<? extends DataWork> dataWorks) throws XPathExpressionException {

        summarySheetModel = getSummarySheetModel();
        trackSheetModel = getTrackSheetModel();
        workSheetModel = getWorkSheetModel();

        // Si no hay datos devolvemos el template solo como está
        if (dataSummary == null && dataTrack == null && (dataWorks == null || dataWorks.isEmpty()))
            return;

        if (dataSummary != null)
            getSummarySheetModel().setData(dataSummary);
        else
            removeChild(getSummarySheetModel());

        if (dataTrack != null)
            getTrackSheetModel().setData(dataTrack);
        else
            removeChild(getTrackSheetModel());

        if (dataWorks != null && !dataWorks.isEmpty()) {
            WorkSheet workSheet;
            for (DataWork dataWork : dataWorks) {
                workSheet = new WorkSheet(getWorkSheetModel().clone(true));
                workSheet.setData(dataWork);
                insertBefore(workSheet, getWorkSheetModel());
            }
        }
        removeChild(getWorkSheetModel());

    }

    private SummarySheet getSummarySheetModel() throws XPathExpressionException {
        if (summarySheetModel != null) return summarySheetModel;
        Node node = (Node) getXPath().evaluate(
                "./table:table[1]",
                getElement(), XPathConstants.NODE
        );
        return new SummarySheet((Element) node);
    }

    private TrackSheet getTrackSheetModel() throws XPathExpressionException {
        if (trackSheetModel != null) return trackSheetModel;
        Node node = (Node) getXPath().evaluate(
                "./table:table[2]",
                getElement(), XPathConstants.NODE
        );
        return new TrackSheet((Element) node);
    }

    private WorkSheet getWorkSheetModel() throws XPathExpressionException {
        if (workSheetModel != null) return workSheetModel;
        Element element = getElement();
        Node node = (Node) getXPath().evaluate(
                "./table:table[3]",
                element, XPathConstants.NODE
        );
        return new WorkSheet((Element) node);
    }

}
