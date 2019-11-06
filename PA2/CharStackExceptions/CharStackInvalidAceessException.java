package CharStackExceptions;

public class CharStackInvalidAceessException extends Exception {
	public CharStackInvalidAceessException() {
		super("Invalid char stack position access.");
	}
}