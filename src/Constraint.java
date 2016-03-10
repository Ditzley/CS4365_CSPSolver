
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
