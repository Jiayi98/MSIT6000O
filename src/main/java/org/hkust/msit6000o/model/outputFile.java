package org.hkust.msit6000o.model;

// file object, may not need
public class outputFile {
    private final String fileName;
    private final String fileURL;

    public outputFile(String fileName, String URL) {
        this.fileName = fileName;
        this.fileURL = URL;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileURL() {
        return fileURL;
    }

}
