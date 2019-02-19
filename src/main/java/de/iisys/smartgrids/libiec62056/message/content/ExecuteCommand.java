package de.iisys.smartgrids.libiec62056.message.content;

/**
 * The execute command requests that a device executes a predefined function.
 * 
 */
public class ExecuteCommand extends DataSet {

    /**
     * Initializes {@link DataSet#address} and {@link DataSet#value} of {@link DataSet}
     * with the given function and data.
     * @param function the function
     * @param data the data
     */
    public ExecuteCommand(String function, String data) {
        super(function, data, null);
    }

    public String getFunction() {
        return getAddress();
    }

    public String getData() {
        return getValue();
    }

}
