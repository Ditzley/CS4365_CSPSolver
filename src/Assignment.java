import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Assignment {
    
    private LinkedHashMap<String, Variable> variables;
    private ArrayList<Constraint> constraints;
    private int assigned;
    
    public Assignment(HashMap<String, Variable> variables, ArrayList<Constraint> constraints) {
        this.assigned = 0;
        this.variables = new LinkedHashMap<String, Variable>(variables);
        this.constraints = constraints;
    }
    
    public boolean checkComplete() {
        // if not all variables are assigned assignment is not complete
        if(assigned != variables.size()) {
            return false;
            
            // else return the validity of the assignment
        } else {
            return isValid();
        }
    }
    
    private boolean isValid() {
        for(Constraint c : constraints) {
            if(c.validate(variables.get(c.getLhs()), variables.get(c.getRhs()))) {
                continue;
            } else {
                return false;
            }
        }
        
        return true;
        
    }
    
    public boolean isValueConsistent(Variable var, Integer value) {
        // for each constraint
        for(Constraint c : constraints) {
            // see if var is a participant
            if(c.hasVariable(var.getName())) {
                if(c.getLhs() == var.getName()) {
                    if(!this.variables.get(c.getRhs()).isAssigned()) {
                        continue;
                    } else {
                        if(!c.eval(value, variables.get(c.getRhs()).getAssignment())) {
                            return false;
                        }
                    }
                } else if(c.getRhs() == var.getName()) {
                    if(!this.variables.get(c.getLhs()).isAssigned()) {
                        continue;
                    } else {
                        if(!c.eval(variables.get(c.getLhs()).getAssignment(), value)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    
    public void assign(Variable var, int value) {
        var.assign(value);
        this.assigned++;
    }
    
    public void unassign(Variable var, int value) {
        var.unassign();
        var.invalidateValue(value);
        this.assigned--;
    }
    
    public Variable selectUnassigned() {
        // get all unassigned variables
        ArrayList<Variable> unassigned = new ArrayList<Variable>();
        for(Variable var : variables.values()) {
            if(!var.isAssigned()) {
                unassigned.add(var);
            }
        }
        
        return this.getMostConstrained(unassigned);
        
    }
    
    /*
     * Get variable with fewest legal values
     */
    private Variable getMostConstrained(ArrayList<Variable> variables) {
        int min = Integer.MAX_VALUE;
        Variable mostConstrained = null;
        for(Variable var : variables) {
            if(var.getLegalValues().size() < min) {
                min = var.getLegalValues().size();
                mostConstrained = var;
            } else if(var.getLegalValues().size() == min) {
                mostConstrained = getMostConstraining(var, mostConstrained);
            }
        }
        
        return mostConstrained;
    }
    
    /*
     * Get variable with most constraints on remaining variables
     */
    private Variable getMostConstraining(Variable var1, Variable var2) {
        int var1Count = 0;
        int var2Count = 0;
        
        // for each constraint
        for(Constraint c : constraints) {
            // for var1
            // see if it is a participant
            if(c.hasVariable(var1.getName())) {
                // see if other participant is assigned or not
                if(((c.getLhs() == var1.getName()) && !variables.get(c.getRhs()).isAssigned())
                        || ((c.getRhs() == var1.getName())
                                && !variables.get(c.getLhs()).isAssigned())) {
                    // increment counter
                    var1Count++;
                }
            }
            
            // do again for var2
            // see if it is a participant
            if(c.hasVariable(var1.getName())) {
                // see if other participant is assigned or not
                if(((c.getLhs() == var2.getName()) && !variables.get(c.getRhs()).isAssigned())
                        || ((c.getRhs() == var2.getName())
                                && !variables.get(c.getLhs()).isAssigned())) {
                    // increment counter
                    var2Count++;
                }
            }
        }
        
        if(var1Count == var2Count) {
            // return the variable that occurs first alphabetically
            return var1.getName().compareTo(var2.getName()) > 0 ? var1 : var2;
        } else {
            // return the variable with higher count
            return var1Count > var2Count ? var1 : var2;
        }
    }
    
    /*
     * Order the values from the least constraining to most constraining
     * Least constraining being the value which rules out the least amount of values for the
     * remaining variables
     */
    public Integer getLeastConstrainingValue(Variable var) {
        // if no values remain
        if(var.getLegalValues().size() == 0) {
            return null;
        }
        
        ArrayList<Constraint> relevantConstraints = new ArrayList<Constraint>();
        // for each constraint
        for(Constraint c : constraints) {
            // see if var is a participant
            if(c.hasVariable(var.getName())) {
                // see if other participant is assigned or not
                if(((c.getLhs() == var.getName()) && !variables.get(c.getRhs()).isAssigned())
                        || ((c.getRhs() == var.getName())
                                && !variables.get(c.getLhs()).isAssigned())) {
                    relevantConstraints.add(c);
                }
            }
        }
        
        int min = Integer.MAX_VALUE;
        int leastConstraining = Integer.MAX_VALUE;
        for(int value : var.getLegalValues()) {
            int invalidatedValues = 0;
            
            for(Constraint c : relevantConstraints) {
                if(c.getLhs() == var.getName()) {
                    for(int p : variables.get(c.getRhs()).getLegalValues()) {
                        if(!c.eval(value, p)) {
                            invalidatedValues++;
                        }
                    }
                } else if(c.getRhs() == var.getName()) {
                    for(int p : variables.get(c.getLhs()).getLegalValues()) {
                        if(!c.eval(p, value)) {
                            invalidatedValues++;
                        }
                    }
                }
            }
            
            if(invalidatedValues < min) {
                min = invalidatedValues;
                leastConstraining = value;
            } else if(invalidatedValues == min) {
                if(value < leastConstraining) {
                    leastConstraining = value;
                }
            }
        }
        return leastConstraining;
    }
    
    public boolean forwardChecking() {
        ArrayList<Variable> assigned = new ArrayList<Variable>();
        ArrayList<Variable> unassigned = new ArrayList<Variable>();
        // split into assigned and unassigned
        for(Variable var : variables.values()) {
            if(var.isAssigned()) {
                assigned.add(var);
            } else {
                unassigned.add(var);
            }
        }
        
        for(Variable var : unassigned) {
            ArrayList<Integer> values = var.getLegalValues();
            
            // for remaining legal values
            for(int value : var.getLegalValues()) {
                for(Constraint c : constraints) {
                    // if var = lhs and rhs is assigned
                    if(c.getLhs() == var.getName()
                            && assigned.contains(variables.get(c.getRhs()))) {
                        if(!c.eval(value, variables.get(c.getRhs()).getAssignment())) {
                            var.invalidateValue(value);
                        }
                    } else if(c.getRhs() == var.getName()
                            && assigned.contains(variables.get(c.getLhs()))) {
                        if(!c.eval(variables.get(c.getRhs()).getAssignment(), value)) {
                            var.invalidateValue(value);
                        }
                    }
                }
            }
            
            // no legal values remain, terminate
            if(values.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
