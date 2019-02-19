package de.iisys.smartgrids.libiec62056.message.content;

/**
 * Where the value represents a data string, the address is the start location to which the
 * data is to be written. The unit field is left empty.
 * 
 */
public class WriteCommand extends DataSet {

    /**
     * Initializes {@link DataSet#address} and {@link DataSet#value}
     * of {@link DataSet} with the given starting location and data.
     * Also initializes {@link DataSet#unit} with null.
     * @param startLocation the starting location
     * @param data the data
     */
    public WriteCommand(String startLocation, String data) {
        super(startLocation, data, null);
    }

    public String getStartLocation() {
        return getAddress();
    }

    public String getData() {
        return getValue();
    }

}
