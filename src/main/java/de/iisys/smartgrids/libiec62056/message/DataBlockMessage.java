package de.iisys.smartgrids.libiec62056.message;

import de.iisys.libinterface.message.annotation.MessageTemplate;
import de.iisys.libinterface.message.interfaces.Message;
import de.iisys.smartgrids.libiec62056.message.content.DataSet;
import de.iisys.libinterface.message.annotation.Callback;

/**
 * A data block consists of a sequence of data lines separated by the characters CR, carriage
 * return, code ASCII 0DH and LF, line feed, code 0AH. A data line consists of one or more data
 * sets. A data set contains, in general, an identification number or address, the value, the unit
 * and various boundary characters. A data line should be not longer than 78 characters*
 * including all boundary, separating and control characters. The sequence of the data sets or
 * data lines is not fixed.
 * 
 * Block messages are used in conformance with the protocol selected.
 * 
 */
@MessageTemplate("<STX>~[{address}]\\([{value}][*{unit}]\\)<ETX>:bcc~!bcc!")
@Callback("processDataSet")
public class DataBlockMessage implements Message {

    private DataSet dataSet;

    private String address;
    private String value;
    private String unit;

    /**
     * Processes the data set including the adress, value and unit.
     */
    protected void processDataSet() {
        dataSet = new DataSet(address, value, unit);
    }

    public DataSet getDataSet() {
        return dataSet;
    }

}
