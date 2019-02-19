package de.iisys.smartgrids.libiec62056.message.content;

/**
 * A data set contains, in general, an identification number or address, the value, the unit
 * and various boundary characters. A data line should be not longer than 78 characters*
 * including all boundary, separating and control characters. The sequence of the data sets or
 * data lines is not fixed.
 * 
 */
public class DataSet {

    private String address;
    private String value;
    private String unit;

    /**
     * Initializes {@link #address}, {@link #value} and {@link #unit} with the given
     * adress, value and unit.
     * @param address the adress
     * @param value the value
     * @param unit the unit
     */
    public DataSet(String address, String value, String unit) {
        this.address = address;
        this.value = value;
        this.unit = unit;
    }

    public String getAddress() {
        return address;
    }

    public String getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return address + ": " + value + (unit != null && unit.length() > 0 ? " " + unit : "");
    }

}
