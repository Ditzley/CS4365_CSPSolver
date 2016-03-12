public class Constraint {
    private String lhs;
    private String rhs;
    private char op;
    
    public Constraint(String lhs, String rhs, char op) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }
    
    public String getRhs() {
        return this.rhs;
    }
    
    public String getLhs() {
        return this.lhs;
    }
    
    public boolean hasVariable(String x) {
        return x.equals(this.rhs) || x.equals(this.lhs);
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
    
    public boolean validate(Variable lhs, Variable rhs) {
        // if either is unassigned should return true, allows checking of validity.
        // otherwise no values could be assigned because they would be invalid.
        if(!rhs.isAssigned() || !lhs.isAssigned()) {
            return true;
        }
        return this.eval(lhs.getAssignment(), rhs.getAssignment());
    }
}
