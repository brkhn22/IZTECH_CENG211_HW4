package characters;

@SuppressWarnings("serial")
public class InsufficientStaminaException extends Exception {

	public InsufficientStaminaException() {
		super("The stamina is not sufficient!");
	}
}
