import java.util.ArrayList;
import java.util.Random;

// 1. Instanz besorgen
// while learning{
// getActionEGreedy
// setNewStateAndUpdateQValueOfLastState 
// }
public class Agent {
	// /////////////SIGLETON///////////////
	private static Agent sharedInstance;

	public static Agent getInstance() {
		if (Agent.sharedInstance == null) {
			Agent.sharedInstance = new Agent(5, 6, 5, 1, 1);
		}
		return Agent.sharedInstance;
	}

	// ///////////PRIVATE STUFF/////////////
	private State currentState = null;
	private int xBallMax = 11, yBallMax = 12, xSchlaegerMax = 9, xVMax = 2, yVMax = 2;
	private int anzahlZustaende = xBallMax * yBallMax * xSchlaegerMax * xVMax * yVMax;
	private ArrayList<State> states = new ArrayList<>(anzahlZustaende);
	private Random random = new Random();

	private final double ALPHA = 0.1;
	private final double DISCOUNTFACTOR = 0.90;
	private final double EPSILON_PROBABILITY = 0.1; // %

	private Agent(int xBall, int yBall, int xSchlaeger, int xV, int yV) {
		for (int i = 0; i < anzahlZustaende; i++) {
			states.add(new State());
		}

		// init Qvalues of each state
		this.states.forEach((it) -> {
			for (int i = 0; i < it.qValues.length; i++) {
				// TODO
				double smallRandomVal = random.nextDouble();

				it.qValues[i] = smallRandomVal;

			}
			it.name = "" + random.nextInt();
		});

		// setup initial state
		currentState = states.get(getZustandIndex(xBall, yBall, xSchlaeger, xV, yV));
	}

	private int getZustandIndex(int xBall, int yBall, int xSchlaeger, int xV, int yV) {
		xV = xV == 1 ? 1 : 0;
		yV = yV == 1 ? 1 : 0;

		int xBallStep = xBall * yBallMax * xSchlaegerMax * xVMax * yVMax;// 440
		int yBallStep = yBall * xSchlaegerMax * xVMax * yVMax;// 40
		int xSchlaegerStep = xSchlaeger * xVMax * yVMax;// 4
		int xVStep = xV * yVMax;
		int yVStep = yV;
		int erg = xBallStep + yBallStep + xSchlaegerStep + xVStep + yVStep;
		return erg;
	}

	private int getActionEGreedy() {
		// hole action nach egreedy vom aktuellen state

		int bestIndex = currentState.getBestActionIndex();

		double randNum = random.nextDouble();

		double sum = 0;
		int index = random.nextInt(currentState.qValues.length);

		while (sum < randNum) {
			index = index + 1;
			index = index % (currentState.qValues.length);

			if (index == bestIndex) {
				sum = sum + (1 - EPSILON_PROBABILITY);
			} else {
				sum = sum + (EPSILON_PROBABILITY / 2);
			}
		}

		currentState.lastAction = index;
		return index;
	}

	private int getBestAction() {
		int bestIndex = currentState.getBestActionIndex();
		currentState.lastAction = bestIndex;
		return bestIndex;
	}

	// //////////SCHNITTSTELLE/////////////

	public int getAction(boolean learning) {
		if (learning) {
			return getActionEGreedy();
		} else {
			return getBestAction();
		}
	}

	public void setNewStateAndUpdateQValueOfLastState(int xBall, int yBall, int xSchlaeger, int xV, int yV, double reward, boolean learning) {
		// update qvalue for last state
		// System.out.println("getZustandIndex(xBall:" + xBall + ", yBall:" +
		// yBall + ", xSchlaeger:" + xSchlaeger + ", xV:" + xV + ", yV:"
		// + yV + ");");
		int indexNewState = getZustandIndex(xBall, yBall, xSchlaeger, xV, yV);
		State newState = states.get(indexNewState);

		if (learning)
			currentState.qValues[currentState.lastAction] = currentState.qValues[currentState.lastAction] + ALPHA
					* (reward + DISCOUNTFACTOR * newState.getBestAction() - currentState.qValues[currentState.lastAction]);

		// aendere aktuellen state zu neuem state
		currentState = newState;
	}
}
