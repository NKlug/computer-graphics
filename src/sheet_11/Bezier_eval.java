package sheet_11;

public class Bezier_eval {

	public static float[] eval(float[] control_points, float t) {
		float[] ret = new float[3];
		int degree = control_points.length / 3 - 1;
		for (int i = 0; i < 3; i++) {
			ret[i] = 0;
		}
		for (int i = 0; i <= degree; i++) {
			for (int j = 0; j < 3; j++) {
				ret[j] = ret[j] + binom(i, degree) * pow(1 - t, degree - i)
						* pow(t, i) * control_points[3 * i + j];
			}
		}
		return ret;
	}

	private static int binom(int i, int n) {
		if (i == 0 || i == n) {
			return 1;
		}
		int zaehler = n;
		for (int j = 1; j < i; j++) {
			zaehler = zaehler * (n - j);
		}
		return zaehler / (fac(i));
	}

	private static int fac(int n) {
		int ret = 1;
		for (int i = 1; i <= n; i++) {
			ret = ret * i;
		}
		return ret;
	}

	private static float pow(float base, int exponent) {
		float ret = 1;
		for (int i = 1; i <= exponent; i++) {
			ret = ret * base;
		}
		return ret;
	}

}
