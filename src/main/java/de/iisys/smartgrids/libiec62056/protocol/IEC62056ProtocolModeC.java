package de.iisys.smartgrids.libiec62056.protocol;

import de.iisys.libstate.StateGraph;
import de.iisys.libstate.interfaces.State;
import de.iisys.libstate.interfaces.Transition;
import de.iisys.smartgrids.libiec62056.client.IEC62056Client;
import de.iisys.smartgrids.libiec62056.message.CommandMessage;
import de.iisys.smartgrids.libiec62056.message.OptionSelectMessage;
import de.iisys.smartgrids.libiec62056.message.content.CommandMessageIdentifier;
import de.iisys.smartgrids.libiec62056.message.content.ModeControlCharacter;
import java.io.IOException;

/**
 * Protocol mode C supports bidirectional data exchange with baud rate switching and permits
 * data readout, programming with enhanced security and manufacturer-specific modes.
 * 
 */
public class IEC62056ProtocolModeC extends IEC62056ProtocolModeB {

    protected final String MANUFACTURER_SPECIFIC_MESSAGE = "manufacturerSpecificMessage";

    private boolean enterProgrammingMode;

    public IEC62056ProtocolModeC(IEC62056Client client) {
        this(client, false);
    }

     /**
     * Initializes {@link IEC62056ProtocolModeB#IEC62056ProtocolModeB(de.iisys.smartgrids.libiec62056.client.IEC62056Client) }
     * with the given client and sets whether the programming mode should be entered or not.
     * @param client the client
     * @param enterProgrammingMode the programming mode
     */
    public IEC62056ProtocolModeC(IEC62056Client client, boolean enterProgrammingMode) {
        super(client);
        setEnterProgrammingMode(enterProgrammingMode);

    }

    /**
     * Registers the states with the given protocol.
     * @param protocol the protocol
     */
    @Override
    protected void registerStates(StateGraph protocol) {
        super.registerStates(protocol);

        protocol.registerState(MANUFACTURER_SPECIFIC_MESSAGE);
    }

    /**
     * Register the transition with the given protocol.
     * @param protocol the protocol
     */
    @Override
    protected void registerTransitions(StateGraph protocol) {
        super.registerTransitions(protocol);

        protocol.deregisterTransitions(IDENTIFICATION_RECEIVE, DATA_RECEIVE);
        protocol.deregisterTransitions(DATA_RECEIVE, PROGRAMMING_COMMAND_MESSAGE);

        protocol.registerTransition(IDENTIFICATION_RECEIVE, OPTION_SELECT_MESSAGE, super::adaptIdentification);

        protocol.registerTransition(OPTION_SELECT_MESSAGE, DATA_RECEIVE, (transition) -> {
            OptionSelectMessage optionSelectMessage = getPreparedMessage(transition.getSource());
            return optionSelectMessage.getModeControlCharacter() == ModeControlCharacter.DATA_READOUT;
        }, this::adaptOptionSelectBaudRate);
        protocol.registerTransition(OPTION_SELECT_MESSAGE, PROGRAMMING_COMMAND_RECEIVE, (transition) -> {
            OptionSelectMessage optionSelectMessage = getPreparedMessage(transition.getSource());
            return optionSelectMessage.getModeControlCharacter() == ModeControlCharacter.PROGRAMMING_MODE;
        }, this::adaptOptionSelectBaudRate);
        protocol.registerTransition(OPTION_SELECT_MESSAGE, MANUFACTURER_SPECIFIC_MESSAGE, (transition) -> {
            OptionSelectMessage optionSelectMessage = getPreparedMessage(transition.getSource());
            return optionSelectMessage.getModeControlCharacter() != ModeControlCharacter.DATA_READOUT
                    && optionSelectMessage.getModeControlCharacter() != ModeControlCharacter.PROGRAMMING_MODE;
        }, this::adaptOptionSelectBaudRate);

        protocol.registerTransition(PROGRAMMING_COMMAND_RECEIVE, PROGRAMMING_COMMAND_MESSAGE, (transition) -> {
            CommandMessage commandMessage = getReceivedMessage(transition.getSource());
            return commandMessage.getCommandMessageIdentifier() == CommandMessageIdentifier.PASSWORD_COMMAND;
        });

        // deregister and override in extended classes for real usage
        protocol.registerTransition(MANUFACTURER_SPECIFIC_MESSAGE, END);
    }

    /**
     * Registers the suppliers for {@link OptionSelectMessage}.
     */
    @Override
    protected void registerSuppliers() {
        super.registerSuppliers();
        supplyFor(OptionSelectMessage.class, () -> new OptionSelectMessage(getClient().getBaudRate(), isEnterProgrammingMode() ? ModeControlCharacter.PROGRAMMING_MODE : ModeControlCharacter.DATA_READOUT));
    }

    /**
     * Sets whether the programming mode should be entered or not.
     * @param enterProgrammingMode enters the programming mode or not
     */
    public void setEnterProgrammingMode(boolean enterProgrammingMode) {
        this.enterProgrammingMode = enterProgrammingMode;
    }

    public boolean isEnterProgrammingMode() {
        return enterProgrammingMode;
    }

    /**
     * Adapts the identification by setting the baud rate by getting the baud rate of
     * the identification message and getting the transition source.
     * 
     * @param transition the transition
     * @throws IOException
     */
    protected void adaptOptionSelectBaudRate(Transition<State, State> transition) throws IOException {
        OptionSelectMessage message = getPreparedMessage(transition.getSource());
        getClient().setBaudRate(message.getBaudRate());
    }
}
