
package TestParser;
import Instruction.Instruction;
import Parser.Parser;

import java.util.*;

/**
 * Runs tests on the Parser and Instruction classes
 * @author Gu-Lab
 */
public class TestParser {
	
	private static List<Integer> FAKE_V;
	private static List<Integer> FAKE_W;
	private static List<Integer> FAKE_W_u;

	private static List<Integer> FAKE_S;

	
	public static void main(String[] args) {
		testSetup();
		testInstructionV();
		testParser();
		
	}
	
	public static void testSetup() {
		FAKE_V = new ArrayList<Integer>();
		FAKE_V.add(1);
		FAKE_V.add(2);
		FAKE_V.add(3);
		
		FAKE_W = new ArrayList<Integer>();
		FAKE_W.add(1);
		
		FAKE_W_u = new ArrayList<Integer>();
		FAKE_W_u.add(1);
		
		FAKE_S = new ArrayList<Integer>();
		FAKE_S.add(1);
		
		
		
	}
	
	public static void testInstructionV() {
		Instruction ins  = new Instruction("name", FAKE_V);
		System.out.println(ins.toString());
		System.out.println();
		System.out.println();
		System.out.println();
		
	}
	
	public static void testParser() {
		Parser par = new Parser("C:\\Users\\kitka\\Documents\\UW\\Gu-lab\\Sequencing Parser\\ConfigurationParser\\test\\TestParser\\Instructions.txt");
		System.out.println(par.toString());
		
		Scanner s = new Scanner(System.in);
		
		while(!s.hasNext()) {
			System.out.println("No input");
		}
	}
	
	

}
