using System;
using System.Text;

namespace Cut
{
	public class FieldsCommand : Command
	{
		private String pathToFile;
		private String pattern = "\t";
		private String delimitter;

		public FieldsCommand (String args0, String args1, String args2)
		{

			this.pathToFile = args2;

			this.pattern = args1.Substring (2);
			this.delimitter = args0.Substring (2);

		}

		//Parses and prints the output.
		private void parseCommand ()
		{
			bool isDelimitterValid = true;
			bool hasValidBounds = true;
			if (delimitter.Length < 1 || delimitter.Contains ("\"") || delimitter.Contains ("\'")) {
				isDelimitterValid = false;
			} 

			if (isDelimitterValid) {

				hasValidBounds = isValidPattern (this.pattern);
				if (hasValidBounds) {

					int[][] partitions = getPatternIterationPartitions ();

					printOutput(partitions);

				}
			}
		}

		public override void runCommand ()
		{
			parseCommand();
		}

		//Executes the command and prints the bytes
		public void printOutput (int[][] partitions)
		{
			System.IO.StreamReader file = new System.IO.StreamReader (pathToFile);
			String line;
			int counter = 0;

			while ((line = file.ReadLine()) != null) {
				String[] fields = line.Split (new string[] {delimitter}, StringSplitOptions.RemoveEmptyEntries);
				if (fields.Length == 1) {
					Console.WriteLine(line);
				}

				for (int x = 0; x < partitions.Length && fields.Length > 1 && fields.Length >= partitions[x][0]; x++) {
					int maxBound = partitions[x][1];
					int minBound = partitions[x][0];
					if (partitions [x] [1] > fields.Length || partitions[x][1] == -1) {
						minBound = fields.Length;
						maxBound = fields.Length;
					}				
					for (int i = minBound - 1; i < maxBound; i++) {

						Console.Write (fields[i]);
						if (i + 1 != maxBound) {
							Console.Write(delimitter);
						}
					}
					if (x + 1 != partitions.Length) {
						Console.Write(delimitter);
					}

				}

				Console.WriteLine ();
				counter++;

			}

			file.Close ();
		}

	}
}

