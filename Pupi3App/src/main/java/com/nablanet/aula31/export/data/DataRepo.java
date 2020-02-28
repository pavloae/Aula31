package com.nablanet.aula31.export.data;

import com.nablanet.documents.odf.content.spreadsheet.summary.DataSummary;
import com.nablanet.documents.odf.content.spreadsheet.track.DataTrack;

import java.util.List;

public class DataRepo {

    private DataTrack dataTrack;
    private DataSummary dataSummary;
    private List<DataWorkImpl> dataWorkList;

    public DataRepo(DataTrack dataTrack, DataSummary dataSummary, List<DataWorkImpl> dataWorkList) {
        this.dataTrack = dataTrack;
        this.dataSummary = dataSummary;
        this.dataWorkList = dataWorkList;
    }

    public List<DataWorkImpl> getDataWorkList() {
        return dataWorkList;
    }

    public DataTrack getDataTrack() {
        return dataTrack;
    }

    public DataSummary getDataSummary() {
        return dataSummary;
    }

}
