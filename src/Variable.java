import java.util.Arrays;


public class Variable {
	char name;
	int[] values;
	int assignment;
	
	Variable(char newName, int[] newValues){
		this.name = newName;
		this.values = newValues;
	}
	
	public void assign(int x){
		if(this.isValid(x)){
			assignment = x;
		}
		else{
			System.out.println("Error: Invalid assignment");
		}
	}
	
	public boolean isValid(int x){
		if(Arrays.asList(this.values).contains(x)){
			return true;
		}
		else{
			return false;
		}
	}
}
