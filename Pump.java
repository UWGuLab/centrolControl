package centrolControl;

import com.fazecast.jSerialComm.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is the class of Pump, it treats the pump as an object and defines all its
 * behaviors. The function list here might not be the true representation of its
 * full capabilities, please refer to the user manual for the full command list.
 * I utilized the command predefined by the manufacture to send the operation
 * command from this master program to the pump using serial port.
 *
 * @author Donny Sun
 */
public class Pump implements SerialPortPacketListener {

    /**
     * This pump object depends on the jSerialComm library, it has the following
     * attributes:
     * pumpPort -- SerialPort object, not intended to be modified by other classes
     * maxVol -- the maximum volume of the syringe according to the specific model used
     * resolution -- the maximum resolution of the syringe movement.
     * validator_ -- temporarily saves the feedback info from pump via serial port
     * pattern_ -- the compare pattern that helps to validate the status of pump.
     *          , if it is currently idle (not busy), it will be read as '/0'
     */
    private SerialPort pumpPort;
    protected int maxVol = 250; //max volume is 250 micro liters.
    protected int resolution = 48000; //resolution of pump movement.
    private String validator_;
    public final Pattern pattern_ = Pattern.compile("/0`");

    /**
     * Instantiates the Pump object with the pass in serial port
     *
     * @param port the serial port used connects to the pump
     */
    public Pump(SerialPort port) {

        this.pumpPort = port;

        // Display the parameters for user to check if they are correct
        System.out.println("BaudRate should be 9600: " + pumpPort.getBaudRate());
        System.out.println("DataBits should be 8: " + pumpPort.getNumDataBits());
        System.out.println("StopBits should be 1: " + pumpPort.getNumStopBits());
        System.out.println("Parity should be None: " + pumpPort.getParity());

        //open the port, instantiates the connection
        if (!pumpPort.isOpen()) {
            pumpPort.openPort();
            System.out.println("pump port has opened.");
            pumpPort.addDataListener(this); //add listener
        }
    }

    // terminate the connection between pump and computer
    public void quit() {
        pumpPort.closePort();
        pumpPort.removeDataListener();
    }

    /*
     * Initialize the pump
     */
    public void initialize() {
        getReady();
        String command = "/1W4R\r"; //TODO: 1 is the device ID, might need to make it a variable
        byte[] buf = command.getBytes();
        pumpPort.writeBytes(buf, buf.length);
    }

    /*
     * set the absolute position where the syringe will be move to
     * the volume of the syringe currently in use is 250 uL, it has
     * 48,000 steps. so each step is equal to 5.2 nL.
     * @param newPosition is the new position where to move the syringe to
     * @return no return
     */
    /* TODO: if the future, should add the feature that it will compare the current
     * position to the future position, if the syringe is moving upword, then
     * automatically flip the valve to waste, verse visa, flip to the selector.
     */
    public void setPosition(int newPosition) {
        getReady();
        if (newPosition < 0 | newPosition > 48000) {
            throw new IllegalArgumentException("The syringe range should be within 0 to 48,000");
        }
        String command = "/1A" + newPosition + "R\r";
        byte[] buf = command.getBytes();
        pumpPort.writeBytes(buf, buf.length);
    }

    /*
     * set the current location as the new zero position, the result is automatically stored in non-volatile memory(NVM).
     */
    public void resetZeroPosition() {
        String command = "/1W5R\r"; //TODO: actually 1 is the device ID, might need to make it an available
        byte[] buf = command.getBytes();
        pumpPort.writeBytes(buf, buf.length);
    }

    /*
     * Set the syringe top speed to 'n' steps per second. the top speed is the rate at which dispenses and
     * aspirates operate. top speed can be changed "on-the-fly" during a syringe move. for speeds
     * above 1000 sps, changes in speed which are too large may stall the syringe motor. A 'R' command
     * and is not required for this instruction.
     * @param newMaxSpeed the new set top speed the syringe will move in steps/second
     */
    public void setMaxSpeed(int newMaxSpeed) {
        getReady();
        if (newMaxSpeed < 0 | newMaxSpeed > 10000) {
            throw new IllegalArgumentException("The syringe max speed should be within 0 to 10,000");
        }
        String command = "/1V" + newMaxSpeed + "R\r";
        byte[] buf = command.getBytes();
        pumpPort.writeBytes(buf, buf.length);
    }

    // flip relay to waste (this can change in the future depends on the tube configuration)
    public void flipToWaster() {
        getReady();
        String command = "/1IR\r";
        byte[] buf = command.getBytes();
        pumpPort.writeBytes(buf, buf.length);
    }

    // flip relay to chemical solution/valve selector
    public void flipToSolution() {
        getReady();
        String command = "/1OR\r";
        byte[] buf = command.getBytes();
        pumpPort.writeBytes(buf, buf.length);

    }

    /* setAcceleration /1LxR (1 to 20)
     * @param newAcc the new acceleration set for the syringe
     */
    public void setAcceleration(int newAcc) {
        getReady();
        if (newAcc < 0 | newAcc > 20) {
            throw new IllegalArgumentException("The acceleration should be within 0 to 20");
        }
        String command = "/1L" + newAcc + "R\r";
        byte[] buf = command.getBytes();
        pumpPort.writeBytes(buf, buf.length);
    }

    /* setDeceleration /1IxR (1 to 20)
     * @param newDec the new deceleration set for the syringe
     */
    public void setDeceleration(int newDec) {
        getReady();
        if (newDec < 0 | newDec > 20) {
            throw new IllegalArgumentException("The deceleration should be within 0 to 20");
        }
        String command = "/1I" + newDec + "R\r";
        byte[] buf = command.getBytes();
        pumpPort.writeBytes(buf, buf.length);
    }

    /* move to relative position from the current position in terms of steps (-48,000 to 48,000)
     * @param relativesteps if relativeSteps is positive number, then it's moving opposite direction toward 0 position, verse visa.
     */
    public void moveRelativeSteps(int relativeSteps) {
        getReady();
        //TODO: integrate the get current position method and measure the current location to determine
        // how many steps can move.
        if (relativeSteps < 48000 | relativeSteps > 48000) {
            throw new IllegalArgumentException("The relative steps syringe can move should be within -48000 to 48000");
        }
        String command = "/1P" + relativeSteps + "R\r";
        byte[] buf = command.getBytes();
        pumpPort.writeBytes(buf, buf.length);
    }

    /* dispose liquid /1DxxxxxR the syringe will move xx,xxx steps towards to zero position
     * @param volume amount of liquid to dispose in terms of steps
     */
    public void dispose(int volume) {
        //TODO: integrate get current position method so it will know if ask to dispose more than
        // it actually have.
        getReady();
        if (volume < 0 | volume > 48000) {
            throw new IllegalArgumentException("The dispose amount should be within 00 to 48000");
        }

        String command = "/1D" + volume + "R\r";
        byte[] buf = command.getBytes();
        pumpPort.writeBytes(buf, buf.length);
    }

    //return the current absolute position of the syringe, the reply should be /0'8000
    public void getPosition() {
        getReady();
        String command = "/1?\r";
        byte[] buf = command.getBytes();
        System.out.println("return position:");
        pumpPort.writeBytes(buf, buf.length);
    }

    /* query the pump status with a carriage return (hex 0D, decimal 13) character
     * to determine if the pump is busy or the move has finished.
     */
    private boolean getStatus() throws InterruptedException {
        validator_ = null;
        String command = "/1\r";
        byte[] buf = command.getBytes();
        pumpPort.writeBytes(buf, buf.length);
        Thread.sleep(500);
        if (validator_ != null) {
            Matcher matcher = pattern_.matcher(validator_);
            if (matcher.find()) {
//                System.out.println("Ready");
                return true;
            } else {
//                System.out.println("device busy");
            }
        }
        return false;

    }

    public void getReady() {
        try {
            Thread.sleep(500);
            while (!getStatus()) {
                Thread.sleep(500);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Pump.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getPacketSize() {
        return 8;
    }

    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    public void serialEvent(SerialPortEvent event) {
        byte[] newData = event.getReceivedData();
        char[] result = new char[newData.length];

        for (int i = 0; i < newData.length; ++i) //            System.out.print((char)newData[i]);
        {
//            System.out.println((char) newData[i]);
            result[i] = (char) newData[i];
        }
//        WindowEventDemo.display("\n");
//        System.out.println(result);
        validator_ = String.valueOf(result);
//        System.out.println(validator_);
    }
}
