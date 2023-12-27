package opponents;

public enum OpponentType {
	
	SLIME, GOBLIN, WOLF, ORC;
	
	@Override
	public String toString() {
		String val = super.toString();
		return (val.substring(0, 1).toUpperCase()+val.substring(1).toLowerCase()).replace('ı', 'i');
	}
}
