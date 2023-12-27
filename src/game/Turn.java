package game;

public class Turn {
	// class that gives information about a turn.
	
	private String ownerName;
	private int ownerId;
	private double attackModifier;
	
	
	public Turn(String turnOwner, double attackModifier) {
		this.ownerName = turnOwner;
		this.ownerId = -1;
		this.attackModifier = attackModifier;
	}
	public Turn(int ownerId, double attackModifier) {
		this.ownerId = ownerId;
		this.ownerName = "";
		this.attackModifier = attackModifier;
		
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public double getAttackModifier() {
		return attackModifier;
	}
	
	public int getOwnerId() {
		return ownerId;
	}
	
	public boolean isOpponent() {
		return ownerId != -1;
	}
	
	@Override
	public String toString() {
		if(isOpponent())
			return "Opponent "+ownerId;
		else
			return ownerName;
	}
}
