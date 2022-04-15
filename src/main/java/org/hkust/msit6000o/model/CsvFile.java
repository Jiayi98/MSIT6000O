package org.hkust.msit6000o.model;

// file object, may not need
public class CsvFile {
    private final String fileName;
    private String fileURL;

    public CsvFile(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }
}
