package ptg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;



public class PlayGame {
	       static boolean dead =false;   
	       static boolean win = false;
	public static Environment env;
	
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		env = new Environment();
		 Tiles tiles = new Tiles();
		Scanner sc = new Scanner(System.in);
		   System.out.println("Enter filename: ");
		   String filename = sc.nextLine();
		try 
		   { 
	        BufferedReader br = new BufferedReader(new FileReader(filename));
	        String s;
	        int count=0;
	        String w[][]=new String[4][4]; 
	        while ((s = br.readLine()) != null) 
		     { 
		    	 
	        String[] ar = s.split(" "); //split lines 
	        
	         
	        	
	        	 w[count][0]=ar[0];
	        	 w[count][1]=ar[1];
	             w[count][2]=ar[2];
	        	 w[count][3]=ar[3];
	         count++;
		     }
	        env.setObsEnvironment(w);
		   }   
		 catch (Exception e) // if there is an error, we execute this code. 
		   { 
			  System.err.println(e.getMessage());
		   } 		
	      System.out.println(" ");
	      System.out.println("\nThe environment set for the problem is as follows.");
	    //Print Wumpus World
			env.display(env.getObsEnvironment());
		
			System.out.println("\n\n");
			System.out.println("***********************************************************************");
			System.out.println("\n\t\t\tStart Playing\n\t\t\t");
			System.out.println("***********************************************************************");
			System.out.println("\n\n");
			
			while(!dead && !win)
			{
				
				String agent ="A";
				boolean out_of_bounds=false;   //within the board: rows * columns
				int position = env.position(agent);
				String[] pos = env.getPos();
				String[] posInv = env.getPosInv();
				String[][]temp1;
			    env.setAgent(position);  //position of agent
			

			//update 2'D array
			    
			    env.setObsEnvironment(env.updateTwoD(pos));
			    env.setUnobsEnvironment(env.updateTwoD(posInv));
				env.display_details();
				
				if (pos[position].contains("W")) //if contains wumpus
					dead = true;
				 if (pos[position].contains("G"))  //if contains gold
			        win = true;
				 if(pos[position].contains("P"))   //if contains pit
				 {
					 System.out.println("You have fallen into the pit!");
					 dead = true;
				 }
				 Scanner scr=new Scanner(System.in);
				 while(!out_of_bounds && !dead && !win)
				 {
					 out_of_bounds=false;
					 //R: Right; L: Left; u: Up; D: Down; S: Shoot;
				 System.out.println("What would you like to do? Please enter command [R,L,U,D,S]:");
				 char ip = scr.nextLine().toUpperCase().charAt(0); 
				 if(ip=='R' || ip=='L' ||ip =='U' || ip=='D' || ip=='S')
				 {
					
					 
					if(ip=='S' && env.getArrows()==0){
					System.out.println("Oops, no more arrows left!");
					}
					
					 else{	 
				 int value = tiles.valid(ip,1); 
				if( tiles.check_tiles(ip,position) || value<0 || value>15 )
					{
					System.out.println(" ");
					 System.out.println("*******************************");
					System.out.println("BUMP!!! You hit a wall!");
					 System.out.println("*******************************");
					 env.setDirection(null);
					 env.display_details();
					
					}
				if(Environment.getDirection()!=null)
					{
					out_of_bounds=true;
					pos = env.getPos();
					posInv = env.getPosInv();
			   pos[position] =pos[position].replace('A',' ');
			   posInv[position] =posInv[position].replace('A',' ');
					 
				
				String temp = pos[value]+agent;
				pos[value] = temp;
				posInv[value] = temp;
				
				env.setPos(pos);
				env.setPosInv(posInv);
				
					}
				 }	 
				 }
				 else
					 System.out.println("Invalid Input!");
				 
				 }
			     }
			if(dead)
			{
			System.out.println(" ");
			 System.out.println("*******************************");
			System.out.println("You are Dead!");
			 System.out.println("*******************************");
			}
		if(win)
		{
			System.out.println(" ");
			 System.out.println("*******************************");
			System.out.println("You found Gold!");
			 System.out.println("*******************************");
		} 
	}
	
	
	}
