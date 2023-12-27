package game;

import abstractcharacter.AbstractCharacter;

public class Move {
	// Move class that represents the moves that have been occured in the game.
	// It can also give information about the moves.
	private int moveCounter;
	
	public Move() {
		moveCounter = 1;
	}
	
	public void showAttackMove(AbstractCharacter attacker, AbstractCharacter taker, int dealedDamage) {
		printCurrentMove();
		System.out.println(attacker.toName()+" attacks "+taker.toName()+", "+"deals "+dealedDamage+" damage.");
		System.out.println(taker);
	}
	
	public void showGuardMove(AbstractCharacter guarder) {
		printCurrentMove();
		System.out.println(guarder.toName()+" starts guarding.");
	}
	
	public void showSpecialAttackMove(AbstractCharacter attacker, AbstractCharacter taker, int dealedDamage) {
		printCurrentMove();
		System.out.println(attacker.toName()+" uses "+attacker.toSpecial()+" on "+taker.toName()+", deals "+dealedDamage+" damage.");
		System.out.println(taker);
		System.out.println(attacker);
	}
	
	public void showSpecialPassiveMove(AbstractCharacter specialUser) {
		printCurrentMove();
		System.out.println(specialUser.toName()+" uses "+specialUser.toSpecial());
	}
	
	public void showSpecialPassiveMoveFailed(AbstractCharacter specialUser) {
		printCurrentMove();
		System.out.println(specialUser.toName()+" failed to use "+specialUser.toSpecial());
	}
	
	public void showMoveOrder(AbstractCharacter moveUser) {
		printCurrentMove();
		System.out.println("It is the turn of "+moveUser.toName()+".");
	}
	
	private void printCurrentMove() {
		System.out.print("\nMove "+moveCounter+" - ");
	}
	
	public void showHumanAttackMove(AbstractCharacter attacker, AbstractCharacter taker, int dealedDamage) {
		printCurrentHumanMove();
		System.out.println(attacker.toName()+" attacks "+taker.toName()+", "+"deals "+dealedDamage+" damage.");
		System.out.println(attacker);
		System.out.println(taker);
	}
	
	public void showHumanPunchMove(AbstractCharacter attacker, AbstractCharacter taker, int dealedDamage) {
		printCurrentHumanMove();
		System.out.println(attacker.toName()+" punches "+taker.toName()+", "+"deals "+dealedDamage+" damage.");
		System.out.println(attacker);
		System.out.println(taker);
	}
	
	public void showHumanGuardMove(AbstractCharacter guarder) {
		printCurrentHumanMove();
		System.out.println(guarder.toName()+" starts guarding.");
	}
	
	public void showHumanSpecialAttackMove(AbstractCharacter attacker, AbstractCharacter taker, int dealedDamage) {
		printCurrentHumanMove();
		System.out.println(attacker.toName()+" uses "+attacker.toSpecial()+" on "+taker.toName()+", deals "+dealedDamage+" damage.");
		System.out.println(attacker);
		System.out.println(taker);
	}
	
	public void showHumanSpecialPassiveMove(AbstractCharacter specialUser) {
		printCurrentHumanMove();
		System.out.println(specialUser.toName()+" uses "+specialUser.toSpecial());
	}
	
	private void printCurrentHumanMove() {
		System.out.print("\nMove "+moveCounter+" Result: ");
	}
	
	
	public void showSkipMove(AbstractCharacter skipUser) {
		System.out.println("\n"+skipUser.toName()+" skips the turn");
	}
	
	public void incrementMoveCounter() {
		moveCounter++;
	}
	public int getMoveCounter() {
		return moveCounter;
	}
}
