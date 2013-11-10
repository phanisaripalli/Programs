using System;
using System.IO;

namespace Cut
{
	public class Cut
	{
		//public const int paramOne = 0;

		public static void Main (String[] args)
		{
			if (!isOptionValid(args)) {
				Console.WriteLine ("Incorrect usage");
			} else {
				if (isFileValid (args[args.Length - 1])) {
					analyzeCommand(args);
				} else {
					Console.WriteLine("File does not exist or is not a txt");
				}
			}

		}		

		//
		// Checks if file exists and is a .txt file
		//
		static bool isFileValid (string file)
		{
			bool isValid = File.Exists (file) == true && Path.GetExtension(file).Equals(".txt");
				return isValid;
		}

		//Performs an initial validation of the options
		static bool isOptionValid (String[] args)
		{
			bool result = true;
			int paramCount = args.Length;

			String[] validParams = { "-b", "-f", "-d" };

			if (paramCount < 1) {
				result = false;
			} else if (!(args[0].Length > 1 
			             && Array.Exists(validParams, element => element == args[0].Substring(0,2)))) {
				result = false;
			}
			return result;
		}

		// Performs an initial analyses of the command and calls the suitable class for its execution
		static void analyzeCommand (String[] args)
		{

			if (args [0].StartsWith ("-b")) {
				if (args.Length == 2) {
					BytesCommand bytesCommand = new BytesCommand (args);
					bytesCommand.runCommand ();
				} else {
					Console.WriteLine ("Invalid -d command");
				}

			} else if (args [0].StartsWith ("-d")) {
				if (args.Length == 3 && args [0].Length > 2 && args [1].StartsWith ("-f") && args [1].Length > 2) {
					// Execute command
					FieldsCommand fieldsCommand = new FieldsCommand (args [0], args [1], args [2]);
					fieldsCommand.runCommand ();
				} else {
					Console.WriteLine ("Incorrect -d command");
				}

			} else if (args [0].StartsWith ("-f") && args [0].Length > 2) {
				if (args.Length == 2) {
					FieldsCommand fieldsCommand = new FieldsCommand ("-d\t", args [0], args [1]);
				    fieldsCommand.runCommand ();
				} else if(args.Length == 3 && args [1].Length > 2 && args[1].StartsWith("-d")) {

					FieldsCommand fieldsCommand = new FieldsCommand (args [1], args [0], args [2]);
					fieldsCommand.runCommand ();
				}

			} 
		}


	}
}

