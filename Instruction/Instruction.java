
package centrolControl.Instruction;
import java.util.*;

/**
 * This class represents an instruction that can be executed. See below for
 * more specifics on what is in the different instruction types and 
 * its parameters.
 *
 * @author Gu-Lab, Kitty Li
 */
public class Instruction {

    /**
     * There are different types of Instruction objects and they each have
     * different field values and parameter list lengths:
     *
     * 1. Pumping the valve Instructions
     *      name = name of the chemical for this valve
     *      parameters = value #, speed setting #, volume amount in microliters
     * 2. Wait Instructions
     *      name = "Wait"
     *      parameters = time in milliseconds
     * 3. Set Instructions
     *      name = "Set"
     *      parameters = valve #
     * 4. Imaging Instructions
     *      name = "Imaging"
     *      parameters = *empty list*
     */

    // The name of the chemical in the valve or an instruction name
    private String name;
    // The parameters of the instruction
    private List<Integer> parameters;

    /**
     * Instantiates the Instruction
     *
     * @param name is the name/Instruction type of this instruction as a String
     * @param parameters is a List of Doubles of the parameters or an empty List
     */
    public Instruction(String name, List<Integer> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    /**
     * Gets the parameters of the instruction
     *
     * @return the parameters as a LinkedList of Doubles
     * @return an empty array if there are no parameters
     */
    public List<Integer> getParameters() {

        // Returns an empty array
        if (parameters.isEmpty()) {
            return new ArrayList<Integer>();
        }

        List<Integer> copy = new ArrayList<Integer>();
        for (Integer d : parameters){
            copy.add(d);
        }

        return copy;
    }

    /**
     * Gets the name/Instruction type of this Instruction
     * @return name as a String
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if this Instruction is equal to another Instruction
     *
     * @param other is another Instruction
     * @return true if this Instruction is exactly equal to another
     */
    public boolean equals(Instruction other) {
        return this.getName().equals(other.getName())
                && this.getParameters().equals(other.getParameters());
    }

}
