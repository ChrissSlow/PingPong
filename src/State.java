public class State {
	// aktionen
	public double[] qValues = new double[3];
	public String name;
	public int lastAction;

	

	public int getBestActionIndex() {
		int highest = 0;
		for (int i = 0; i < qValues.length; i++) {
			if (qValues[i] > qValues[highest]) {
				highest = i;
			}
		}
		return highest;
	}
	
public int getActionWithEpsilonGreedy() {
		return 0;
	}

	public double getBestAction(){
		return qValues[getBestActionIndex()];
	}
	
	//public int getCountActions() {
	//	return qValues.length;
	//}

}
