package centrolControl;

import java.util.*;
import com.fazecast.jSerialComm.SerialPort;
import com.google.common.base.Stopwatch;
import java.applet.Applet;
import java.applet.AudioClip;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JOptionPane;

/**
 * This class is used to coordinate the behavior of syringe pump and valve selector
 * to perform a serial of operations. 
 *
 * @author Donny Sun, Kitty Li
 */
public class Fluidic {

    /**
     * Fludic object focus on perform a serial of operations as a module/step in
     * the sequencing experiment. Not like pump and selector object, which focus
     * on one single operation at a time.
     */
    // Initialize the pump object
    private Pump the_pump;
    // Initialize the valve selector object
    private Selector the_selector;
    Stopwatch stopwatch = Stopwatch.createUnstarted();

    /*
     * Set up the Fluidic object by asking user the serial port number to allow
     * the computer to recognize and connect the the devices.
     */
    public Fluidic() {

        // Gets all active ports
        SerialPort[] ports = SerialPort.getCommPorts();

        // Displays all active ports for the use to select from
        List<String> portsNames = new ArrayList<String>(ports.length);
        for (int i = 0; i < ports.length; i++) {
            System.out.println("port " + i + ": " + ports[i].getSystemPortName());
            portsNames.add(ports[i].getSystemPortName());
        }

        // Instantiates the connection between the pump and PC
        // Asks the user to enter the erial port that connects to the pump.
        System.out.println("Please select the serial port connects to the pump");
        Scanner input = new Scanner(System.in);
        String userport = input.next();

        // Check to make sure the input is valid
        while (!portsNames.contains(userport)) {
            System.out.println("Invalid input, please enter the port connects the pump: ");
            userport = input.next();
        }
        int numOfPort = portsNames.indexOf(userport);
        the_pump = new Pump(ports[numOfPort]);

        // Instantiate the connection between the valve selector and PC
        // Asks the user to enter the serial port that connects to the valve selector
        System.out.println("Please select the serial port connects to the selector");
        userport = input.next();

        // Check to make sure the input is valid
        while (!portsNames.contains(userport) || userport.equals(ports[numOfPort].getSystemPortName())) {
            System.out.println("Invalid input, please enter the port connects the selector:");
            userport = input.next();
        }
        input.close();
        numOfPort = portsNames.indexOf(userport);
        the_selector = new Selector(ports[numOfPort]);
    }

    /**
     * This functions serves as the unit test, it can be changed to test some
     * new features or the reliability of the program
     *
     * @throws InterruptedException
     */
    public void pumpTest() throws InterruptedException {
        the_pump.flipToWaster();
        System.out.println("initialize");
        the_pump.initialize();


        System.out.println("port 11 100uL");
        the_selector.switchValve(11);
        Thread.sleep(1000);
        runNCycles(100);

        System.out.println("port 13 250uL");
        the_selector.switchValve(13);
        Thread.sleep(1000);
        runNCycles(250);

        System.out.println("port 20 400uL");
        the_selector.switchValve(20);
        Thread.sleep(1000);
        runNCycles(400);

        the_pump.quit();
        the_selector.quit();
    }

    /**
     * Runs one instruction
     * 
     * @param instr is an Instruction object
     */
    public void runInstruction(Instruction instr) throws InterruptedException {

        String name = instr.getName();

        if (name.equals("SET")) {

            the_selector.switchValve(instr.getParameters().get(0));
            Thread.sleep(1000);

        } else if (name.equals("WAIT")) {
            Integer time = instr.getParameters().get(0);
            Thread.sleep(time);
        } else {
            List<Integer> params = instr.getParameters();
            Integer valve = params.get(0);
            Integer speed = params.get(1);
            Integer amount = params.get(2);

            the_pump.setMaxSpeed(speed * 3333);
            the_selector.switchValve(valve);
            Thread.sleep(1000);
            runNCycles(amount);

        }
    }

    public void initiate() {
        // serves as a precaution in case fluidic flew back in opposite direction
        the_pump.flipToWaster();
        the_pump.initialize();
    }

    /**
     * Instructions on 'Wash' steps, this is no longer being used becuase the program
     * reads the instruction directly and perform accordingly
     * 
     * @throws InterruptedException
     */
    public void wash() throws InterruptedException {
        int counter = 0;
        int cleavBufVOl = 5000; //cleav buff wash 5000uL
        int highSaltVol = 5000; //high salt buff wash 5000uL

        stopwatch.start();

        //get start
        the_pump.flipToWaster();
        Thread.sleep(1000);
        the_pump.initialize();
        Thread.sleep(12000);

        //wash with buffer
        //adjust top speed to high
        the_pump.setMaxSpeed(10000);
        Thread.sleep(1000);
        System.out.println("Start Washing!");

        the_selector.switchValve(9);
        Thread.sleep(1000);
        runNCycles(cleavBufVOl);
        System.out.println("port 9 cleav buff washed " + cleavBufVOl / the_pump.maxVol + " times using " + cleavBufVOl + " uL.");

        the_selector.switchValve(11);
        Thread.sleep(1000);
        runNCycles(highSaltVol);
        System.out.println("port 11 high salt buff washed " + highSaltVol / the_pump.maxVol + " times " + highSaltVol + " uL.");

        for (int i = 0; i < 10; i++) {
            the_selector.switchValve(13);
            Thread.sleep(1000);
            runNCycles(250);

            the_selector.switchValve(15);
            Thread.sleep(1000);
            runNCycles(250);

            the_selector.switchValve(17);
            Thread.sleep(1000);
            runNCycles(250);

            the_selector.switchValve(19);
            Thread.sleep(1000);
            runNCycles(250);

            the_selector.switchValve(21);
            Thread.sleep(1000);
            runNCycles(250);

            counter++;
            System.out.println("port 13, 15, 17, 19, 21 washed " + counter + " times.");
        }

        for (int j = 0; j < 10; j++) {
            the_selector.switchValve(17);
            Thread.sleep(1000);
            runNCycles(250);

            the_selector.switchValve(19);
            Thread.sleep(1000);
            runNCycles(250);

            the_selector.switchValve(21);
            Thread.sleep(1000);
            runNCycles(250);

            counter++;
            System.out.println("port 17, 19, 21 washed " + counter + " times.");
        }

        the_selector.switchValve(20);
        Thread.sleep(1000);
        System.out.println("Washing is done, time used : " + stopwatch.elapsed(TimeUnit.MINUTES) + " minutes");

    }

    /**
     * This function will read in the volume of chemical to be added, and calculate
     * the number of pumping cycles it needed to complet the operation.
     *
     * @assumes the pump device being used has a maximum volume of 250 micro liter
     *
     * @throws InterruptedException
     */
    private void runNCycles(int volume) throws InterruptedException {
        // calculates number of cycles needed according to volume of chemical to add
        int[] parameters = calPumpCycAndVol(volume);
        for (int i = 0; i < parameters[0]; i++) {
            the_pump.flipToSolution();
            the_pump.setPosition(the_pump.resolution);

            the_pump.flipToWaster();
            the_pump.dispense(the_pump.resolution);
        }
        if (parameters[1] > 0) {
            the_pump.flipToSolution();
            the_pump.setPosition(parameters[1] * the_pump.resolution / the_pump.maxVol);

            the_pump.flipToWaster();
            the_pump.dispense(parameters[1] * the_pump.resolution / the_pump.maxVol);
        }
        the_pump.getReady();
    }

    /**
     * This function takes in total amount of chemical to add in, then convert it
     * into number of pumping cycles, assuming the max volume one pumping cycle
     * can process is 250 ul. Then 400 ul will needs 400/250 = 1 + 150/250 = 1.6
     * cycles. it will be saved in an array, '1' saved as 1st element and 0.6 saved
     * as second element.
     *
     * @param volume amount of chemical to add in terms of micro liter.
     * @return number of cycles, first element of array gives number of full cycles
     * and the second element of array gives number of partial cycle.
     */
    private int[] calPumpCycAndVol(int volume) {
        int[] result = new int[2];
        int numCyc = volume / the_pump.maxVol;
        int remainder = volume % the_pump.maxVol;
        result[0] = numCyc;
        result[1] = remainder;
        return result;
    }

    /**
     * Instructions on 'Inject Buffer' steps, this is no longer being used becuase
     * the program reads the instruction directly and perform accordingly
     *
     * @throws InterruptedException
     */
    public void injectBuffer() throws InterruptedException, FileNotFoundException, IOException {
        stopwatch.reset();
        stopwatch.start();

        //get start
        the_pump.flipToWaster();
        the_pump.initialize();

        the_pump.setMaxSpeed(10000);
        System.out.println("Buffer Injection Starts");

        the_selector.switchValve(15);
        Thread.sleep(1000);
        runNCycles(250);

        the_selector.switchValve(17);
        Thread.sleep(1000);
        runNCycles(250);

        the_selector.switchValve(19);
        Thread.sleep(1000);
        runNCycles(250);

        the_selector.switchValve(21);
        Thread.sleep(1000);
        runNCycles(250);

        the_selector.switchValve(9);
        Thread.sleep(1000);
        runNCycles(250);

        the_selector.switchValve(11);
        Thread.sleep(1000);
        runNCycles(1000);

        the_selector.switchValve(13);
        Thread.sleep(1000);

        runNCycles(250);

        the_selector.switchValve(20);
        Thread.sleep(1000);
        System.out.println("Buffer Injection is done, time used : " + stopwatch.elapsed(TimeUnit.MINUTES) + " minutes");
    }

    /**
     * Instructions on 'Incorp 0' steps, this is no longer being used becuase
     * the program reads the instruction directly and perform accordingly
     *
     * @throws InterruptedException
     */
    public void startIncorp0() throws InterruptedException, FileNotFoundException, IOException {
        stopwatch.reset();
        stopwatch.start();

        //get start
        the_pump.flipToWaster();
        the_pump.initialize();

        System.out.println("Incorp 0 Starts");

        System.out.println("Tris Wash Port 13 HighSpeed 500uL.");
        // Tris Wash
        the_selector.switchValve(13);
        Thread.sleep(1000);
        //Speed High
        the_pump.setMaxSpeed(10000);
        runNCycles(500);

        System.out.println("Incorp Mix Port 15 NormalSpeed 500uL.");
        // Incorp Mix
        the_selector.switchValve(15);
        Thread.sleep(1000);
        //normal speed
        the_pump.setMaxSpeed(3333);
        runNCycles(500);

        System.out.println("Color Dye Port 17 HighSpeed 200 uL and NormalSpeed 200uL.");
        // Color Dye
        the_selector.switchValve(17);
        Thread.sleep(1000);
        //speed high
        the_pump.setMaxSpeed(10000);

        runNCycles(200);
        //normal speed
        the_pump.setMaxSpeed(3333);
        runNCycles(200);

        System.out.println("Time to take 220 seconds break.");
        //hold for 220 seconds break
        Thread.sleep(220000);

        System.out.println("Dark Dye Port 19 NormalSpeed 400uL.");
        // Dark Dye
        the_selector.switchValve(19);
        Thread.sleep(1000);
        //normal speed
        the_pump.setMaxSpeed(3333);

        runNCycles(400);

        System.out.println("Time to take 180 seconds break.");
        //hold for 180 seconds break
        Thread.sleep(180000);

        System.out.println("IM Buffer Port 21 HighSpeed 400uL.");
        // IM Buffer
        the_selector.switchValve(21);
        Thread.sleep(1000);
        //high speed
        the_pump.setMaxSpeed(10000);

        runNCycles(400);

        the_selector.switchValve(20);
        Thread.sleep(1000);
        showMessage("Please select the FOVs", "FOV Selection");
    }

    /**
     * Instructions on 'Incorp 0 last step', this is no longer being used becuase
     * the program reads the instruction directly and perform accordingly
     *
     * @throws InterruptedException
     */
    public void cyc0LastStep() throws InterruptedException {
        System.out.println("IM Buffer Port 21 HighSpeed 500uL.");
        the_selector.switchValve(21);
        Thread.sleep(1000);
        the_pump.setMaxSpeed(10000);
        runNCycles(500);

        the_selector.switchValve(20);//assume valve 20 is not currently used
        Thread.sleep(1000);
        System.out.println("Incorp 0 is done, time used : " + stopwatch.elapsed(TimeUnit.MINUTES) + " minutes");
    }

    /**
     * Instructions on 'Run Sequences' steps, this is no longer being used becuase
     * the program reads the instruction directly and perform accordingly
     *
     * @throws InterruptedException
     */
    public void runSequencing(int counter) throws InterruptedException {
        stopwatch.reset();
        stopwatch.start();

        System.out.println("Incorp " + counter + " Starts");

        //get start
        the_pump.flipToWaster();
        the_pump.initialize();

        //Cleavage
        System.out.println("Cleavage Buffer Port 9 HighSpeed 200uL and NormalSpeed 150uL.");
        the_selector.switchValve(9);
        Thread.sleep(1000);
        //high speed
        the_pump.setMaxSpeed(10000);
        runNCycles(150);
        //normal speed
        the_pump.setMaxSpeed(3333);
        runNCycles(200);

        System.out.println("It is time to take 280 seconds break.");
        //hold for 280 seconds break
        Thread.sleep(280000);
        
        System.out.println("Cleavage Buffer Port 21 NormalSpeed 150uL.");
        //normal speed
        runNCycles(200);

        System.out.println("The second break will take 200 seconds.");
        //hold for 200 seconds break
        Thread.sleep(200000);

        System.out.println("High Salt Buffer Port 11 HighSpeed 1200uL and NormalSpeed 500uL.");
        //high salt
        the_selector.switchValve(11);
        Thread.sleep(1000);
        //high speed
        the_pump.setMaxSpeed(10000);
        runNCycles(1200);
        //normal speed
        the_pump.setMaxSpeed(3333);
        runNCycles(500);

        System.out.println("Tris Wash Port 13 HighSpeed 500uL.");
        // Tris wash
        the_selector.switchValve(13);
        Thread.sleep(1000);
        //high speed
        the_pump.setMaxSpeed(10000);
        runNCycles(500);

        System.out.println("Incorp Mix Port 15 NormalSpeed 500uL.");
        //Incorp Mix
        the_selector.switchValve(15);
        Thread.sleep(1000);
        //normal speed
        the_pump.setMaxSpeed(3333);
        runNCycles(500);

        System.out.println("Color Dye Port 17 HighSpeed 200uL and NormalSpeed 200uL");
        //Color Dye
        the_selector.switchValve(17);
        Thread.sleep(1000);
        //high speed
        the_pump.setMaxSpeed(10000);
        runNCycles(200);
        //normal speed
        the_pump.setMaxSpeed(3333);
        runNCycles(200);

        System.out.println("Taking 3rd break for 220 seconds.");
        //hold for 220 seconds break
        Thread.sleep(220000);

        System.out.println("Dark Dye Port 19 NormalSpeed 400uL.");
        // Dark dye
        the_selector.switchValve(19);
        Thread.sleep(1000);
        //normal speed
        the_pump.setMaxSpeed(3333);

        runNCycles(400);

        System.out.println("Taking the final break for 180 seconds.");
        //hold for 180 seconds break
        Thread.sleep(180000);

        System.out.println("IM Buffer Port 21 HighSpeed 500uL.");
        // IM Buffer
        the_selector.switchValve(21);
        Thread.sleep(1000);
        //high speed
        the_pump.setMaxSpeed(10000);
        runNCycles(500);

        the_selector.switchValve(20);//assume valve 20 is not currently used
        Thread.sleep(1000);
        System.out.println("Incorp " + counter + " is done, time used : " + stopwatch.elapsed(TimeUnit.MINUTES) + " minutes");
    }

    public void lastSequencingCycle() throws InterruptedException {
        stopwatch.reset();
        stopwatch.start();

        System.out.println("Final Incorpration Cycle Starts");
        //get start
        the_pump.flipToWaster();
        the_pump.initialize();

        //Cleavage
        System.out.println("Cleavage Buffer Port 9 HighSpeed 200uL and NormalSpeed 200uL.");
        the_selector.switchValve(9);
        Thread.sleep(1000);
        //high speed
        the_pump.setMaxSpeed(10000);
        runNCycles(200);
        //normal speed
        the_pump.setMaxSpeed(3333);
        runNCycles(200);

        System.out.println("It is time to take 280 seconds break.");
        //hold for 280 seconds break
        Thread.sleep(280000);
        System.out.println("Cleavage Buffer Port 21 NormalSpeed 150uL.");
        //normal speed
        runNCycles(150);

        System.out.println("The second break will take 200 seconds.");
        //hold for 200 seconds break
        Thread.sleep(200000);

        System.out.println("High Salt Buffer Port 11 HighSpeed 1200uL and NormalSpeed 500uL.");
        //high salt
        the_selector.switchValve(11);
        Thread.sleep(1000);
        //high speed
        the_pump.setMaxSpeed(10000);
        runNCycles(1200);
        //normal speed
        the_pump.setMaxSpeed(3333);
        runNCycles(500);

        System.out.println("Tris Wash Port 13 HighSpeed 500uL.");
        // Tris wash
        the_selector.switchValve(13);
        Thread.sleep(1000);
        //high speed
        the_pump.setMaxSpeed(10000);
        runNCycles(500);

        System.out.println("Incorp Mix Port 15 NormalSpeed 500uL.");
        //Incorp Mix
        the_selector.switchValve(15);
        Thread.sleep(1000);
        //normal speed
        the_pump.setMaxSpeed(3333);
        runNCycles(500);

        System.out.println("Color Dye Port 17 HighSpeed 200uL and NormalSpeed 200uL");
        //Color Dye
        the_selector.switchValve(17);
        Thread.sleep(1000);
        //high speed
        the_pump.setMaxSpeed(10000);
        runNCycles(200);
        //normal speed
        the_pump.setMaxSpeed(3333);
        runNCycles(200);

        System.out.println("Taking 3rd break for 220 seconds.");
        //hold for 220 seconds break
        Thread.sleep(220000);

        System.out.println("IM Buffer Port 21 HighSpeed 500uL.");
        // IM Buffer
        the_selector.switchValve(21);
        Thread.sleep(1000);
        //high speed
        the_pump.setMaxSpeed(10000);
        runNCycles(500);

        the_selector.switchValve(20);//assume valve 20 is not currently being used
        Thread.sleep(1000);
        System.out.println("The final incorp cycle is done, time used : " + stopwatch.elapsed(TimeUnit.MINUTES) + " minutes");
    }

    //FIXME: not working
    public void playSound() throws FileNotFoundException, IOException, InterruptedException {
        URL url = null;
        try {
            File file = new File("G:/pump and valve system/CodeProjectSerialComms/Resources/beep.wav");
            if (file.canRead()) {
                url = file.toURI().toURL();
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("could not play ", e);
        }

        // URL url = StdAudio.class.getResource(filename);
        if (url == null) {
            throw new IllegalArgumentException("could not play '" + "'");
        }

        AudioClip clip = Applet.newAudioClip(url);
        clip.play();
    }

    //FIXME: not working, plan to use this at the end of sequencing experiment,
    //now it quits itself.
    public int yesOrNo(String infoMessage, String title) {
        Object[] options = {"Yes, done", "Quit Program!"};
        int selection = JOptionPane.showOptionDialog(null, infoMessage, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]); //default button title
        return selection;
    }

    public static void showMessage(String infoMessage, String title) {
        JOptionPane.showMessageDialog(null, infoMessage, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public Selector getSelector() {
        return this.the_selector;
    }
}
