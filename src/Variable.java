import java.util.ArrayList;

public class Variable implements Comparable<Variable> {
    private String name;
    private ArrayList<Integer> values;
    private ArrayList<Integer> legalValues;
    private boolean assigned;
    private int assignment;
    
    public Variable(String name, ArrayList<Integer> values) {
        this.name = name;
        this.values = values;
        this.legalValues = values; // at the beginning all values are legal
        this.assigned = false;
        this.assignment = Integer.MAX_VALUE; // value doesn't really matter
    }
    
    public String getName() {
        return this.name;
    }
    
    public ArrayList<Integer> getValues() {
        return this.values;
    }
    
    public boolean isAssigned() {
        return this.assigned;
    }
    
    public ArrayList<Integer> getLegalValues() {
        return this.legalValues;
    }
    
    public void assign(int value) {
        this.assignment = value;
        this.assigned = true;
    }
    
    public void unassign() {
        this.assigned = false;
    }
    
    /*
     * Integer instead of int because remove(int) removes at index
     */
    public void invalidateValue(Integer value) {
        this.legalValues.remove(value);
    }
    
    @Override
    public int compareTo(Variable other) {
        return this.name.compareTo(other.name);
    }
    
    public boolean equals(Variable other) {
        return this.name.equals(other.name);
    }
    
    public int getAssignment() {
        return assignment;
    }
    
    
}
