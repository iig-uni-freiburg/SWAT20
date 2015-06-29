package de.uni.freiburg.iig.telematik.swat.logs;

import de.uni.freiburg.iig.telematik.sewol.format.MXMLLogFormat;
import de.uni.freiburg.iig.telematik.sewol.format.XESLogFormat;

public enum SwatLogType {

    MXML(MXMLLogFormat.MXML_EXTENSION, "MXML Log"), XES(XESLogFormat.XES_EXTENSION, "XES Log"), Aristaflow("csv", "Aristaflow Log");

    private String fileEnding = null;
    private String description = null;

    private SwatLogType(String fileEnding, String description) {
        this.fileEnding = fileEnding;
        this.description = description;
    }
    
    public String getFileEnding(){
        return fileEnding;
    }
    
    public String getDescription(){
        return description;
    }

}
