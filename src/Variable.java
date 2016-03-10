import java.util.ArrayList;


public class Variable implements Comparable<Variable> {
    private String name;
    private ArrayList<Integer> values;
    private int assignment;
    private int howConstrained;
    private int howConstraining;
    
    Variable(String newName, ArrayList<Integer> newValues) {
        this.name = newName;
        this.values = newValues;
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
    
    public int getHowConstrained(){
    	return this.howConstrained;
    }
    
    public int getHowConstraining(){
    	return this.howConstraining;
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
    
    public void setHowConstrained(int howConstrained){
    	this.howConstrained = howConstrained;
    }
    
    public void setHowConstraining(int howConstraining){
    	this.howConstraining = howConstraining;
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
