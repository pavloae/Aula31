package com.nablanet.aula31.domain;

import java.net.URL;
import java.text.DecimalFormat;

public class ResultUploadFile {

    private static DecimalFormat df = new DecimalFormat("0.00");

    public long bytesTransferred;
    public long totalByteCount;

    public boolean complete;
    public URL url;

    public ResultUploadFile(long bytesTransferred, long totalByteCount) {
        this.bytesTransferred = bytesTransferred;
        this.totalByteCount = totalByteCount;
    }

    public ResultUploadFile(boolean complete, URL url) {
        this.complete = complete;
        this.url = url;
    }

    public float getFloatProgress() {
        if (totalByteCount == 0 || bytesTransferred == 0)
            return 0;
        return (1.0f * bytesTransferred / totalByteCount);
    }

    public float getPercentProgress() {
        return Math.round(getFloatProgress() * 100.0) / 100.0f;
    }

    public Integer getIntPercentProgress() {
        return (int) getPercentProgress();
    }


    public String getStringProgress() {
        return String.format("%s %%", df.format(getPercentProgress()));
    }

}
