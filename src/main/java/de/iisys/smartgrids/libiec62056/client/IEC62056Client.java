package de.iisys.smartgrids.libiec62056.client;

import de.iisys.libinterface.parser.service.MessageCharacters;
import de.iisys.libinterface.message.interfaces.Message;
import de.iisys.libinterface.parser.service.MessageParserService;
import de.iisys.smartgrids.libiec62056.message.content.BaudRate;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openmuc.jrxtx.DataBits;
import org.openmuc.jrxtx.FlowControl;
import org.openmuc.jrxtx.Parity;
import org.openmuc.jrxtx.SerialPort;
import org.openmuc.jrxtx.SerialPortBuilder;
import org.openmuc.jrxtx.StopBits;
import org.parboiled.errors.ParsingException;

/**
 * Class to create the network client. Reads and writes the messages and calculates
 * the delay time.
 */
public class IEC62056Client implements Closeable {
    
    private static final Logger LOGGER = Logger.getLogger(IEC62056Client.class.getName());

    private int minimumResponseTime;
    private int maxTimeout;
    private int baudRate;

    private MessageParserService parser;

    private SerialPort serialPort;
    private Socket socket;

    private InputStream input;
    private OutputStream output;

    /**
     * Creates a new network client with the given host and port
     * @param host the host
     * @param port the port
     * @return {@link #createNetworkClient(java.lang.String, int)}
     * @throws IOException 
     */
    public static IEC62056Client createNetworkClient(String host, int port) throws IOException {
        return createNetworkClient(host, port, true);
    }
    /**
     * Creates a new network client with the given host, post and whether there is
     * delay or not.
     * @param host the host
     * @param port the port
     * @param noDelay the delay
     * @return {@link IEC62056Client} with a given {@link Socket}
     * @throws IOException 
     */
    public static IEC62056Client createNetworkClient(String host, int port, boolean noDelay) throws IOException {
        LOGGER.log(Level.FINE, "Connecting to {0}...", host);
        Socket socket = new Socket(host, port);
        LOGGER.log(Level.FINER, "Successfully connected to {0}!", host);
        socket.setTcpNoDelay(noDelay);

        return new IEC62056Client(socket);
    }

    /**
     * Creates a serial port client with the given port name.
     * @param portName the name of the port
     * @return {@link #createSerialPortClient(java.lang.String, int, org.openmuc.jrxtx.DataBits, org.openmuc.jrxtx.Parity, org.openmuc.jrxtx.FlowControl, org.openmuc.jrxtx.StopBits)}
     * @throws IOException 
     */
    public static IEC62056Client createSerialPortClient(String portName) throws IOException {
        return IEC62056Client.createSerialPortClient(portName, 300, DataBits.DATABITS_7, Parity.EVEN, FlowControl.NONE, StopBits.STOPBITS_1);
    }
    /**
     * Creates a serial port client with the given portname, baud rate, data bits,
     * parity, flow control and stop bits because of the character format.
     * @param portName the name of the port
     * @param baudRate the port name
     * @param dataBits data bits
     * @param parity parity 
     * @param flowControl flow control
     * @param stopBits stop bits
     * @return {@link #createSerialPortClient(java.lang.String, int, org.openmuc.jrxtx.DataBits, org.openmuc.jrxtx.Parity, org.openmuc.jrxtx.FlowControl, org.openmuc.jrxtx.StopBits, int) }
     * @throws IOException 
     */
    public static IEC62056Client createSerialPortClient(String portName, int baudRate, DataBits dataBits, Parity parity, FlowControl flowControl, StopBits stopBits) throws IOException {
        return createSerialPortClient(portName, baudRate, dataBits, parity, flowControl, stopBits, 5000);
    }

    /**
     * Creates a serial port client with the given port name, baud rate, data bits, parity,
     * flow control, stop bits and serial port timeout.
     * @param portName the name of the port
     * @param baudRate the baud rate
     * @param dataBits the data bits
     * @param parity the parity
     * @param flowControl the flow control
     * @param stopBits the stop bits
     * @param serialPortTimeout the serial port timeout
     * @return new {@link IEC62056Client with a given {@link SerialPort}
     * @throws IOException 
     */
    public static IEC62056Client createSerialPortClient(String portName, int baudRate, DataBits dataBits, Parity parity, FlowControl flowControl, StopBits stopBits, int serialPortTimeout) throws IOException {
        LOGGER.log(Level.FINE, "Connecting to {0}...", portName);
        SerialPort serialPort = SerialPortBuilder.newBuilder(portName)
                .setBaudRate(baudRate)
                .setDataBits(dataBits)
                .setParity(parity)
                .setStopBits(stopBits)
                .setFlowControl(flowControl)
                .build();
        LOGGER.log(Level.FINER, "Successfully connected to {0}!", portName);
        serialPort.setSerialPortTimeout(serialPortTimeout);

        return new IEC62056Client(serialPort);
    }

    /**
     * Gets the input and output stream of {@link SerialPort} and   
     * and initializes {@link #serialPort} of {@link IEC62056Client} with 
     * the given serial port object.
     * @param serialPort the serial port
     * @throws IOException 
     */
    public IEC62056Client(SerialPort serialPort) throws IOException {
        this(serialPort.getInputStream(), serialPort.getOutputStream());
        this.serialPort = serialPort;
    }

    /**
     * Gets the input and output stream of {@link Socket} and   
     * and initializes {@link #socket} of {@link IEC62056Client} with 
     * the given socket.
     * @param socket
     * @throws IOException 
     */
    public IEC62056Client(Socket socket) throws IOException {
        this(socket.getInputStream(), socket.getOutputStream());
        this.socket = socket;
    }
    
    /**
     * Initializes {@link #input} and {@link #output} of {@link IEC62056Client} with
     * the given input and output stream and creates a parser.
     * @param input input stream
     * @param output output stream
     */
    public IEC62056Client(InputStream input, OutputStream output) {
        this.input = input;
        this.output = output;

        LOGGER.log(Level.FINE, "Creating parser...");
        parser = new MessageParserService();
        LOGGER.log(Level.FINER, "Successfully created parser!");
        minimumResponseTime = 200;
        maxTimeout = 1500;
        baudRate = BaudRate.BAUD_RATE_300;
    }

    /**
     * Gets socket.
     * @return socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Gets serial port.
     * @return serial port
     */
    public SerialPort getSerialPort() {
        return serialPort;
    }
    /**
     * Closes the socket and serial port.
     * @throws IOException 
     */
    @Override
    public void close() throws IOException {
        LOGGER.log(Level.FINE, "Closing socket...");
        if (socket != null && !socket.isClosed()) {
            socket.close();
        } else if (serialPort != null && !serialPort.isClosed()) {
            serialPort.close();
        }
        LOGGER.log(Level.FINER, "Successfully closed socket!");
    }

    /**
     * Initializes {@link #minimumResponseTime} of {@link IEC62056Client} with the given
     * minimum response time value.
     * @param minimumResponseTime the minimum response time
     */
    public void setMinimumResponseTime(int minimumResponseTime) {
        this.minimumResponseTime = minimumResponseTime;
    }

    public int getMinimumResponseTime() {
        return minimumResponseTime;
    }

    /**
     * Initializes {@link #maxTimeout} of {@link IEC62056Client} with the given
     * maxmium timeout value.
     * @param maxTimeout maximium timeout
     */
    public void setMaxTimeout(int maxTimeout) {
        this.maxTimeout = maxTimeout;
    }

    public int getMaxTimeout() {
        return maxTimeout;
    }

    /**
     * Initializes {@link #baudRate} of {@link IEC62056Client} with the given baud rate value
     * and tries to set baud rate.
     * @param baudRate the baud rate value
     */
    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;

        if (getSerialPort() != null && getBaudRate() != getSerialPort().getBaudRate()) {
            try {
                getSerialPort().setBaudRate(getBaudRate());
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Could not set baud rate for serial port connected.", ex);
            }
        }
    }

    public int getBaudRate() {
        return baudRate;
    }

    /**
     * Calculates the baud time with the given characters time.
     * @param characters baud rate character time
     * @return baud time
     */
    protected int calculateBaudTime(int characters) {
        double timePerCharacter = 1.0 / getBaudRate() * 1000;
        return (int) (0.5 + timePerCharacter * characters);
    }

    /**
     * Character format is ISO/IEC 646:1991 (7-bit coding).
     *
     * @param character
     * @return
     */
    protected char codeCharacter(char character) {
        return (char) (character & 0b0111_1111);
    }

    /**
     * Serializes the message, sends it to the {@link #output} stream and calcultes
     * the baud time while every action is being logged.
     * @param message the message
     * @throws IOException 
     */
    public void write(Message message) throws IOException {
        LOGGER.log(Level.FINER, "Serializing message {0}...", message);
        String serialized = parser.serialize(message);
        LOGGER.log(Level.FINEST, "Successfully serialized message {0} to {1}", new Object[]{message, MessageCharacters.prettyReplaceString(serialized)});

        LOGGER.log(Level.FINE, "Sending {0}", MessageCharacters.prettyReplaceString(serialized));
        for (int i = 0; i < serialized.length(); i++) {
            output.write(codeCharacter(serialized.charAt(i)));
        }
        LOGGER.log(Level.FINEST, "Waiting {0} ms for message being sent...", calculateBaudTime(serialized.length()));
        waitFor(calculateBaudTime(serialized.length()));
        LOGGER.log(Level.FINER, "Sent {0}", MessageCharacters.prettyReplaceString(serialized));
    }

    /**
     * Calls the {@link #getMinimumResponseTime()} method and gives it to
     * {@link #waitUntilTimeout(int)}
     * @throws IOException 
     */
    public void waitUntilTimeout() throws IOException {
        waitUntilTimeout(getMinimumResponseTime());
    }
    /**
     * Calls the {@link #getMaxTimeout()} method and gives it to {@link #waitUntilTimeout(int, int)
     * to calculate the the time to wait until timout.
     * @param startingWaitingTime the starting waiting time
     * @throws IOException 
     */
    public void waitUntilTimeout(int startingWaitingTime) throws IOException {
        waitUntilTimeout(startingWaitingTime, getMaxTimeout());
    }
    
    /**
     * Calculates the current time to wait until timeout.
     * @param startingWaitingTime the starting waiting time
     * @param maxWaitingTime the maximum waiting time
     * @throws IOException 
     */
    public void waitUntilTimeout(int startingWaitingTime, int maxWaitingTime) throws IOException {
        int completeWaitingTime = 0;
        int currentWaitingTime = startingWaitingTime;
        int baudRateWaitingTime = calculateBaudTime(1);

        LOGGER.log(Level.FINEST, "Waiting for next character...");
        while (input.available() == 0 && completeWaitingTime < maxWaitingTime) {
            waitFor(currentWaitingTime);
            completeWaitingTime += currentWaitingTime;
            currentWaitingTime = baudRateWaitingTime;
        }
        LOGGER.log(Level.FINER, "Waited {0} ms for reaction!", completeWaitingTime);
    }

    /**
     * Gets the current time and adds the given time in milliseconds to get a ending
     * time. By subtracting the end time with the current time, you get the remaining time.
     * While the end time value is bigger then the current time the executing thread sleeps.
     * @param timeInMillis the time in milli seconds
     */
    public void waitFor(int timeInMillis) {
        long currentTime = System.currentTimeMillis();
        long endTime = currentTime + timeInMillis;
        long remainingTime = endTime - currentTime;

        while (currentTime < endTime) {
            try {
                Thread.sleep(remainingTime);
            } catch (InterruptedException ex) {
            }

            currentTime = System.currentTimeMillis();
            remainingTime = endTime - currentTime;
        }
    }

    /**
     * Reads the characters of noise.
     * @throws IOException
     * @deprecated
     */
    @Deprecated
    public void readNoise() throws IOException {
        int available = 0;
        while ((available = input.available()) > 0) {
            LOGGER.log(Level.FINE, "Reading {0} characters of noise...", available);
            byte[] bytes = new byte[available];
            input.read(bytes);

            if (input.available() == 0) {
                waitUntilTimeout();
            }
        }
    }

    /**
     * Returns {@link #read(java.lang.Class...)}
     * @param <C> 
     * @param clazz class representation
     * @return {@link #read(java.lang.Class...)}
     * @throws IOException 
     */
    public <C extends Message> C read(Class<C> clazz) throws IOException {
        return (C) read(new Class[]{clazz});
    }

    /**
     * Receives and deserializes the message while every action being logged.
     * @param classes {@link Message}
     * @return the deserialized message
     * @throws IOException 
     */
    public Message read(Class<? extends Message>... classes) throws IOException {
        waitUntilTimeout();

        int baudRateWaitingTime = calculateBaudTime(1);

        ParsingException lastException = null;
        LOGGER.log(Level.FINER, "Receiving message...");
        StringBuilder message = new StringBuilder();
        while (input.available() > 0) {
            message.append(codeCharacter((char) input.read()));

            if (input.available() == 0) {
                LOGGER.log(Level.FINER, "Received {0}", MessageCharacters.prettyReplaceString(message.toString()));
                try {
                    LOGGER.log(Level.FINER, "Deserializing message {0}...", MessageCharacters.prettyReplaceString(message.toString()));
                    Message deserialized = parser.deserialize(message.toString(), classes);
                    LOGGER.log(Level.FINER, "Deserialized message {0} to {1}", new Object[]{MessageCharacters.prettyReplaceString(message.toString()), deserialized});
                    LOGGER.log(Level.FINE, "Completely received {0}", MessageCharacters.prettyReplaceString(message.toString()));
                    return deserialized;
                } catch (ParsingException ex) {
                    LOGGER.log(Level.FINEST, "Deserializing of {0} failed with error {1}, waiting for more characters...", new Object[]{MessageCharacters.prettyReplaceString(message.toString()), ex.toString()});
                    lastException = ex;
                    waitUntilTimeout(baudRateWaitingTime);
                }
            }
        }

        throw new IOException("Could not receive awaited message within specified time."
                + (message.length() > 0 ? " But got " + MessageCharacters.prettyReplaceString(message.toString()) + "." : "")
                + (lastException != null ? " And the last error was " + lastException.toString() : ""));
    }

}
