package de.iisys.smartgrids.libiec62056.message.content;

/**
 * Where a data string is to be read, the address is the start location from which data is
 * read.
 * The value represents the number of locations to be read including the start location.
 * The unit field is left empty.
 */
public class ReadCommand extends DataSet {
/**
 * Initializes {@link DataSet#address} and {@link DataSet#value}
 * of {@link DataSet} with the given starting location and to read location.
 * Also initializes {@link DataSet#unit} with null.
 * @param startLocation the starting location
 * @param toReadLocations to read location
 */
    public ReadCommand(String startLocation, String toReadLocations) {
        super(startLocation, toReadLocations, null);
    }

    public String getStartLocation() {
        return getAddress();
    }

    public String getToReadLocations() {
        return getValue();
    }

}
