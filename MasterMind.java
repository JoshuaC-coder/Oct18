/**
 *	Plays the game of MasterMind.
 *	This program plays a game where a four letter word or code is generated
 * 	by the program, and the user must guess it in ten or less tries. The
 *  word will always be four characters long and through the letters A-F.
 *  To help out the user, after every guess if two chars match in the 
 * 	same sequence, it will tell the user there was an exact match.
 *  If two chars match somewhere, it will tell the user there was a partial
 *  match.
 * 
 *	@author Joshua Cao
 *	@since  9/27/2024
 */

public class MasterMind {

	private boolean reveal;			// true = reveal the master combination
	private PegArray[] guesses;		// the array of guessed peg arrays
	private PegArray master;		// the master (key) peg array
	private int guessCount;
	
	// Constants
	private final int PEGS_IN_CODE = 4;		// Number of pegs in code
	private final int MAX_GUESSES = 10;		// Max number of guesses
	private final int PEG_LETTERS = 6;		// Number of different letters on pegs
	private final char UPPERCASE_A = 'A';   // constant for 'A' value
	private final char LOWERCASE_A = 'a';   // constant for 'a' value
	private final char UPPERCASE_F = 'F';   // constant for 'F' value
	private final char LOWERCASE_F = 'f';   // constant for 'f' value
									
	public MasterMind()
	{
		guessCount = 1;
		guesses = new PegArray[MAX_GUESSES];
		 for (int i = 0; i < MAX_GUESSES; i++) {
            guesses[i] = new PegArray(PEGS_IN_CODE);
        }
         master = new PegArray(PEGS_IN_CODE);
	}
	
	public static void main(String [] args)
	{
		MasterMind mm = new MasterMind();
		mm.printIntroduction();
		mm.generateMasterCode();
		mm.playEntireGame();
	}
	/**
	 *	Print the introduction screen
	 */
	public void printIntroduction() {
		System.out.println("\n");
		System.out.println("+------------------------------------------------------------------------------------+");
		System.out.println("| ___  ___             _              ___  ___ _             _                       |");
		System.out.println("| |  \\/  |            | |             |  \\/  |(_)           | |                      |");
		System.out.println("| | .  . |  __ _  ___ | |_  ___  _ __ | .  . | _  _ __    __| |                      |");
		System.out.println("| | |\\/| | / _` |/ __|| __|/ _ \\| '__|| |\\/| || || '_ \\  / _` |                      |");
		System.out.println("| | |  | || (_| |\\__ \\| |_|  __/| |   | |  | || || | | || (_| |                      |");
		System.out.println("| \\_|  |_/ \\__,_||___/ \\__|\\___||_|   \\_|  |_/|_||_| |_| \\__,_|                      |");
		System.out.println("|                                                                                    |");
		System.out.println("| WELCOME TO MONTA VISTA MASTERMIND!                                                 |");
		System.out.println("|                                                                                    |");
		System.out.println("| The game of MasterMind is played on a four-peg gameboard, and six peg letters can  |");
		System.out.println("| be used.  First, the computer will choose a random combination of four pegs, using |");
		System.out.println("| some of the six letters (A, B, C, D, E, and F).  Repeats are allowed, so there are |");
		System.out.println("| 6 * 6 * 6 * 6 = 1296 possible combinations.  This \"master code\" is then hidden     |");
		System.out.println("| from the player, and the player starts making guesses at the master code.  The     |");
		System.out.println("| player has 10 turns to guess the code.  Each time the player makes a guess for     |");
		System.out.println("| the 4-peg code, the number of exact matches and partial matches are then reported  |");
		System.out.println("| back to the user. If the player finds the exact code, the game ends with a win.    |");
		System.out.println("| If the player does not find the master code after 10 guesses, the game ends with   |");
		System.out.println("| a loss.                                                                            |");
		System.out.println("|                                                                                    |");
		System.out.println("| LET'S PLAY SOME MASTERMIND!                                                        |");
		System.out.println("+------------------------------------------------------------------------------------+");
		System.out.println("\n");
	}
	
	/**
	 * This method prompts enter to start the game, and then generates 
	 * a four letter master code using the Dice class. Each generate value
	 * is casted into a char and let as a value in the instance of the
	 * object, PegArray, called master. It also prints out the board and
	 * the number of guesses done so far.
     */
	public void generateMasterCode()
	{
		String compConfirm = Prompt.getString("\nHit the Enter key to start the game");
		Dice d = new Dice();
		int randomNumber = 0;                        // random # generate 
		                                             // by Dice class
		char [] pegMaster = new char[PEGS_IN_CODE];  // temp array to store 
	                                                 // the random chars
		master = new PegArray(PEGS_IN_CODE);
		
		// Generate a random # and casts it to a char A-F four times
		for(int i = 0; i < pegMaster.length; i++)
		{
			d.roll();
			randomNumber = d.getValue();
			pegMaster[i] = (char)(randomNumber + (UPPERCASE_A - 1));
			master.getPeg(i).setLetter(pegMaster[i]);
		}
		printBoard();
		System.out.println("\nGuess " + guessCount);
	}
	
	/**
	 * This method prompts the user to enter a four digit, A-F, code as a 
	 * guess for match the master's code. If the length of the code isn't
	 * four characters long, or if the code contains characters not A-F,
	 * it will keep on prompting the user to do so.
	 * 
	 * @return The user's guess of the master code
     */
	public String getUserInput()
{
    boolean wordFlagged; // This will be set to true if any invalid input is found
    char[] holdsValues = new char[PEGS_IN_CODE]; // each char value of the guess
    int asciiValue = 0;                          // ascii value of the char
    String userGuess = "";                       // the user's guess

    do
    {
        userGuess = Prompt.getString("\nEnter the code using (A,B,C,D,E,F). "
                                     + "For example, ABCD or abcd from left"
                                     + "-to-right");

        // Assume the input is valid initially
        wordFlagged = false;

        // Check for correct length
        if (userGuess.length() == PEGS_IN_CODE)
        {
            // Checks each character in the word given
            for (int i = 0; i < holdsValues.length; i++)
            {
                holdsValues[i] = userGuess.charAt(i);
                asciiValue = (int) holdsValues[i];

                // Check if the character is outside the valid range
                if (!(((asciiValue >= UPPERCASE_A) && (asciiValue <= UPPERCASE_F)) || 
                       ((asciiValue >= LOWERCASE_A) && (asciiValue <= LOWERCASE_F))))
                {
                    // If an invalid character is found, set wordFlagged to true
                    wordFlagged = true;
                }
            }
        }
        else
        {
            // If the length is not valid, mark the word as flagged
            wordFlagged = true;
        }

        if (wordFlagged)
        {
            System.out.print("ERROR: Bad input, try again.");
        }

    } while (wordFlagged);

    userGuess = userGuess.toUpperCase();
    return userGuess;
}

	/**
	 * This method plays one round ten times, or until the user guesses
	 * the master code. Prints corresponding messages if the user wins or
	 * loses.
     */
	public void playEntireGame()
	{
		boolean wonOut = false; // if the user won or not
		while(guessCount <= 10 && !reveal)
		{
			wonOut = oneTurnWonOrNot();
		}
		
		// Win message		
		if(wonOut)
		{
			System.out.println("Nice work! You found the master code in " 
			                   + (guessCount - 1) + " guesses.");
		}
		
		// Lose message
		else
		{
			System.out.println("Oops. You were unable to find the solution "
		                       + "in 10 guesses.");
		}	
	}
	
	/**
	 * This method runs a turn and takes the user's guess once. It first
	 * gets the user's input, and puts each char into the chart. Then
	 * it calls to methods to calculate the partial and exact matches.
	 * It also reveals the master code if they guess it right, and prints 
	 * out the current guess count.
	 * 
	 * @return If the user won during that round or not
     */
	public boolean oneTurnWonOrNot()
	{
		boolean won = false;         // if the user had four exact matches
		String userGuessOut = "";    // the user's guess
		userGuessOut = getUserInput();
		
		// Puts each char of the user's word onto the board
		for (int i = 0; i < PEGS_IN_CODE; i++) 
		{
            guesses[guessCount - 1].getPeg(i).setLetter(userGuessOut.charAt(i));
        }
        guesses[guessCount - 1].getExactMatches(master);
        guesses[guessCount - 1].getPartialMatches(master);
            
        if(guesses[guessCount - 1].getExact() == PEGS_IN_CODE)
        {
			reveal = true;
			won = true;
		}
		if(guessCount == MAX_GUESSES)
		{
			reveal = true;
		}
		printBoard();
		guessCount++;
			
		// Prints out the num of guesses used so far
		if(guessCount <= MAX_GUESSES && !reveal)
		{
			System.out.println("\nGuess " + guessCount);
		}
		return won;
	}
	
	/**
	 *	Print the peg board to the screen
	 */
	public void printBoard() 
	{
		// Print header
		System.out.print("+--------+");
		for (int a = 0; a < PEGS_IN_CODE; a++) System.out.print("-------+");
		System.out.println("---------------+");
		System.out.print("| MASTER |");
		for (int a = 0; a < PEGS_IN_CODE; a++)
			if (reveal)
				System.out.printf("   %c   |", master.getPeg(a).getLetter());
			else
				System.out.print("  ***  |");
		System.out.println(" Exact Partial |");
		System.out.print("|        +");
		for (int a = 0; a < PEGS_IN_CODE; a++) System.out.print("-------+");
		System.out.println("               |");
		// Print Guesses
		System.out.print("| GUESS  +");
		for (int a = 0; a < PEGS_IN_CODE; a++) System.out.print("-------+");
		System.out.println("---------------|");
		for (int g = 0; g < MAX_GUESSES - 1; g++) {
			printGuess(g);
			System.out.println("|        +-------+-------+-------+-------+---------------|");
		}
		printGuess(MAX_GUESSES - 1);
		// print bottom
		System.out.print("+--------+");
		for (int a = 0; a < PEGS_IN_CODE; a++) System.out.print("-------+");
		System.out.println("---------------+");
	}
	
	/**
	 *	Print one guess line to screen
	 *	@param t	the guess turn
	 */
	public void printGuess(int t) 
	{
		System.out.printf("|   %2d   |", (t + 1));
		// If peg letter in the A to F range
		char c = guesses[t].getPeg(0).getLetter();
		if (c >= 'A' && c <= 'F')
			for (int p = 0; p < PEGS_IN_CODE; p++)
				System.out.print("   " + guesses[t].getPeg(p).getLetter() + "   |");
		// If peg letters are not A to F range
		else
			for (int p = 0; p < PEGS_IN_CODE; p++)
				System.out.print("       |");
		System.out.printf("   %d      %d    |\n",
							guesses[t].getExact(), guesses[t].getPartial());
	}

}
