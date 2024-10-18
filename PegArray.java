/**
 *  This class creates and manages one array of pegs from the game MasterMind.
 *
 *  @author Joshua Cao
 *  @since  10/27/2024
*/

public class PegArray {

	// array of pegs
	private Peg [] pegs;

	// the number of exact and partial matches for this array
	// as matched against the master.
	// Precondition: these values are valid after getExactMatches() 
	// and getPartialMatches() are called
	private int exactMatches, partialMatches;
		
	/**
	 *	Constructor
	 *	@param numPegs	number of pegs in the array
	 */
	public PegArray(int numPegs)
	{	
		pegs = new Peg[numPegs]; 
		
		// intializes each value in peg to a different Peg object
			for (int i = 0; i < pegs.length; i++) 
			{
				pegs[i] = new Peg();
				exactMatches = 0;
				partialMatches = 0;
			}
		}
	
	/**
	 *	Return the peg object
	 *	@param n	The peg index into the array
	 *	@return		the peg object
	 */
	public Peg getPeg(int n) 
	{ 
		return pegs[n]; 		
	}
	
	/**
	 *  Finds exact matches between master (key) peg array and this peg array
	 *	Postcondition: field exactMatches contains the matches with the master
	 * 
	 *  @param master	The master (code) peg array
	 *	@return			The number of exact matches
	 */
	public int getExactMatches(PegArray master) 
	{
		// Loops through the master and user's guess and checks if the
		// same index of the peg array are the same.
		for(int i = 0; i < 4; i++)
		{	 
			 if(master.getPeg(i).getLetter() == pegs[i].getLetter())
			 {
				 exactMatches++;
			 }
		 }
		 return exactMatches; 
	}
	
	/**
	 *  Find partial matches between master (key) peg array and this peg array
	 *	Postcondition: field partialMatches contains the matches with the master
	 * 
	 *  @param master	The master (code) peg array
	 *	@return			The number of partial matches
	 */
	public int getPartialMatches(PegArray master) 
	{ 
		boolean partialFound = true;    // if a partial check has been found 
										// for a single check
		
		int [] flagged = {-1, -1, -1, -1};  // stores index not to check exact
		int [] flaggedJ = {-1, -1, -1, -1}; // stores index not to check partial
		int index = 0;
		
		
		// Checks for the indexs that were an exact match
		for(int i = 0; i < 4; i++)
		{
			 if(master.getPeg(i).getLetter() == pegs[i].getLetter())
			 {
				 flagged[i] = i;	
			 }
			 else
			 {
				 flagged[i] = -1;
			 }
		 }
		
		// Checks for the indexs that are a partial match 
		for(int i = 0; i < 4; i++)
		{
			
			partialFound = true;	
					
			// Goes through each of the master peg's for every of the user's pegs
			for(int j = 0; j < 4; j++)
			{
				// Makes sure an exact match doesn't count
				if(!(master.getPeg(i).getLetter() == pegs[i].getLetter()))
				{
								
					// If the word has already been found once in a single
					// check, go to the next peg/word
				
					if(master.getPeg(j).getLetter() == pegs[i].getLetter() 
					   && partialFound && flagged[j] == -1 && flaggedJ[j] == -1)				
					{			
						flaggedJ[j] = j;
						partialFound = false;
						partialMatches++;	
					}
					else if(flaggedJ[j] <= 0)
					{
						flaggedJ[j] = -1;
					}
				}		
			}
		}	
		return partialMatches;
	}
	
	// Accessor methods
	// Precondition: getExactMatches() and getPartialMatches() must be called first
	public int getExact() 
	{ 
		return exactMatches; 
	}
	public int getPartial() 
	{ 
		return partialMatches; 
	}

}
