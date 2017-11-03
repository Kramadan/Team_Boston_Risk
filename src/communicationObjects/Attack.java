package communicationObjects;

import riskGame.Country;

public class Attack extends Action {

	private static final long serialVersionUID = -5171689819052346620L;
	
	private final Country attackFrom, target;
	private final int[] attackResult;
	
	public Attack(String message, Country attackFrom, Country target, int[] attackResult) {
		
		super(message);
		this.attackFrom = attackFrom;
		this.target = target;
		this.attackResult = attackResult;
		
	}

	public Country getAttackFrom() {
		return attackFrom;
	}

	public Country getTarget() {
		return target;
	}

	public int[] getAttackResult() {
		return attackResult;
	}
}
