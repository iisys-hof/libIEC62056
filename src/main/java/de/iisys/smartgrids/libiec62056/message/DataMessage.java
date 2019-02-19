package de.iisys.smartgrids.libiec62056.message;

import de.iisys.libinterface.message.annotation.MessageTemplate;
import de.iisys.libinterface.message.interfaces.Message;
import de.iisys.smartgrids.libiec62056.message.content.DataBlock;
import de.iisys.smartgrids.libiec62056.message.content.DataSet;
import java.util.List;
import de.iisys.libinterface.message.annotation.Callback;

/**
 * Normal response of a tariff device, for example the full data set.
 * 
 */
@MessageTemplate("<STX>~([{addresses}]\\([{values}][*{units}]\\):<CR><LF>)\\!<CR><LF><ETX>:bcc~!bcc!")
@Callback("processDataBlock")
public class DataMessage implements Message {

    private DataBlock dataBlock;

    private List<String> addresses;
    private List<String> values;
    private List<String> units;

    /**
     * Processes the data block.<br>
     * Creates a new data block if {@link #addresses}, {@link #values} and {@link units}
     * are not null and fills the data block with those variables.
     */
    protected void processDataBlock() {
        if (addresses != null && values != null && units != null) {
            dataBlock = new DataBlock();
            for (int i = 0; i < addresses.size(); i++) {
                dataBlock.getDataSets().add(new DataSet(addresses.get(i), values.get(i), i < units.size() ? units.get(i) : null));
            }
        }
    }

    /**
     * Returns the data block.
     * @return data block
     */
    public DataBlock getDataBlock() {
        return dataBlock;
    }

}
