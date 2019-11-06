package CharStackExceptions;

public class CharStackEmptyException extends Exception {
	public  CharStackEmptyException() {
		super("Cannot pop a value, Char Stack is empty.");
	}
}