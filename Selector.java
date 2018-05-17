package centrolControl;

import java.io.OutputStream;
import com.fazecast.jSerialComm.*;

public class Selector {

    private int currentPosition; // records the current Position of valve
    private SerialPort selectorPort;
    // currently, valve position 20 is not connected to any chemical, but we swtich
    // to this position at the end of each procedure to prevent the chemical from
    // leaking because of gravity.
    private int[] validPositions = {9, 11, 13, 15, 17, 19, 20, 21}; // records currently being used positions
    // records the baud rates that can be used
    private int[] validBaudRate = {1200, 2400, 4800, 9600, 14400, 19200, 28800, 38400};
    private SerialPortDataListener listener;
    /*
     * Default constructor: instantiates a Pump object with user input
     */

    public Selector(SerialPort port) {

        this.currentPosition = 1;
        this.selectorPort = port;

        // Let user check the parameters to see if it is correct
        System.out.println("BaudRate should be 9600: " + selectorPort.getBaudRate());
        System.out.println("DataBits should be 8: " + selectorPort.getNumDataBits());
        System.out.println("StopBits should be 1: " + selectorPort.getNumStopBits());
        System.out.println("Parity should be None: " + selectorPort.getParity());

        //open the port
        if (!selectorPort.isOpen()) {
            selectorPort.openPort();
            System.out.println("selector port has opened.");

            selectorPort.addDataListener(new SerialPortDataListener() {

                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                }

                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    byte[] newData = new byte[selectorPort.bytesAvailable()];
                    int numRead = selectorPort.readBytes(newData, newData.length);
                    System.out.println("Selector Port Read " + numRead + " bytes.");
                    }
                }
            });
        }
    }

    public void quit() {
        selectorPort.closePort();
        selectorPort.removeDataListener();
    }

    // TODO: TEST if function works
    // return the valve that is currently being used.
    public String getCurrentPosition() {
        String command = "*CP\r"; //TODO: check if command is correct
        byte[] buf = command.getBytes();
        selectorPort.writeBytes(buf, buf.length);
        OutputStream stream = selectorPort.getOutputStream();
        String feedback = stream.toString();
        return feedback;
    }

    // TODO: TEST if function works
    // return a list of valid commands
    public String getValidCommands() {
        String command = "*/?\r"; //TODO: check if command is correct
        byte[] buf = command.getBytes();
        selectorPort.writeBytes(buf, buf.length);
        OutputStream stream = selectorPort.getOutputStream();
        String feedback = stream.toString();
        return feedback;
    }

    // TODO: TEST if function works
    // return the part number and date of the firmware
    public String getPartNumberAndFirmwareVersion() {
        String command = "*VR\r"; // TODO: check if command is correct
        byte[] buf = command.getBytes();
        selectorPort.writeBytes(buf, buf.length);
        OutputStream stream = selectorPort.getOutputStream();
        String feedback = stream.toString();
        return feedback;
    }

    // this function will switch the selector to a new valve
    public void switchValve(int newValve) {
        String command = "";
        if (isValidValve(newValve)) {
            this.currentPosition = newValve;

            switch (this.currentPosition) {
                case 9:
                    command = "*GO09\r";
                    break;
                case 11:
                    command = "*GO11\r";
                    break;
                case 13:
                    command = "*GO13\r";
                    break;
                case 15:
                    command = "*GO15\r";
                    break;
                case 17:
                    command = "*GO17\r";
                    break;
                case 19:
                    command = "*GO19\r";
                    break;
                case 20:
                    command = "*GO20\r";
                    break;
                case 21:
                    command = "*GO21\r";
                    break;
                default:
                    command = "Invalid valve selection";
                    throw new IllegalArgumentException(command);
            }
            byte[] buf = command.getBytes();
            selectorPort.writeBytes(buf, buf.length);
        } else {
            throw new IllegalArgumentException("Try to switch to an invalid valve.");
        }
    }

    // check to see if the input valve number is available according to our predefined available ports list
    public boolean isValidValve(int valve) {
        for (int i = 0; i < validPositions.length; i++) {
            if (validPositions[i] == valve) {
                return true;
            }
        }
        return false;
    }

    // TODO: TEST if function works
	/* Set the baud rate to 1200, 2400, 4800, 9600 (default), 14400, 19200, 28800, or 38400.
     * The parity setting, number of data bits, and number of stop bits cannot be changed.
     * (See section entitled Setting a New Baud Rate on the next page)
     */
    public void setBaudRate(int newRate) {
        String command = "";
        if (isValidBaudRate(newRate)) {
            command = "*SB" + newRate + "\r";
            byte[] buf = command.getBytes();
            selectorPort.writeBytes(buf, buf.length);
        } else {
            throw new IllegalArgumentException("Try to switch to an invalid valve.");
        }
    }

    // check to see if the input baud rate is available according to our predefined available ports list
    public boolean isValidBaudRate(int rate) {
        for (int i = 0; i < validBaudRate.length; i++) {
            if (validBaudRate[i] == rate) {
                return true;
            }
        }
        return false;
    }
}
