package characters;

@SuppressWarnings("serial")
public class CharacterRunsException extends Exception {
	public CharacterRunsException() {
		super("The character escaped from the game.");
	}
	
}
