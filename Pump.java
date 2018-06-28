package centrolControl;

import com.fazecast.jSerialComm.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//This is the pump object
public class Pump implements SerialPortPacketListener {

    private SerialPort pumpPort;
    protected int maxVol = 250; //max volume is 250 micro liters.
    private String _validator;
    public final Pattern pattern = Pattern.compile("/0`");
    /*
     * Default constructor: instantiates a Pump object with user input
     */

    public Pump(SerialPort port) {

        this.pumpPort = port;

        // Let user check the parameters to see if it is correct
        System.out.println("BaudRate should be 9600: " + pumpPort.getBaudRate());
        System.out.println("DataBits should be 8: " + pumpPort.getNumDataBits());
        System.out.println("StopBits should be 1: " + pumpPort.getNumStopBits());
        System.out.println("Parity should be None: " + pumpPort.getParity());

        //open the port
        if (!pumpPort.isOpen()) {
            pumpPort.openPort();
            System.out.println("pump port has opened.");
            pumpPort.addDataListener(this);
        }
    }

    public void quit() {
        pumpPort.closePort();
        pumpPort.removeDataListener();
    }

    /*
     * Initialize the pump
     */
    public void intialize() throws InterruptedException {
        getReady();
        String command = "/1W4R\r"; //TODO: actually 1 is the device ID, might need to make it an available
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
    public void setPosition(int newPosition) throws InterruptedException {
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
        if (newMaxSpeed < 0 | newMaxSpeed > 10000) {
            throw new IllegalArgumentException("The syringe max speed should be within 0 to 10,000");
        }
        String command = "/1V" + newMaxSpeed + "R\r";
        byte[] buf = command.getBytes();
        pumpPort.writeBytes(buf, buf.length);
    }

    // flip relay to waste (this can change in the future depends on the tube configuration)
    public void flipToWaster() throws InterruptedException {
        getReady();
        String command = "/1IR\r";
        byte[] buf = command.getBytes();
        pumpPort.writeBytes(buf, buf.length);
    }

    // flip relay to chemical solution/valve selector
    public void flipToSolution() throws InterruptedException {
        getReady();
        String command = "/1OR\r";
        byte[] buf = command.getBytes();
        pumpPort.writeBytes(buf, buf.length);

    }

    /* setAcceleration /1LxR (1 to 20)
     * @param newAcc the new acceleration set for the syringe
     */
    public void setAcceleration(int newAcc) {
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
    public void dispose(int volume) throws InterruptedException {
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
        String command = "/1?\r";
        byte[] buf = command.getBytes();
        System.out.println("return position:");
        pumpPort.writeBytes(buf, buf.length);
    }

    /* query the pump status with a carriage return (hex 0D, decimal 13) character
     * to determine if the pump is busy or the move has finished.
     */
//    public void getStatus() {
//        try {
////            System.out.println("get Status");
//            Thread.sleep(500);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(Pump.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        String command = "/1\r";
//        byte[] buf = command.getBytes();
//        pumpPort.writeBytes(buf, buf.length);
//        validateStatus();
//    }

    private boolean getStatus() {
        String command = "/1\r";
        byte[] buf = command.getBytes();
        pumpPort.writeBytes(buf, buf.length);
        if (_validator != null) {
            Matcher matcher = pattern.matcher(_validator);
            if (matcher.find()) {
                System.out.println("Ready");
                return true;
            } else {
                System.out.println("device busy");
            }
        }
        return false;

    }

    public void getReady() throws InterruptedException {
        Thread.sleep(2000);
        while (!getStatus()) {
            Thread.sleep(1000);
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
        _validator = String.valueOf(result);
        System.out.println(_validator);
    }

//    private void validateStatus() {
//        if (_validator != null) {
//            Matcher matcher = pattern.matcher(_validator);
//            if (matcher.find()) {
//                System.out.println("Ready");
//            } else {
//                System.out.println("device busy");
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(Pump.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                getStatus();
//            }
//        }
//    }
}
