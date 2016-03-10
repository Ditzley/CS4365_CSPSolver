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

	private String returnMostConstraining(TreeMap<String, Variable> vars, ArrayList<Constraint> cons, String key1, String key2) {
		// TODO Auto-generated method stub
		return null;
	}
    
}

