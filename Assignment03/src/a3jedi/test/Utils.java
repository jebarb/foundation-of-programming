package a3jedi.test;

public class Utils {
	/**
	 * Returns true only if all inputs are also true.
	 * 
	 * Note: this is vacuously true if the input array is empty
	 */
	public static boolean all(boolean[] values) {
		// This short-circuits over the values - the first false value returns
		// false overall
		for (int i = 0; i < values.length; i++) {
			if (!values[i])
				return false;
		}

		return true;
	}
}
