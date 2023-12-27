package characters;

import abstractcharacter.*;
import opponents.Opponent;
import weapons.*;

public abstract class Human<W extends Weapon> extends AbstractCharacter implements Character<W> {
	// human class that extends abstract character and implements character interface.
	private W weapon;
	private double reducingDamageP;
	private int stamina, specialActionCounter;
	private boolean isSpecial;
	private String name;
	private CharacterJob job;
	private boolean isSkip;
	
	public Human(String name, CharacterJob job) {
				// gives values between (100, 150)
		super( ((int)Math.round(Math.random()*50))+100,
				// gives values between (20, 40)
				((int)Math.round(Math.random()*20))+20,
				// gives values between (10, 99)
				((int)Math.round(Math.random()*89))+10);
		initializeHuman();
		this.name = name;
		this.job = job;
	}

	private void initializeHuman() {
		setWeapon();
		this.stamina = 10;
		this.reducingDamageP = 1;
		this.specialActionCounter = 0;
		this.isSpecial = false;
	}

	@SuppressWarnings("unchecked")
	private void setWeapon() {
		// sets the weapon randomly.
		int rand = (int)Math.floor(Math.random()*3)+1;
		if(rand == 1) 
			weapon = (W) new Sword();
		else if(rand == 2)
			weapon = (W) new Spear();
		else
			weapon = (W) new Bow();		
	}
	
	public int getDamage(int dealedDamage) {
		// method that gets the damage.
		int damage = (int) Math.round((dealedDamage*reducingDamageP));
		setPoints(getPoints() - damage);
		return damage;
	}

	@Override
	public void guard() {
		// 75% of the damage reduced.
		reducingDamageP = 1-0.75;
		stamina += 3;
	}

	@Override
	public void run() throws CharacterRunsException {
		throw new CharacterRunsException();
		
	}
	
	
	@SuppressWarnings("hiding")
	@Override
	public <W extends Weapon> int attackWithWeapon(W weapon, HumanAction action, Opponent opponent) throws InsufficientStaminaException {
		int totalDamage = (int)Math.round(getWeaponDamage(weapon, action));
		return opponent.getDamage(totalDamage);
		
	}

	@Override
	public int punch(Opponent opponent) throws InsufficientStaminaException {
		int totalDamage = (int)Math.round(getPunch());
		return opponent.getDamage(totalDamage);
		
	}
	
	// Abstract methods will be implemented in the derived classes.
	public abstract int specialAction(HumanAction humanAction, double attackModifier, Opponent opponent) throws SpecialAlreadyUsedException, InsufficientStaminaException;
	
	void initSpecialAction() throws SpecialAlreadyUsedException {
		// method that inits the special action.
		if(getSpecialActionCounter()>0)
			throw new SpecialAlreadyUsedException();
		setSpecial(true);
		incrementSpecialActionCounter();
		// if it does not guard, than it must be running or attacking
		resetGuarding();
	}
	
	double getPunch() throws InsufficientStaminaException {
		// check whether it has enough stamina, if not throws InsufficientStaminaException.
		hasEnoughStamina(1);
		setStamina(getStamina() - 1);
		// if it does not guard, than it must be running or attacking
		resetGuarding();
		return getAttack()*0.8;
		
	}
	
	@SuppressWarnings("hiding")
	<W extends Weapon> double getWeaponDamage(W weapon, HumanAction action) throws InsufficientStaminaException {
		// method that returns weapon damage in given action.
		double totalDamage = 0;
		WeaponReturn weaponReturn = null;
		WeaponType weaponType = weapon.getWeaponType();
		
		if(action == HumanAction.HUMAN_ATTACK_WITH_WEAPON_1) {
			// if it is first action of the weapon.
			hasEnoughStamina(weaponType.getfirstStamina());
			weaponReturn = weapon.firstAttack(getAttack());
			// reduce the stamina.
			setStamina(getStamina()-weapon.getWeaponType().getfirstStamina());
		}else if (action == HumanAction.HUMAN_ATTACK_WITH_WEAPON_2) {
			// if it is second action of the weapon.
			hasEnoughStamina(weaponType.getSecondStamina());
			weaponReturn = weapon.secondAttack(getAttack());
			// reduce the stamina.
			setStamina(getStamina()-weapon.getWeaponType().getSecondStamina());
		}
		if(weaponReturn != null) {
			// set the total damage and whether isSkips the next turn.
			totalDamage = weaponReturn.getCombinedAttack();
			isSkip = weaponReturn.isSkip();
		}
		return totalDamage;
	}
	
	double getDamageOfAction(HumanAction action) throws InsufficientStaminaException, SpecialAlreadyUsedException {
		// gets damage respect to given action.
		if(isSpecial() && action == HumanAction.HUMAN_SPECIAL_ACTION)
			throw new SpecialAlreadyUsedException();
		double totalDamage;
		switch(action) {
		case HUMAN_PUNCH:{
			totalDamage = getPunch();
			break;
		}
		case HUMAN_ATTACK_WITH_WEAPON_1:{
			totalDamage = getWeaponDamage(getWeapon(), HumanAction.HUMAN_ATTACK_WITH_WEAPON_1);
			break;
		}
		case HUMAN_ATTACK_WITH_WEAPON_2:{
			totalDamage = getWeaponDamage(getWeapon(), HumanAction.HUMAN_ATTACK_WITH_WEAPON_2);
			break;
		}
		default:
			totalDamage = 0;
		}
		return totalDamage;
	}
	
	private void hasEnoughStamina(int staminaOff) throws InsufficientStaminaException {
		// checks whether action fits for the stamina.
		if(getStamina() - staminaOff < 0)
			throw new InsufficientStaminaException();
	}
	
	public W getWeapon() {
		return weapon;
	}

	public int getSpecialActionCounter() {
		return specialActionCounter;
	}

	private void incrementSpecialActionCounter() {
		specialActionCounter++;
	}
	
	public boolean isSpecial() {
		return isSpecial;
	}
	
	public void setSkip(boolean isSkip) {
		this.isSkip = isSkip;
	}

	void setSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}
	
	public int getStamina() {
		return stamina;
	}
	
	void setStamina(int stamina) {
		this.stamina = stamina;
	}
	
	public String getName() {
		return name;
	}
	
	public CharacterJob getJob() {
		return job;
	}
	
	public boolean isSkip() {
		return isSkip;
	}

	private void resetGuarding() {
		reducingDamageP = 1;
	}
	
	@Override
	public String toString() {
		String str = name+", Job: "+job.toString()+" Points: "+getPoints()+", Stamina: "+getStamina();
		return str;
	}
	
	@Override
	public String toFullCharacterInfo() {
		String str = name+", Job: "+job.toString()+", Points: "
				+getPoints()+", Stamina: "+getStamina()+", Attack: "+getAttack()
				+", Speed: "+getSpeed()+", Weapon: "+weapon.getWeaponType().toString()+" with +"
				+weapon.getAdditionalAttack()+" attack";
	
		return str;
	}
	
	@Override
	public String toName() {
		return getName();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		else if(!(o instanceof Human)) 
			return false;
		else {
			@SuppressWarnings("unchecked")
			Human<W> human = (Human<W>) o;
			return name.equals(human.getName());
		}
		
	}
	
	@Override
	public String toSpecial() {
		String str = job.toString();
		return str+" Special";
	}
}
