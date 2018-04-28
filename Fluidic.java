package centrolControl;

import centrolControl.Pump;
import centrolControl.Selector;
import java.util.ArrayList;
import java.util.Scanner;
import com.fazecast.jSerialComm.SerialPort;
import com.google.common.base.Stopwatch;
import java.applet.Applet;
import java.applet.AudioClip;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JOptionPane;
import sun.audio.*;

public class Fluidic {

    //TODO: define pump and selector
    private Pump the_pump;
    private Selector the_selector;
    Stopwatch stopwatch = Stopwatch.createUnstarted();

    /*
     * Constructor with no parameters
     */
    public Fluidic() {

        // Gets all available ports
        SerialPort[] ports = SerialPort.getCommPorts();

        // Displays all available ports for the use to use
        ArrayList portsNames = new ArrayList(ports.length);
        for (int i = 0; i < ports.length; i++) {
            System.out.println("port " + i + ": " + ports[i].getSystemPortName());
            portsNames.add(ports[i].getSystemPortName());
        }

        // Instantiate the pump connection with PC

        // Asks the user for input for which port to use
        System.out.println("Please select the port for the pump");
        Scanner input = new Scanner(System.in);
        String userport = input.next();

        // make sure user select the correct port name
        while (!portsNames.contains(userport)) {
            System.out.println("Invalid input, please select the port for the pump: ");
            userport = input.next();
        }
        int numOfPort = portsNames.indexOf(userport);
        the_pump = new Pump(ports[numOfPort]);

        // Instantiate the selector the connection with PC
        // Asks the user for input for which port to use
        System.out.println("Please select the port for the selector");
        userport = input.next();

        // make sure user select the correct port name and the port is not being used
        while (!portsNames.contains(userport) || userport.equals(ports[numOfPort].getSystemPortName())) {
            System.out.println("Invalid input, please select the port for the selector:");
            userport = input.next();
        }
        input.close();
        numOfPort = portsNames.indexOf(userport);
        the_selector = new Selector(ports[numOfPort]);
    }

    public void pumpTest() throws InterruptedException {
        the_pump.intialize();
        Thread.sleep(12000);
        the_selector.switchValve(9);
        Thread.sleep(1000);
        the_selector.switchValve(11);
        Thread.sleep(1000);
        the_selector.getCurrentPosition();
    }

    public void wash() throws InterruptedException {
        int counter = 0;

        stopwatch.start();

        //get start
        the_pump.intialize();
        Thread.sleep(12000);

        //wash with buffer
        //adjust top speed to high
        the_pump.setMaxSpeed(10000);
        Thread.sleep(1000);
        System.out.println("Start Washing!");

        the_selector.switchValve(9);
        Thread.sleep(1000);
        runNCyclesAtHighSpeed(2);
        System.out.println("port 9 cleav buff washed 20 times.");

        the_selector.switchValve(11);
        Thread.sleep(1000);
        runNCyclesAtHighSpeed(2);
        System.out.println("port 11 cleav buff washed 20 times.");

        for (int i = 0; i < 2; i++) {
            the_selector.switchValve(13);
            Thread.sleep(1000);
            runNCyclesAtHighSpeed(1);

            the_selector.switchValve(15);
            Thread.sleep(1000);
            runNCyclesAtHighSpeed(1);

            the_selector.switchValve(17);
            Thread.sleep(1000);
            runNCyclesAtHighSpeed(1);

            the_selector.switchValve(19);
            Thread.sleep(1000);
            runNCyclesAtHighSpeed(1);

            the_selector.switchValve(21);
            Thread.sleep(1000);
            runNCyclesAtHighSpeed(1);

            counter++;
            System.out.println("port 13, 15, 17, 19, 21 washed " + counter + " times.");
        }

        for (int j = 0; j < 2; j++) {
            the_selector.switchValve(17);
            Thread.sleep(1000);
            runNCyclesAtHighSpeed(1);

            the_selector.switchValve(19);
            Thread.sleep(1000);
            runNCyclesAtHighSpeed(1);

            the_selector.switchValve(21);
            Thread.sleep(1000);
            runNCyclesAtHighSpeed(1);

            counter++;
            System.out.println("port 17, 19, 21 washed " + counter + " times.");
        }

        System.out.println("Washing is done, time used : " + stopwatch.elapsed(TimeUnit.MINUTES) + " minutes");

    }

    private void runNCyclesAtHighSpeed(int n) throws InterruptedException {
        //TODO: add currentMaxSpeed check
        for (int i = 0; i < n; i++) {
            the_pump.flipToSolution();
            Thread.sleep(1000);
            the_pump.setPosition(48000);
            Thread.sleep(6000);
            the_pump.flipToWaster();
            Thread.sleep(1000);
            the_pump.dispose(48000);
            Thread.sleep(6000);
        }
    }

    private void runNCyclesAtNormSpeed(int n) throws InterruptedException {
        //TODO: add currentMaxSpeed check
        for (int i = 0; i < n; i++) {
            the_pump.flipToSolution();
            Thread.sleep(1000);
            the_pump.setPosition(48000);
            Thread.sleep(16000);
            the_pump.flipToWaster();
            Thread.sleep(1000);
            the_pump.dispose(48000);
            Thread.sleep(16000);
        }
    }

    public void injectBuffer() throws InterruptedException, FileNotFoundException, IOException {
        stopwatch.reset();
        stopwatch.start();

        //get start
        the_pump.intialize();
        Thread.sleep(12000);

        System.out.println("Buffer Injection Starts");

        the_selector.switchValve(15);
        Thread.sleep(1000);
        runNCyclesAtHighSpeed(1);

        the_selector.switchValve(17);
        Thread.sleep(1000);
        runNCyclesAtHighSpeed(1);

        the_selector.switchValve(19);
        Thread.sleep(1000);
        runNCyclesAtHighSpeed(1);

        the_selector.switchValve(21);
        Thread.sleep(1000);
        runNCyclesAtHighSpeed(1);

        the_selector.switchValve(9);
        Thread.sleep(1000);
        runNCyclesAtHighSpeed(1);

        the_selector.switchValve(11);
        Thread.sleep(1000);
        runNCyclesAtHighSpeed(1);

        the_selector.switchValve(13);
        Thread.sleep(1000);
        runNCyclesAtHighSpeed(1);

        System.out.println("Buffer Injection is done, time used : " + stopwatch.elapsed(TimeUnit.MINUTES) + " minutes");
    }

    public void startIncorp0() throws InterruptedException, FileNotFoundException, IOException {
        stopwatch.reset();
        stopwatch.start();

        //get start
        the_pump.intialize();
        Thread.sleep(12000);

        System.out.println("Incorp 0 Starts");

        System.out.println("Tris Wash Port 13 HighSpeed 2 times.");
        // Tris Wash
        the_selector.switchValve(13);
        Thread.sleep(1000);
        //Speed High
        the_pump.setMaxSpeed(10000);
        Thread.sleep(1000);
        runNCyclesAtHighSpeed(2);

        System.out.println("Incorp Mix Port 15 NormalSpeed 2 times.");
        // Incorp Mix
        the_selector.switchValve(15);
        Thread.sleep(1000);
        //normal speed
        the_pump.setMaxSpeed(3333);
        Thread.sleep(1000);
        runNCyclesAtNormSpeed(2);

        System.out.println("Color Dye Port 17 HighSpeed 1 time and NormalSpeed 1 time.");
        // Color Dye
        the_selector.switchValve(17);
        Thread.sleep(1000);
        //speed high
        the_pump.setMaxSpeed(10000);
        Thread.sleep(1000);
        runNCyclesAtHighSpeed(1);
        //normal speed
        the_pump.setMaxSpeed(3333);
        Thread.sleep(1000);
        runNCyclesAtNormSpeed(1);

        System.out.println("Time to take 220 seconds break.");
        //hold for 220 seconds break
        Thread.sleep(220000);

        System.out.println("Dark Dye Port 19 NormalSpeed 2 times.");
        // Dark Dye
        the_selector.switchValve(19);
        Thread.sleep(1000);
        //normal speed
        the_pump.setMaxSpeed(3333);
        Thread.sleep(1000);
        runNCyclesAtNormSpeed(2);

        System.out.println("Time to take 180 seconds break.");
        //hold for 180 seconds break
        Thread.sleep(180000);

        System.out.println("IM Buffer Port 21 HighSpeed 2 times.");
        // IM Buffer
        the_selector.switchValve(21);
        Thread.sleep(1000);
        //high speed
        the_pump.setMaxSpeed(10000);
        Thread.sleep(1000);
        runNCyclesAtHighSpeed(2);

        showMessage("Please select the FOVs", "FOV Selection");

    }

    public void cyc0LastStep() throws InterruptedException {
        System.out.println("IM Buffer Port 21 HighSpeed 2 times.");
        the_selector.switchValve(21);
        Thread.sleep(1000);
        the_pump.setMaxSpeed(10000);
        Thread.sleep(1000);
        runNCyclesAtHighSpeed(2);
        System.out.println("Incorp 0 is done, time used : " + stopwatch.elapsed(TimeUnit.MINUTES) + " minutes");
    }

    public void runSequencing(int counter) throws InterruptedException {

        stopwatch.reset();
        stopwatch.start();

        System.out.println("Incorp " + counter + " Starts");

        //get start
        the_pump.intialize();
        Thread.sleep(12000);

        //Cleavage
        System.out.println("Cleavage Buffer Port 9 HighSpeed 1 time and NormalSpeed 1 time.");
        the_selector.switchValve(9);
        Thread.sleep(1000);
        //high speed
        the_pump.setMaxSpeed(10000);
        Thread.sleep(1000);
        runNCyclesAtHighSpeed(1);
        //normal speed
        the_pump.setMaxSpeed(3333);
        Thread.sleep(1000);
        runNCyclesAtNormSpeed(1);

        System.out.println("It is time to take 280 seconds break.");
        //hold for 280 seconds break
        Thread.sleep(280000);
        System.out.println("Cleavage Buffer Port 21 NormalSpeed 1 time.");
        //normal speed
        runNCyclesAtNormSpeed(1);

        System.out.println("The second break will take 200 seconds.");
        //hold for 200 seconds break
        Thread.sleep(200000);

        System.out.println("High Salt Buffer Port 11 HighSpeed 6 times and NormalSpeed 2 times.");
        //high salt
        the_selector.switchValve(11);
        Thread.sleep(1000);
        //high speed
        the_pump.setMaxSpeed(10000);
        Thread.sleep(1000);
        runNCyclesAtHighSpeed(6);
        //normal speed
        the_pump.setMaxSpeed(3333);
        Thread.sleep(1000);
        runNCyclesAtNormSpeed(2);

        System.out.println("Tris Wash Port 13 HighSpeed 2 times.");
        // Tris wash
        the_selector.switchValve(13);
        Thread.sleep(1000);
        //high speed
        the_pump.setMaxSpeed(10000);
        Thread.sleep(1000);
        runNCyclesAtHighSpeed(2);

        System.out.println("Incorp Mix Port 15 NormalSpeed 2 times.");
        //Incorp Mix
        the_selector.switchValve(15);
        Thread.sleep(1000);
        //normal speed
        the_pump.setMaxSpeed(3333);
        Thread.sleep(1000);
        runNCyclesAtNormSpeed(2);

        System.out.println("Color Dye Port 17 HighSpeed 2 times and NormalSpeed 2 times.");
        //Color Dye
        the_selector.switchValve(17);
        Thread.sleep(1000);
        //high speed
        the_pump.setMaxSpeed(10000);
        Thread.sleep(1000);
        runNCyclesAtHighSpeed(1);
        //normal speed
        the_pump.setMaxSpeed(3333);
        Thread.sleep(1000);
        runNCyclesAtNormSpeed(1);

        System.out.println("Taking 3rd break for 220 seconds.");
        //hold for 220 seconds break
        Thread.sleep(220000);

        System.out.println("Dark Dye Port 19 NormalSpeed 2 times.");
        // Dark dye
        the_selector.switchValve(19);
        Thread.sleep(1000);
        //normal speed
        the_pump.setMaxSpeed(3333);
        Thread.sleep(1000);
        runNCyclesAtNormSpeed(2);

        System.out.println("Taking the final break for 180 seconds.");
        //hold for 180 seconds break
        Thread.sleep(180000);

        System.out.println("IM Buffer Port 21 HighSpeed 2 times.");
        // IM Buffer
        the_selector.switchValve(21);
        Thread.sleep(1000);
        //high speed
        the_pump.setMaxSpeed(10000);
        Thread.sleep(1000);
        runNCyclesAtHighSpeed(2);

        System.out.println("Incorp " + counter + " is done, time used : " + stopwatch.elapsed(TimeUnit.MINUTES) + " minutes");

    }

    //TODO: not working, need to fix
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