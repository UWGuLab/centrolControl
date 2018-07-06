package centrolControl;
import java.util.*;

import java.io.*;


/**
 * This class is used to parse instructions for running the SMI-seq experiments.
 * The instructions must be a text file in the correct format. This is a mutable
 * object.
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

    // The list of sections and its corresponding starting step number (0 based)
    private List<Map<String, Integer>> sections;
    
    /**
     * Instantiates the Parser object without knowing the instruction set
     */
    public Parser() {
    	
    	// Instantiates all fields
    	this.path = null;
    	this.instr = new ArrayList<Instruction>();
        this.sections = new ArrayList<Map<String, Integer>>();
    }

    /**
     * Instantiates the Parser object by parsing all instructions in the given
     * file path. The path must be the ENTIRE path.
     *
     * @param path is a String representing the path of the Instruction file
     */
    public Parser(String path) {
        parseFile(path);
    }
    
    /**
     * Parses the file with the given path
     * 
     * @param path is a String representing the path of the Instruction file
     */
    public void parseFile(String path) {
    	
    	// Instantiates all fields
    	this.path = path;
        this.instr = new ArrayList<Instruction>();
        this.sections = new ArrayList<Map<String, Integer>>();
    	
    	// Creates a new File object and Scanner
        File f = new File(this.path);
        try {
            Scanner input = new Scanner(f);
            parseFile(input);
            input.close();
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
                sec.put(head[1].trim().toUpperCase(), new Integer(counter));
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
                    
                    if (p.isEmpty() || p.endsWith(".")) { // Ignores the step number and empty tokens
                        continue;
                    } else if (p.toUpperCase().equals("WAIT")) {
                        name = "WAIT";
                    } else if (p.toUpperCase().equals("SET")) {
                        name = "SET";
                    } else if (p.toUpperCase().equals("IMAGING")) {
                        name = "IMAGING";
                    } else {
                        try {
                            Integer n = new Integer(p);
                            pars.add(n);
                            if (pars.size() == 2 && (n > 5 || n < 0)) {
                            	throw new IllegalArgumentException("The speed cannot be greater than 5 or less than 0.");
                            }
                        } catch (NumberFormatException e) {
                            if (name.isEmpty()) {
                                name = p;
                            } else if (p.toUpperCase().equals("USER")){
                                pars.add(new Integer(-1));
                                break;
                            } else {
                                System.out.println("One of the parameters is not an integer!");
                                System.out.println(e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                    
                    if (pars.size() == 3 || (pars.size() == 1 && (name.equals("SET") || name.equals("WAIT")))) {
                    	break;
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
    public List<Instruction> getAllInstructions() {
        List<Instruction> copy = new ArrayList<Instruction>();

        for (Instruction e : instr) {
            copy.add(e);
        }

        return copy;
    }
    
    /**
     * Gets the instructions for one section specified by name
     * 
     * @param section name is a String representing the section
     * @return a List of Instructions for the specified section or NULL if there is no such section
     */
    public List<Instruction> getSectionInstructions(String sectionName) {

    	sectionName = sectionName.toUpperCase();
    	
    	// Finds the section number
    	Iterator itr = sections.iterator();
    	while (itr.hasNext()) {
    		
    		// Gets the next section
    		Map<String, Integer> sectionMap = (Map<String, Integer>) itr.next();
    		Integer start = sectionMap.get(sectionName);
    		
    		if (start == null) {
    			continue;
    		} else {
    			if (itr.hasNext()) {
    				Map<String, Integer> sectionMapNext = (Map<String, Integer>) itr.next();
    				Collection<Integer> endList = sectionMapNext.values();
                                Integer end = endList.iterator().next();

    				return instr.subList(start, end);
    			} else {
    				return instr.subList(start, instr.size());
    			}
    		}
    		
    	}
    	return null;
    	
    }
 
    
    /**
     * Checks if this Parser object already has Instructions or not
     */
    public boolean isEmpty() {
    	if (this.path == null && this.instr.isEmpty() && this.sections.isEmpty()) {
    		return true;
    	}
    	return false;
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
