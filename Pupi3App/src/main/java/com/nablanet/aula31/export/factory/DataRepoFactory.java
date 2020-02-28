package com.nablanet.aula31.export.factory;

import com.nablanet.aula31.export.data.DataRepo;
import com.nablanet.aula31.export.data.DataWorkImpl;
import com.nablanet.documents.odf.content.spreadsheet.summary.DataSummary;
import com.nablanet.documents.odf.content.spreadsheet.track.DataTrack;

import java.util.List;

public class DataRepoFactory {

    private List<DataWorkImpl> dataWorkList;
    private DataTrack dataTrack;
    private DataSummary dataSummary;

    private boolean dataSumaryCompleted, dataTrackCompleted, dataWorkListCompleted;

    public void setDataSummary(DataSummary dataSummary) {
        this.dataSummary = dataSummary;
        dataSumaryCompleted = true;
    }

    public void setDataTrack(DataTrack dataTrack) {
        this.dataTrack = dataTrack;
        dataTrackCompleted = true;
    }

    public void setDataWorkList(List<DataWorkImpl> dataWorkList) {
        this.dataWorkList = dataWorkList;
        dataWorkListCompleted = true;
    }

    public boolean isCompleted() {
        return dataSumaryCompleted && dataTrackCompleted && dataWorkListCompleted;
    }

    public DataRepo getDataRepo() {
        return new DataRepo(dataTrack, dataSummary, dataWorkList);
    }

}
