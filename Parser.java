package centrolControl;
import java.util.*;
import java.io.*;


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
     * file path. The path must be the ENTIRE path.
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
            System.out.println("File not found! Please input another file path");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Parses the information in the input Scanner object
     * The instruction 'Wait user' is inputted as 'Wait 0'
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
                sec.put(head[1].trim(), new Integer(counter));
                sections.add(sec);

            } else {

                // Instruction lines are split at the tabs
                String[] instructn = line.split("\t");

                // Fields for the new instruction
                String name = "";
                List<Integer> pars = new ArrayList<Integer>();

                // Parses the line one token at a time
                for (int i = 0; i < instructn.length; i++) {
                	
                	// Token
                    String p = instructn[i].trim();
                    
                    if (p.isEmpty() || p.endsWith(".")) { // Ignores the number and empty tokens
                        continue;
                    } else if (p.toUpperCase().equals("WAIT")) {
                        name += "Wait";
                    } else if (p.toUpperCase().equals("SET")) {
                        name += "Set";
                    } else if (p.toUpperCase().equals("IMAGING")) {
                        name += "Imaging";
                    } else {
                        try {
                            Integer n = new Integer(p);
                            pars.add(n);
                        } catch (NumberFormatException e) {
                            if (name.isEmpty()) {
                                name = p;
                            } else if (p.toUpperCase().equals("USER")){
                                pars.add(-1);
                            } else {
                                System.out.println("One of the parameters is not a number!");
                                System.out.println(e.getMessage());
                                e.printStackTrace();
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
     * @return a List of Maps where the Keys are the names of the sections
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

    /**
     * Returns a String representation of all the Instructions in the same
     * format as the input.
     *
     * @return a String representation of the Instructions
     */
    @Override
    public String toString(){
        String out = "";
        Iterator<Map<String, Integer>> iter = sections.iterator();
        String section_name = "";
        Integer step = new Integer(-1);

        if (iter.hasNext()) {
        	Map<String, Integer> m = iter.next();
        	section_name = m.keySet().iterator().next();
            out += "\n@\t" + section_name + "\n\n";
        	if (iter.hasNext()) {
        		m = iter.next();
        		section_name = m.keySet().iterator().next();
        		step = m.get(section_name);
        	}
        }

        for (int i = 0; i < instr.size(); i++) {
            if (!sections.isEmpty() && step.equals(new Integer(i))) {
                out += "\n\n@\t" + section_name + "\n\n";
                if (iter.hasNext()) {
            		Map<String, Integer> m = iter.next();
            		section_name = m.keySet().iterator().next();
            		step = m.get(section_name);
            	}
            }
            out += "" + (i + 1) + ".\t" + instr.get(i).toString() + "\n";
        }

        return out;
    }
}
