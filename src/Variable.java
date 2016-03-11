import java.util.ArrayList;


public class Variable implements Comparable<Variable> {
    private String name;
    private ArrayList<Integer> values;
    private int assignment;
    
    
    public Variable(String newName, ArrayList<Integer> newValues) {
        this.name = newName;
        this.values = newValues;
        this.assignment = Integer.MAX_VALUE;
    }
    
    public void assign(int x) {
        if(this.isValid(x)) {
            setAssignment(x);
        } else {
            System.out.println("Error: Invalid assignment");
        }
    }
    
    public boolean isValid(int x) {
        if(this.values.contains(x)) {
            return true;
        } else {
            return false;
        }
    }
    
    public void invalidate(int value) {
        this.values.remove(value);
    }
    
    /*
     * Getters
     */
    
    public int getAssignment() {
        return assignment;
    }
    
    public String getName() {
        return this.name;
    }
    
    public ArrayList<Integer> getValues() {
        return this.values;
    }
    
    
    /*
     * Setters
     */
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setValues(ArrayList<Integer> values) {
        this.values = values;
    }
    
    public void setAssignment(int assignment) {
        this.assignment = assignment;
    }
    
    
    /*
     * Comparisons
     */
    
    @Override
    public int compareTo(Variable other) {
        return this.name.compareTo(other.name);
    }
    
    public boolean equals(Variable other) {
        return this.name.equals(other.name);
    }
}
