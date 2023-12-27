package characters;

public enum CharacterJob {
	// Character job enum list.
	HUNTER, SQUIRE, KNIGHT, VILLAGER;
	
	@Override
	public String toString() {
		String val = super.toString();
		return (val.substring(0, 1).toUpperCase()+val.substring(1).toLowerCase()).replace('ı', 'i');
	}
}
