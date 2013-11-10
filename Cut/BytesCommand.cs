using System;
using System.Text;
using System.Text.RegularExpressions;

namespace Cut
{
	//Class implementing the functionalities or logic for -d option
	public class BytesCommand : Command
	{
		private String[] args;
		private String pathToFile;

		public BytesCommand (String[] args)
		{
			this.args = args;
			this.pathToFile = args [args.Length - 1];
		}

		public override void runCommand ()
		{
			parseCommand();
		}

		//Parses the command options using regex to select 
		//the start and ending indices for reading bytes.

		private void parseCommand ()
		{

			bool hasValidBounds = true;
			String pattern = args[0].Substring(2);
				
			hasValidBounds = isValidPattern (pattern);

			if (hasValidBounds) {
				int[][] partitions = getPatternIterationPartitions ();
				printOutput(partitions);
				}
		}

		//Executes the command and prints the bytes
		public void printOutput (int[][] partitions)
		{
			System.IO.StreamReader file = new System.IO.StreamReader (pathToFile);
			String line;
			int counter = 0;
			while ((line = file.ReadLine()) != null) {

				byte[] bytes = Encoding.ASCII.GetBytes (line);

				for (int x = 0; x < partitions.Length; x++) {
					int maxBound = partitions[x][1];
					int minBound = partitions[x][0];
					if (partitions [x] [1] > bytes.Length || partitions[x][1] == -1) {
						maxBound = bytes.Length;
					}
					for (int i = minBound - 1; i < maxBound; i++) {
						Console.Write ((char)bytes [i]);
					}
				}

				Console.WriteLine ();
				counter++;
			}

			file.Close ();
		}
	}

}

