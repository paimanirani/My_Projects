package ptg;

public class ProblemSolver
{
	/*
	 *arguments in the form:   ./ProblemSolver 0 1 3 4 2 5 7 8 6
	 * 
	 */
private static void usage() {
		
		System.err.println("Usage: Numbers should range from 0 to 8");
			
		System.exit(0);
			
		    
		}
	
	public static void main(String[] args)
	{
		boolean debug = false;
		
		int length=args.length; 
		// Numbers to be adjusted if the debug toggle is present, as components
		// of args will be in different locations if it is.
		if (length != 9)
		 {

			    usage();
			
		}
		
		
		else
		{
		 dispatchEightPuzzle(args);

			
		}

		
	}

	

	// Helper method to build our initial 8puzzle state passed in through args
	private static void dispatchEightPuzzle(String[] a)
	{
		int value;
		int[] initState = new int[9];
		// i -> loop counter
		for (int i = 0; i < a.length; i++)
		{
			value=Integer.parseInt(a[i]);
			if(value>=0&&value<9)
			initState[i] = value;
			else usage();
			
		}
		int c = check(initState);
		if (c==1)
			AStarSearch.search(initState);
		else if(c==-1)
			usage();
		
	}
	
	private static int check(int[] initState) {
		int count=0;
		for(int i=0;i<initState.length;i++)
		{
			for(int j=0;j<initState.length;j++){
				if(initState[i]==initState[j])
				{
					count++;
					continue;
				}
			}
		}
		if(count>initState.length)
		{
			return -1;
		}
		else return 1;
	}
}
