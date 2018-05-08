
package centrolControl.Parser;
import java.util.*;
import java.io.*;
import centrolControl.Instruction.Instruction;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;


/**
 * This class is used to parse instructions for running the SMI-seq experiments.
 * The instructions must be a text file in the correct format.
 *
 * @author Gu-Lab Kitty Li
 */
public class Parser {

    /**
     * The Parser object keeps track of all instructions in the file path as a 
     * list that doesn't depend on the sections listed in the file itself. 
     * Instead another list of maps keeps track of where each section starts in 
     * the instructions list. 
     */
    
    // The instruction file path
    private String path;

    // The list of all instructions
    private List<Instruction> instr;

    // The list of sections and its corresponding number of steps
    private List<Map<String, Integer>> sections;

    /**
     * Instantiates the Parser object by parsing all instructions in the given
     * file path
     *
     * @param path is a String representing the path of the Instruction file
     */
    public Parser(String path) {
        // Instantiates all fields
        this.path = path;
        this.instr = new ArrayList<Instruction>();
        this.sections = new ArrayList<Map<String, Integer>>();

        // Creates a new File object and Scanner
        File f = new File(this.path);
        try {
            Scanner input = new Scanner(f);
            parseFile(input);
        } catch (FileNotFoundException e){
            System.out.println("File not found! Please input another file path.");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Parses the information in the input Scanner object
     *
     * @param input is a Scanner object
     */
    private void parseFile(Scanner input) {

        // Keeps track of the number of instructions
        int counter = 0;

        // Loops through all the lines in the input file
        while (input.hasNext()) {

            // Gets the next line and removes white space on either end
            String line = input.nextLine().trim();

            if (line.isEmpty() || line.startsWith("#")) {

                // Ignores empty or comment lines
                continue;

            } else if (line.startsWith("@")) {

                // Creates a Key, Value pair of the section name
                // and the step that this section starts at
                // and put the pair into the list of sections
                Map<String, Integer> sec = new HashMap<String, Integer>();
                String[] head = line.split("\t");
                sec.put(head[1], new Integer(counter));
                sections.add(sec);

                System.out.println(sec.toString());

            } else {

                // Instruction lines are split at the tabs
                String[] instructn = line.split("\t");

                // Fields for the new instruction
                String name = "";
                List<Integer> pars = new ArrayList<Integer>();

                for (int i = 0; i < instructn.length; i++) {
                    String p = instructn[i];
                    if (p.isEmpty() || p.endsWith(".")) {
                        continue;
                    } else if (p.equals("Wait")) {
                        name += "Wait";
                    } else if (p.equals("Set")) {
                        name += "Set";
                    } else if (p.equals("Imaging")) {
                        name += "Imaging";
                    } else {
                        try {
                            Integer n = new Integer(p);
                            pars.add(n);
                        } catch (NumberFormatException e) {
                            if (name.isEmpty()) {
                                name = p;
                            } else {
                                System.out.println("One of the parameters is not a number!");
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                }

                instr.add(new Instruction(name, pars));

                // Increments the count of the number of instructions
                counter++;
            }
        }
    }

    /**
     * Gets the file path of this set of instructions
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets a List of the sections in order
     *
     * @return a List of Maps where the Keys are the Names of the sections
     *          the integers are the steps where that section starts
     */
    public List<Map<String, Integer>> getSections() {

        List<Map<String, Integer>> copy = new ArrayList<Map<String, Integer>>();

        for (Map<String, Integer> e : sections) {
            Map<String, Integer> ec = new HashMap<String, Integer>();
            ec.putAll(e);
            copy.add(ec);
        }

        return copy;
    }

    /**
     * Gets the instructions in this data set.
     * 
     * @return a List of Instructions (immutable)
     */
    public List<Instruction> getInstructions() {
        List<Instruction> copy = new ArrayList<Instruction>();

        for (Instruction e : instr) {
            copy.add(e);
        }

        return copy;
    }

    public static void main (String[] args){
        Parser newInstruct = new Parser("C:\\Users\\Nikon\\Documents\\NetBeansProjects\\sequencer\\micromanager\\mmstudio\\src\\centrolControl\\instruction.txt");
        List<Map<String, Integer>> sections = newInstruct.getSections();
        System.out.println(newInstruct.getPath());
        List<Instruction> instructions = newInstruct.getInstructions();
//        for (int i = 0; i < sections.size(); i++){
//            instruction = sections.get(i);
//        }
    }
}
