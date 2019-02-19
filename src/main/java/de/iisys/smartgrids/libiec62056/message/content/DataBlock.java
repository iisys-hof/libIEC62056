package de.iisys.smartgrids.libiec62056.message.content;

import java.util.ArrayList;
import java.util.List;

/**
 * A data block consists of a sequence of data lines separated by the characters CR, carriage
 * return, code ASCII 0DH and LF, line feed, code 0AH. A data line consists of one or more data
 * sets.
 * 
 */
public class DataBlock {
    
    private List<DataSet> dataSets;
    
    /**
     * Creates a new array list of data sets
     */
    public DataBlock() {
        dataSets = new ArrayList<>();
    }

    public List<DataSet> getDataSets() {
        return dataSets;
    }

}
