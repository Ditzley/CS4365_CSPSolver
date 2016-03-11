import java.util.TreeMap;

public class Constraint {
    private Variable lhs;
    private Variable rhs;
    private char op;
    
    public Constraint(Variable lhs, Variable rhs, char op) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }
    
    public Variable getRhs() {
        return this.rhs;
    }
    
    public Variable getLhs() {
        return this.lhs;
    }
    
    public boolean hasVariable(Variable x) {
        return x.equals(this.rhs) || x.equals(this.lhs);
    }
    
    public boolean validifyAssignment(TreeMap<String, Variable> vars) {
        // if either is unassigned, fail
        int rhs = vars.get(this.rhs.getName()).getAssignment();
        int lhs = vars.get(this.lhs.getName()).getAssignment();
        if(rhs == Integer.MAX_VALUE || lhs == Integer.MAX_VALUE) {
            return false;
        } else {
            return this.eval(rhs, lhs);
        }
        
    }
    
    public boolean eval(int x, int y) {
        switch(this.op) {
            case ('='):
                return x == y;
            case ('<'):
                return x < y;
            case ('>'):
                return x > y;
            case ('!'):
                return x != y;
        }
        return false; // should never reach
    }
}
