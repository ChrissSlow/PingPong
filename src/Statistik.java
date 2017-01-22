import java.util.ArrayList;

public class Statistik {
	// /////////////SIGLETON///////////////
	private static Statistik sharedInstance;

	public static Statistik getInstance() {
		if (Statistik.sharedInstance == null) {
			Statistik.sharedInstance = new Statistik();
		}
		return Statistik.sharedInstance;
	}

	private int countNegatives = 0;
	private int countPositives = 0;
	private ArrayList<Boolean> last1000 = new ArrayList<>();

	private Statistik() {
	}

	private void pp(boolean positive) {
		if (positive) {
			countPositives++;
			last1000.add(new Boolean(true));
		} else {
			countNegatives++;
			last1000.add(new Boolean(false));
		}

		if (last1000.size() > 1000) {
			last1000.remove(0);
		}

		if ((countNegatives + countPositives) % 1000 == 0) {
			getVerhaeltnisOfLast1000();
		}
	}

	private void getVerhaeltnisOfLast1000() {
		int pos = 0;
		int neg = 0;

		for (Boolean it : last1000) {
			if (it) {
				pos++;
			} else {
				neg++;
			}
		}

		System.out.println("+ " + pos + "\t\t - " + neg);
	}

	public void failurepp() {
		pp(false);
	}

	public void positivepp() {
		pp(true);
	}

	public int getCount() {
		return countNegatives + countPositives;
	}
}
