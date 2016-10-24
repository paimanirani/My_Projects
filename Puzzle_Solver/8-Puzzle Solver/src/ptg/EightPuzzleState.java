package ptg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * EightPuzzleState defines a state for the 8puzzle problem. The board is always
 * represented by a single dimensioned array, we attempt to provide the illusion
 * that the state representation is 2 dimensional and this works very well. In
 * terms of the actual tiles, '0' represents the hole in the board, and 0 is
 * treated special when generating successors.
 * 
 * @author Paiman Irani
 * 
 */
public class EightPuzzleState 
{

	
	private final int[] GOAL = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 0 };
	private final int PUZZLE_SIZE = GOAL.length;
	private int[] curBoard;  //Current Board state
	private int manDist = 0;  //Manhattan Distance of current state

	
	

	/**
	 * 
	 * @param board
	 */
	public EightPuzzleState(int[] board)
	{
		curBoard = board;
		
		setManDist();
	}


	/*
	 * Set the Manhattan Distance for the current board
	 */
	private void setManDist()
	{
		int index = 0;
		for (int i = 0; i < 3; i++)
		{
			for (int k = 0; k < 3; k++)
			{
			
				int val = (curBoard[index]);

				// If we are looking at the hole('0'), skip it
				if (val != 0)
				{
					
					// Vertical offset, divide the tile value by the vertical dimension
					int vert = (val-1) / 3;
					// Horizontal offset, mod the tile value by the horizontal dimension
					int horiz = (val-1) % 3;
					manDist += Math.abs(vert - (i)) + Math.abs(horiz - (k));
				}
				index++;
				
			}
		}
	}

	/*
	 * Attempt to locate the "0" spot on the current board
	 * 
	 * @return the index of the "hole" (or 0 spot)
	 */
	private int getHole()
	{
		// If returning -1, an error has occured. The "hole" should always exist
		// on the board and should always be found by the below loop.
		int holeIndex = -1;

		for (int i = 0; i < PUZZLE_SIZE; i++)
		{
			if (curBoard[i] == 0)
				holeIndex = i;
		}
		return holeIndex;
	}

	

	/**
	 * Getter for the Manhattan Distance value
	 * 
	 * @return the Manhattan Distance h(n) value
	 */
	public int getManDist()
	{
		return manDist;
	}


	/**
	 * Is thought about in terms of NO MORE THAN 4 operations. Can slide tiles
	 * from 4 directions if hole is in middle Two directions if hole is at a
	 * corner three directions if hole is in middle of a row
	 * 
	 * @return an ArrayList containing all of the successors for that state
	 */
	
	public ArrayList<EightPuzzleState> genSuccessors()
	{
		
		ArrayList<EightPuzzleState> successors = new ArrayList<EightPuzzleState>();
		int hole = getHole();

		// try to generate a state by sliding a tile leftwise into the hole
		// if we CAN slide into the hole
		if (hole != 0 && hole != 3 && hole != 6)
		{
			/*
			 * we can slide leftwise into the hole, so generate a new state for
			 * this condition and throw it into successors
			 */
			swapAndStore(hole - 1, hole, successors);
			
		}

		// try to generate a state by sliding a tile topwise into the hole
		if (hole != 6 && hole != 7 && hole != 8)
		{
			swapAndStore(hole + 3, hole, successors);
			
		}

		// try to generate a state by sliding a tile bottomwise into the hole
		if (hole != 0 && hole != 1 && hole != 2)
		{
			swapAndStore(hole - 3, hole, successors);
			
		}
		// try to generate a state by sliding a tile rightwise into the hole
		if (hole != 2 && hole != 5 && hole != 8)
		{
			swapAndStore(hole + 1, hole, successors);
			
		}

		return successors;
	}

	/*
	 * Switches the data at indices d1 and d2, in a copy of the current board
	 * creates a new state based on this new board and pushes into s.
	 */
	private void swapAndStore(int d1, int d2, ArrayList<EightPuzzleState> s)
	{
		int[] cpy = new int[PUZZLE_SIZE];
		for (int i = 0; i < PUZZLE_SIZE; i++)
		{
			cpy[i] = curBoard[i];
		}
		int temp = cpy[d1];
		cpy[d1] = curBoard[d2];
		cpy[d2] = temp;
		s.add((new EightPuzzleState(cpy)));
	}

	/**
	 * Check to see if the current state is the goal state.
	 * 
	 * @return - true or false, depending on whether the current state matches the goal
	 */
	public boolean isGoal()
	{
		if (Arrays.equals(curBoard, GOAL))
		{
			return true;
		}
		return false;
	}

	/**
	 * Method to print out the current state. Prints the puzzle board.
	 */
	public void printState()
	{
		System.out.println(curBoard[0] + " | " + curBoard[1] + " | "+ curBoard[2]);
		System.out.println("---------");
		System.out.println(curBoard[3] + " | " + curBoard[4] + " | "+ curBoard[5]);
		System.out.println("---------");
		System.out.println(curBoard[6] + " | " + curBoard[7] + " | "+ curBoard[8]);

	}

	//return true or false, depending on whether the states are equal
	/* 
	public boolean equals(EightPuzzleState s)
	{
		if (Arrays.equals(curBoard, ((EightPuzzleState) s).getCurBoard()))
		{
			return true;
		}
		else
			return false;
	}
	*/


	/**
	 * Getter to return the current board array
	 * 
	 * @return the curState
	 */
	public int[] getCurBoard()
	{
		return curBoard;
	}
	
}