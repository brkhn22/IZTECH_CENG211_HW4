package game;

@SuppressWarnings("serial")
public class NotAUniqueNameException extends Exception {

	public NotAUniqueNameException(String name) {
		super(name+" is not a unique name!");
	}
	
}
