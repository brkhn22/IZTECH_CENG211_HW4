package characters;

@SuppressWarnings("serial")
public class SpecialAlreadyUsedException extends Exception {

	
	public SpecialAlreadyUsedException() {
		super("Special has already been used!");
	}
}
