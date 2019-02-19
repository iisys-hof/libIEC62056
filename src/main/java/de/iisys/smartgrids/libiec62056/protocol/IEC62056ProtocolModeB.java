package de.iisys.smartgrids.libiec62056.protocol;

import de.iisys.libstate.StateGraph;
import de.iisys.libstate.interfaces.State;
import de.iisys.libstate.interfaces.Transition;
import de.iisys.smartgrids.libiec62056.client.IEC62056Client;
import de.iisys.smartgrids.libiec62056.message.IdentificationMessage;
import de.iisys.smartgrids.libiec62056.message.content.BaudRate;
import java.io.IOException;

/**
 * Protocol mode B supports bidirectional data exchange with baud rate switching. This protocol
 * mode permits data readout and programming with optional password protection.
 * 
 */
public class IEC62056ProtocolModeB extends IEC62056ProtocolModeA {

    /**
     * Initializes {@link IEC62056ProtocolModeA#IEC62056ProtocolModeA(de.iisys.smartgrids.libiec62056.client.IEC62056Client)
     * with the given client.
     * @param client the client
     */
    public IEC62056ProtocolModeB(IEC62056Client client) {
        super(client);
    }

    /**
     * Registers the transistion with the given protocol.
     * @param protocol the protocol.
     */
    @Override
    protected void registerTransitions(StateGraph protocol) {
        super.registerTransitions(protocol);

        protocol.overrideTransition(DATA_RECEIVE, PROGRAMMING_COMMAND_MESSAGE, this::resetBaudRateToDefault);
    }
    /**
     * Adapts the identification by setting the baud rate by getting the baud rate of
     * the identification message and getting the transition source.
     * 
     * @param transition the transition
     * @throws IOException
     */
    
    @Override
    protected void adaptIdentification(Transition<State, State> transition) throws IOException {
        super.adaptIdentification(transition);

        IdentificationMessage identificationMessage = getReceivedMessage(transition.getSource());
        getClient().setBaudRate(identificationMessage.getBaudRate());
    }

    /**
     * Sets the baud rate to 300.
     * @param transition the transition
     * @throws IOException 
     */
    protected void resetBaudRateToDefault(Transition<State, State> transition) throws IOException {
        getClient().setBaudRate(BaudRate.BAUD_RATE_300);
    }

}
