import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.sun.javafx.collections.MappingChange.Map;

public class CSPSolver {
    
    private static TreeMap<String, Variable> vars;
    private static ArrayList<Constraint> cons;
    
    public static void main(String[] args) {
        // Variables
        vars = new TreeMap<String, Variable>();
        String varFile = args[0];
        try {
            BufferedReader br = new BufferedReader(new FileReader(varFile));
            String line;
            while((line = br.readLine()) != null) {
                // Get variable data, initialize, and put into map
                String[] split = line.split(":");
                String name = split[0];
                String[] stringValues = split[1].trim().split("\\s+"); // split at whitespaces
                ArrayList<Integer> values = new ArrayList<Integer>();
                for(String v : stringValues) {
                    values.add(Integer.parseInt(v));
                }
                
                vars.put(name, new Variable(name, values));
            }
            
            br.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        // Constraints
        cons = new ArrayList<Constraint>();
        String conFile = args[1];
        try {
            BufferedReader br = new BufferedReader(new FileReader(conFile));
            String line;
            while((line = br.readLine()) != null) {
                // Get constraint data, initialize, and put into list
                String[] split = line.split("\\s+"); // split at whitespaces
                String lhsName = split[0];
                String rhsName = split[2];
                char op = split[1].toCharArray()[0];
                
                Variable lhs = vars.get(lhsName);
                Variable rhs = vars.get(rhsName);
                
                if(lhs == null || rhs == null) {
                    throw new IllegalArgumentException("Constraint variable not in variable file");
                }
                
                cons.add(new Constraint(lhs, rhs, op));
            }
            
            br.close();
            
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        // Forward Checking or Not
		boolean forwardChecking = (args[2] == "fc");
		
		
    }
    
    String returnMostConstrained(TreeMap<String, Variable> vars, ArrayList<Constraint> cons){
		int minValues = 100000000;
		String mostConstrained = "";
		int possibleValues;
    	
    	for(Entry<String, Variable> entry : vars.entrySet()){
			possibleValues = ((entry.getValue()).getValues()).size();
			//This line is terrible. It sets possibleVaules to the number of possible
			//values that the variable can take. Java's dumb.
			if (possibleValues < minValues){
				minValues = possibleValues;
				mostConstrained = entry.getKey();
			}
			
			else if (possibleValues == minValues){
				mostConstrained = returnMostConstraining(vars, cons, mostConstrained, entry.getKey());
			}
		}
  
    	return mostConstrained;
    }

	String returnMostConstraining(TreeMap<String, Variable> vars, ArrayList<Constraint> cons, String key1, String key2) {
		Variable var1 = vars.get(key1);
		Variable var2 = vars.get(key2);
		
		int var1Count = 0;
		int var2Count = 0;
		//These ints hold how many constraints each Variable features in, in which it constrains a currently unassigned variable
		
		for(Constraint c : cons){
			if((c.getLhs() == var1 && (vars.get((c.getRhs()).getName())).getAssignment() == 2147483647) || (c.getRhs() == var1 && (vars.get((c.getLhs()).getName())).getAssignment() == 2147483647)){
				/* Ooh, boy, this nightmare of a line probably needs explaining.
				 * 
				 * Basically what this is saying is that if the left hand side of the constraint is var1, and the right hand side is an unassigned variable, or if the sides are flipped, this is true
				 * The reason that looks so gruesome is I had to get the rhs of c, a variable, get it's name, find that same Variable in the TreeMap, then check that Variable's assignment to see if it's
				 * the default, the max value of an unsigned int (which, admittedly, I'm banking on one of the test files including)
				 */
				
				var1Count++;//lol
			}
			
			else if((c.getLhs() == var2 && (vars.get((c.getRhs()).getName())).getAssignment() == 2147483647) || (c.getRhs() == var2 && (vars.get((c.getLhs()).getName())).getAssignment() == 2147483647)){
				//DON'T PANIC. Same line as above, just replace all instances of var1 with var2. Don't panic.
				
				var2Count++;
			}

		}

		if(var1Count > var2Count){//If var1 is more constraining than var 2
			return key1;
		}
		else if (var1Count < var2Count){//Else if var2 is more constraining than var1
			return key2;
		}
		else if (key1.charAt(0) < key2.charAt(0)){//If both values are equally constraining, if key1 is alphabetically before key2
			return key1;
		}
		else {//else if key2 is alphabetically before key1
			return key2;
		}
	}
	
	/* Least Constraining Value Logic
	 * Note: The variable we are currently assigning a value to will arbitrarily be called v
	 * 1. Determine all values of v which are currently valid to assign to it
	 * 	- This will exist as another, seperate function, because it will also be useful for doing forward checking
	 * 2. Identify all constraints which contain both v and another, unassigned variable
	 * 3. For all possible values of v, for all constraints satisfying rule 2
	 * 	- See how many values of the other, unassigned value become invalid if v is given the chosen value
	 * 4. Assign v the value which is the least constraining
	 */
	
	Integer leastConstrainingValue(TreeMap<String, Variable> vars, ArrayList<Constraint> cons, String key){
		ArrayList<Integer> validAssignments = validAssignments(vars, cons, key);
		ArrayList<Constraint> relevantConstraints = new ArrayList<Constraint>();
		Variable var = vars.get(key);
		
		for(Constraint c : cons){//for every constraint
			if(c.getLhs() == var || c.getRhs() == var){//if one of its sides is our variable
				if(c.getLhs().getAssignment() == 2147483647 || c.getRhs().getAssignment() == 2147483647){//and the other is unassigned
					relevantConstraints.add(c);//mark that constraint as relevant
				}
			}
		}
		
		int leastInvalidatedValues = 10000000;
		int leastConstrainingValue = 10000000;
		Variable unassignedVar;
		
		for(Integer v : validAssignments){
			int invalidatedValues = 0;
			int value = v.intValue();
			for(Constraint r : relevantConstraints){
				if(r.getLhs() == var){
					unassignedVar = r.getRhs();
					for(Integer p : unassignedVar.getValues()){
						if(!r.eval(value, p.intValue())){
							invalidatedValues++;
						}
					}
				}
				else{
					unassignedVar = r.getLhs();
					for(Integer p : unassignedVar.getValues()){
						if(!r.eval(p.intValue(), value)){
							invalidatedValues++;
						}
					}
				}
			}
			
			if(invalidatedValues < leastInvalidatedValues){
				leastInvalidatedValues = invalidatedValues;
				leastConstrainingValue = value;
			}
		}
		
		return leastConstrainingValue;
		
		
		
	}
    
	ArrayList<Integer> validAssignments(TreeMap<String, Variable> vars, ArrayList<Constraint> cons, String key){
		Variable var = vars.get(key);//determine the variable identified by the key
		ArrayList<Integer> validAssignments = var.getValues();//create an ArrayList of valid assignments for var. Defaults to all possible assignments of var
		
		for(Constraint c : cons){//for every constraint
			if(c.getLhs() == var && c.getRhs().getAssignment() != 2147483647){//if the constraint has var as its left side and an assigned variable on its right
				for(Integer v : validAssignments){//for all possible values of var
					if (!c.eval(v, c.getRhs().getAssignment())){//if that value would invalidate this constraint, remove it from validAssignments
						validAssignments.remove(v);//POSSIBLE BUG: This might delete the object at index v, instead of object v
					}
				}
			}
			else if(c.getRhs() == var && c.getLhs().getAssignment() != 2147483647){//else if the constraint has var as its right side and an assigned variable on its left
				for(int v : validAssignments){//for all possible values of var
					if (!c.eval(c.getLhs().getAssignment(), v)){//if that value would invalidate this constraint, remove it from validAssignments
						validAssignments.remove(v);
					}
				}
			}
		}
		
		return validAssignments;//return all assignments to var which would not invalidate any constraints
	}
}

