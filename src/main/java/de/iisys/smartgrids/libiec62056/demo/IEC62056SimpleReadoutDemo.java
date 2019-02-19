package de.iisys.smartgrids.libiec62056.demo;

import de.iisys.smartgrids.libiec62056.client.IEC62056Client;
import de.iisys.smartgrids.libiec62056.message.DataMessage;
import de.iisys.smartgrids.libiec62056.message.content.DataBlock;
import de.iisys.smartgrids.libiec62056.protocol.IEC62056ProtocolModeC;
import java.io.IOException;

/**
 * Simpler version to read and print out the data message set.
 * 
 */
public class IEC62056SimpleReadoutDemo {

    public static void main(String[] args) throws IOException, InterruptedException {
        //Creates network client
        try (IEC62056Client iec62056Client = IEC62056Client.createNetworkClient("10.70.7.10", 8000)) {
            //Protocol mode C
            IEC62056ProtocolModeC iec62056ProtocolModeC = new IEC62056ProtocolModeC(iec62056Client);

            //Gets data block to read out
            iec62056ProtocolModeC.listenTo(DataMessage.class, (dataReadout) -> {
                DataBlock dataBlock = dataReadout.getDataBlock();

                System.out.println("Readout data:\n\nOBIS: Value Unit");
                //Prints data block
                for (int i = 0; i < dataBlock.getDataSets().size(); i++) {
                    System.out.println(dataBlock.getDataSets().get(i));
                }
                System.out.println();
            });
            //Runs protocol mode C
            iec62056ProtocolModeC.runProtocol();
        }
    }

}
