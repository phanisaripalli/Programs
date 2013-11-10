using System;
using System.Text.RegularExpressions;

namespace Cut
{
	// An abstract class. Concrete classes are written for individual commands
	public abstract class Command
	{
		int[][] partitions;

		public abstract void runCommand();

		// Checks the options provided are valid, sytactically
		public bool isValidPattern(String pattern) {

			String[] parameters = pattern.Split (new string[] {","}, StringSplitOptions.RemoveEmptyEntries);
			partitions = new int[parameters.Length][];
			bool hasValidBounds = true;

			for (int i = 0; i < parameters.Length; i++) {
				String parameter = parameters [i];

				int startIndex = 0;
			    int endIndex = 0;

				// Uses a regex matching to determine if the boudaries or options provided to -b, -f are right.
				if (Regex.IsMatch (parameter, @"(^[0-9]+[\-]{1}[0-9]+$)")) {
					string[] parts = new string[]{}; 
					parts = parameter.Split (new string[] {"-"}, StringSplitOptions.RemoveEmptyEntries);
					//-f1-3,6- /Users/phanisaripalli/Desktop/task.txt
					startIndex = int.Parse (parts [0]);
					endIndex = int.Parse (parts [1]);
					partitions [i] = new int[]{startIndex, endIndex};

				} else if (Regex.IsMatch (parameter, @"(^[\-]{1}[0-9]+$)")) {
					startIndex = 1;
					endIndex = int.Parse (parameter.Substring(1));
					partitions [i] = new int[]{startIndex, endIndex};
				} else if (Regex.IsMatch (parameter, @"(^[0-9]+[\-]{1}$)")) {
					startIndex = int.Parse (parameter.Substring (0, parameter.Length - 1));
					endIndex = -1;
					partitions [i] = new int[]{startIndex, endIndex};
				} else if (Regex.IsMatch (parameter, @"(^[0-9]+$)")) {		
					startIndex = int.Parse (parameter);
					endIndex = int.Parse (parameter);
					partitions [i] = new int[]{startIndex, endIndex};
				} else {
					hasValidBounds = false;
				}
				if (startIndex < 1 ) {
					hasValidBounds = false;
					break;
				}
			}

			return hasValidBounds;

		}

		// Returns a multidimensional array that represents number of partitions for each 
		//line to read and the boundaries for each partition.
		public int[][] getPatternIterationPartitions() {
			return partitions;
		}

	}


}

