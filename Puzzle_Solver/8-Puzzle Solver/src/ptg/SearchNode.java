package ptg;

import java.util.ArrayList;
import ptg.EightPuzzleState;

/**
 * 
 * Class to represent a SearchNode. This will be a wrapper for a State, and
 * track the cost to get to that state and the state's parent node.
 * 
 * @author Paiman Irani
 * 
 */

public class SearchNode
{

	private EightPuzzleState curState;
	private SearchNode parent;
	private double man; // Manhattan distance cost
	private double moves; //moves cost
	private double fCost; // final cost
	
	/**
	 * 
	 * @param prev The parent node
	 * @param s The current state
	 * @param m The number of moves
	 */
	public SearchNode(SearchNode prev,EightPuzzleState s, double m)
	{
		curState = s;
		parent = null;
		man = 0;
		moves = 0;
		fCost = 0;
	}

	/**
	 * 
	 * Constructor for all other SearchNodes
	 * @param prev The parent node
	 * @param s The current state
	 * @param m The number of moves
	 * @param man The Manhattan distance
	 */
	public SearchNode(SearchNode prev, EightPuzzleState s, double m, double man)
	{
		parent = prev;
		curState = s;
		this.man =man;
		moves = m;
		fCost = man + moves;
		
	}

	
	/**
	 * @return the curState
	 */
	public EightPuzzleState getCurState()
	{
		return curState;
	}

	/**
	 * @return the parent
	 */
	public SearchNode getParent()
	{
		return parent;
	}

	/**
	 * @return the Manhattan
	 */
	public double getMan()
	{
		return man;
	}


	/**
	 * 
	 * @return the final cost
	 */
	public double getFCost()
	{
		return fCost;
	}
	
	
	/**
	 * 
	 * @return The moves made
	 */
	public double getMoves()
	{
		return moves;
	}

}
