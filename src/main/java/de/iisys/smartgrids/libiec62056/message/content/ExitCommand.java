package de.iisys.smartgrids.libiec62056.message.content;

/**
 * No data set is required when the command type identifier is 0.
 * 
 */
public class ExitCommand extends DataSet {

    /**
     * Initializes {@link DataSet#address} and {@link DataSet#value} and {@link DataSet#unit} 
     * of {@link DataSet} with null.
     */
    public ExitCommand() {
        super(null, null, null);
    }

}
