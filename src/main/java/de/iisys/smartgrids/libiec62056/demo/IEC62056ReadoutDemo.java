package de.iisys.smartgrids.libiec62056.demo;

import de.iisys.smartgrids.libiec62056.client.IEC62056Client;
import de.iisys.smartgrids.libiec62056.message.DataMessage;
import de.iisys.smartgrids.libiec62056.message.IdentificationMessage;
import de.iisys.smartgrids.libiec62056.message.content.DataBlock;
import de.iisys.smartgrids.libiec62056.protocol.IEC62056ProtocolModeC;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reads and prints out the data message set.
 * 
 */
public class IEC62056ReadoutDemo {

    public static void main(String[] args) throws IOException, InterruptedException {
        //Creates new logger
        Logger logger = Logger.getLogger(IEC62056Client.class.getName());
        //Creates new handler
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.FINE);
        logger.setLevel(Level.ALL);
        logger.addHandler(handler);

        //Creates a network client
        try (IEC62056Client iec62056Client = IEC62056Client.createNetworkClient("10.70.7.10", 8000)) {
            //sets the maximum time out
            iec62056Client.setMaxTimeout(5000);
            //Protocol mode C
            IEC62056ProtocolModeC iec62056ProtocolModeC = new IEC62056ProtocolModeC(iec62056Client);

            //prints the identification
            iec62056ProtocolModeC.listenTo(IdentificationMessage.class, (identification) -> {
                System.out.println();
                System.out.println("Manufacturer Identification: " + identification.getManufacturerIdentification());
                System.out.println("Identification: " + identification.getIdentification());
                System.out.println("Protocol Mode: " + identification.getProtocolMode());
                System.out.println("Baud Rate: " + identification.getBaudRate());
                System.out.println();
            });

            //Gets the data block to read out
            iec62056ProtocolModeC.listenTo(DataMessage.class, (dataReadout) -> {
                DataBlock dataBlock = dataReadout.getDataBlock();

                System.out.println();
                //Prints the data block
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
