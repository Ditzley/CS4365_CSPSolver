import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CSPSolver {
    
    private static HashMap<String, Variable> vars;
    private static ArrayList<Constraint> cons;
    
    public static void main(String[] args) {
        if(args.length != 3) {
            throw new IllegalArgumentException("Expected 3 arguments");
        }
        // Variables
        vars = new HashMap<String, Variable>();
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
                
                cons.add(new Constraint(lhs.getName(), rhs.getName(), op));
            }
            
            br.close();
            
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        // Forward Checking or Not
        boolean forwardChecking = (args[2] == "fc");
        
        if(forwardChecking) {
            System.out.println(forwardCheckingSearch());
        } else {
            System.out.println(backtrackingSearch());
        }
    }
    
    public static boolean backtrackingSearch() {
        return recursiveBacktracking(new Assignment(vars, cons)); // start with empty assignment
    }
    
    public static boolean recursiveBacktracking(Assignment assignment) {
        // check if complete
        if(assignment.checkComplete()) {
            return true;
        }
        // if not complete get next unassigned variable
        Variable var = assignment.selectUnassigned();
        
        Integer value;
        // goes in order from least constraining to most constraining
        while((value = assignment.getLeastConstrainingValue(var)) != null) {
            
            // assign value, check if consistent
            if(assignment.isValueConsistent(var, value)) {
                // add to assignment
                assignment.assign(var, value);
                // do the search until failure or success
                if(recursiveBacktracking(assignment)) {
                    // on success
                    return true;
                } else {
                    // on failure
                    // unassign value
                    assignment.unassign(var, value);
                }
            } else {
                // value is not consistent, invalidate
                var.invalidateValue(value);
            }
        }
        
        return false;
    }
    
    public static boolean forwardCheckingSearch() {
        return recursiveForwardCheckingSearch(new Assignment(vars, cons));
    }
    
    public static boolean recursiveForwardCheckingSearch(Assignment assignment) {
        // check if complete
        if(assignment.checkComplete()) {
            return true;
        }
        
        // do forward checking here
        if(!assignment.forwardChecking()) {
            return false;
        }
        
        // if not complete get next unassigned variable
        Variable var = assignment.selectUnassigned();
        
        Integer value;
        // goes in order from least constraining to most constraining
        while((value = assignment.getLeastConstrainingValue(var)) != null) {
            
            // assign value, check if consistent
            if(assignment.isValueConsistent(var, value)) {
                // add to assignment
                assignment.assign(var, value);
                // do the search until failure or success
                if(recursiveForwardCheckingSearch(assignment)) {
                    // on success
                    return true;
                } else {
                    // on failure
                    // unassign value
                    assignment.unassign(var, value);
                }
            } else {
                // value is not consistent, invalidate
                var.invalidateValue(value);
            }
        }
        
        return false;
    }
}
