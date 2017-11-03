package communicationObjects;

import riskGame.Country;

public class Fortify extends Action {

	private static final long serialVersionUID = -6165468403368006768L;
	
	private final Country fortifyFrom, fortifyTo;
	private final int fortifiedWith;
	
	public Fortify(String message, Country fortifyFrom, Country fortifyTo, int fortifiedWith) {
		super(message);
		
		this.fortifyFrom = fortifyFrom;
		this.fortifyTo = fortifyTo;
		this.fortifiedWith = fortifiedWith;
		
	}

	public Country getFortifyFrom() {
		return fortifyFrom;
	}

	public Country getFortifyTo() {
		return fortifyTo;
	}

	public int getFortifiedWith() {
		return fortifiedWith;
	}
}
