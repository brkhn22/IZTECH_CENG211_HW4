package opponents;

import abstractcharacter.AbstractCharacter;
import characters.Human;

public abstract class Opponent extends AbstractCharacter {
	// Opponent class that extends AbstractCharacter.
	private OpponentAction currentAction;
	private double reducingDamageP;
	private int id;
	private OpponentType opponentType;
	private boolean isSpecial;
	private boolean isSkip;
	
	public Opponent(int id, OpponentType opponentType) {
				// gives values between (50, 150)
		super( (int)Math.round(Math.random()*100)+50,
				// gives values between (5, 25)
				(int)Math.round(Math.random()*20)+5,
				// gives values between (1, 90)
				(int)Math.round(Math.random()*89)+1);
		this.id = id;
		this.opponentType = opponentType;
		reducingDamageP = 1;
		isSpecial = false;
		isSkip = false;
	}
	
	public Opponent(int id, OpponentType opponentType, int points, int attack, int speed) {
		super(points, attack, speed);
		this.id = id;
		this.opponentType = opponentType;
	}

	// abstract special attack, that will be implemented by child classes.
	public abstract int specialAttack(double attackModifier, Human human);
	
	public int getDamage(int dealedDamage) {
		// gets damage and decreases the point.
		int damage = (int)Math.round(dealedDamage * reducingDamageP);
		setPoints(getPoints() - damage);
		return damage;
		
	}
	public void setAction() {
		// reset reducing damage.
		if(currentAction == OpponentAction.OPPONENT_GUARD)
			reducingDamageP = 1;
		// select a random action.
		OpponentAction[] actions = {OpponentAction.OPPONENT_ATTACK, OpponentAction.OPPONENT_GUARD, OpponentAction.OPPONENT_SPECIAL};
		int index = (int)Math.floor(Math.random()*actions.length);
		currentAction = actions[index];
		if(currentAction == OpponentAction.OPPONENT_SPECIAL)
			isSpecial = true;
	}


	public int attack(Human human) {
		return human.getDamage(getAttack());
	}
	
	public void guard() {
		this.reducingDamageP = 0.5;
	}
	
	public int getId() {
		return id;
	}
	
	public OpponentType getType() {
		return opponentType;
	}
	
	public OpponentAction getAction() {
		return currentAction;
	}
	
	public boolean isSpecial() {
		return isSpecial;
	}
	
	public boolean isSkip() {
		return isSkip;
	}
	
	void setSkip(boolean isSkip) {
		this.isSkip = isSkip;
	}
	
	void setSpecial(boolean special) {
		isSpecial = special;
	}
	
	@Override
	public String toString() {
		String str = "Opponent "+id+", Type: "+opponentType.toString()+", Points: "+getPoints();
		return str;
	}
	
	@Override
	public String toFullCharacterInfo() {
		String str = "Id: "+id+", Type: "+opponentType.toString()+", Points: "+
					getPoints()+", "+" Attack: "+getAttack()+", Speed: "+getSpeed();
	
		return str;
	}
	
	@Override
	public String toName() {
		return "Opponent "+id;
	}
}
