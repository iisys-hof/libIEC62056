package de.iisys.smartgrids.libiec62056.message.content;

/**
 * Password command.
 * The address and unit fields are empty (devoid of any characters).
 * 
 */
public class PasswordCommand extends DataSet {

    /**
     * Initializes {@link DataSet#value} of {@link DataSet} with the password.
     * Also initializes the {@link DataSet#address} and {@link DataSet#unit} of {@link DataSet}
     * with null.
     * @param password the password
     */
    public PasswordCommand(String password) {
        super(null, password, null);
    }

    public String getPassword() {
        return getValue();
    }

}
