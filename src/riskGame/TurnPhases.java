package riskGame;

import java.io.Serializable;

public class TurnPhases implements Serializable{

	private static final long serialVersionUID = -4762646552098615837L;
	private String turnPhase;

	public TurnPhases(String turnPhase) {
		
		this.turnPhase = turnPhase;
		
	}

	public String getTurnPhase() {
		
		return turnPhase;
		
	}


	public void setTurnPhase(String turnPhase) {
		
		this.turnPhase = turnPhase.toLowerCase();
		
	}
}
