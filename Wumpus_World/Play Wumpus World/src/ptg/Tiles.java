package ptg;

import java.util.Scanner;


public class Tiles {
	public static Environment env = new Environment(); 
	public static String[][]w;
	public static String[][]wInv;
	/**
	 * This method checks the boundaries of the board 
	 * @param ip
	 * @param a
	 * @return true if it is the boundary
	 */
	public static boolean check_tiles(char ip, int a)
	{
	if(ip=='R')
	{
		if(a==3 ||a==7 ||a==11 ||a==15 )
			return true;
		else 
			return false;
	}
	else if(ip=='L')
	{
		if(a==0 ||a==4 ||a==8 ||a==12 )
			return true;
		else 
			return false;
	}
	else
		return false;
	}

	
	
/**
 * This method makes sure valid step is made  
 * @param ip
 * @param i
 * @return The next position of the agent 
 */
	@SuppressWarnings("static-access")
	public static int valid(char ip, int i) {
		int temp = 0;
		int value=0;
		 switch (ip) {
		 case 'U':
			 temp = 1;
			 if(i==1)
			value = move(temp);
			 else
				 value = arrow_shot(temp);
			env.setDirection("UP");
			 break;
			 
		 case 'D':
			 temp = 2;
			 if(i==1)
					value = move(temp);
					 else
						 value = arrow_shot(temp);
			 env.setDirection("DOWN");
			 break;
		
		 case 'R':
			 temp = 3;
			 if(i==1)
					value = move(temp);
					 else
						 value = arrow_shot(temp);
			 env.setDirection("RIGHT");
			 break;
		
		 case 'L':
			 temp = 4;
			 if(i==1)
					value = move(temp);
					 else
						 value = arrow_shot(temp);
			 env.setDirection("LEFT");
			 break;
	
		 case 'S':
			 int arrow = env.getArrows();
			 env.setArrows(arrow--);
			 value = env.getAgent();
			 shoot();
			 if(env.isWumpus_killed()){
				 System.out.println(" ");
					System.out.println("*******************************");
					System.out.println("You killed the wumpus!");
					System.out.println("*******************************");
					System.out.println("");
					Environment.display(wInv);
					System.out.println("Remaining Arrows: "+Environment.getArrows());

			 }
			 else
			 {
				 System.out.println(" ");
				 System.out.println("*******************************");
				 System.out.println("You missed the wumpus!");
					System.out.println("*******************************");
					System.out.println("");
					Environment.display(wInv);
					System.out.println("Remaining Arrows: "+Environment.getArrows());
			 }
			 env.setDirection(null);
			 break;
		  
		 }
		 return value;
	}

	
	/**
	 * This method allocates the position of the arrow 
	 * @param temp
	 * @return The position of the arrow 
	 */
	@SuppressWarnings("static-access")
	public static int arrow_shot(int temp) {
		int arrow = env.getArrow_At();
		int value = 0;
		if(temp ==1)
		{
			value=arrow-4;
		}
		if(temp ==2)
		{
			value=arrow+4;
		}
		if(temp ==3)
		{
			value=arrow+1;
		}
		if(temp ==4)
		{
		
			value=arrow-1;
		}
		env.setArrow_At(value);
		return value;
		
	}
	
/**
 * This method makes sure the position of the arrow is valid
 */
	@SuppressWarnings("static-access")
	private static void shoot() {
		Scanner scr=new Scanner(System.in);
		boolean in_bound=true;
		boolean valid_input = false;
		while(!valid_input){
			valid_input = false;
		System.out.println("Where do you want to shoot? Please enter command [R,L,U,D]");
		char ip = scr.nextLine().toUpperCase().charAt(0); 
		 if(ip=='R' || ip=='L' ||ip =='U' || ip=='D')
		 {
			 valid_input = true;
			 while(in_bound)
			 {
				
				 in_bound=true;
				 
				int value = valid(ip,2);
				  if(!check_arrow(ip,env.getArrow_At())&& value>=0 && value<=15)
				  {
				String[] pos=env.getPos();
					if(pos[value].contains("W")){
						pos[value]=pos[value].replace("W","");
						env.setObsEnvironment(env.updateTwoD(pos));
						env.setWumpus_killed(true);
						remove_Stench(value);
			
					}
				  }
				  else
					 in_bound=false;
			
			 }
		 }
		 else
		 {
			 System.out.println("Invalid Input!");
		 }
		
	}
	
	}

	/**
	 * This method checks whether the arrow reached the boundary
	 * @param ip
	 * @param a
	 * @return true if it reached the boundary
	 */
	public static boolean check_arrow(char ip, int a) {
		
			if(ip=='R')
			{
				if(a==4 ||a==8 ||a==12 )
					return true;
				else 
					return false;
			}
			else if(ip=='L')
			{
				if(a==3 ||a==7 ||a==11 )
					return true;
				else 
					return false;
			}
			else
				return false;
		
	}

	/**
	 * This method removes stench if the wumpus is killed 
	 * @param a
	 */
	private static void remove_Stench(int a) {
		int s_pos[]=new int[20];
		  int t1,t2,t3,t4;
          t1=a-1;
          t2=a+1;
          t3=a+4;
          t4=a-4;

             if(a==0||a==4 || a==8 || a==12)
               t1=0;
            if(a==3 || a==7 || a==11 || a==15)
                t2=0;
            if(t3>15)
               t3=0;
            if(t4<0)
               t4=0;

	             s_pos[0]=t1;s_pos[1]=t2;s_pos[2]=t3;s_pos[3]=t4;
	             

                int temp,count;

                for(int i=0;i<4;++i)
                 {
                 temp=s_pos[i];
                  count=-1;
                 for(int j=0;j<4;j++)
                 {
                 for(int k=0;k<4;k++)
                {
                 ++count;
       w= Environment.getObsEnvironment();
       wInv=Environment.getUnobsEnvironment(); 
                if(count==temp && w[j][k].contains("S"))
                {
                	w[j][k]=w[j][k].replace("S","");
                wInv[j][k]=wInv[j][k].replace("S","");
                }
                
              }
            }
          }  
        		Environment.setObsEnvironment(w);
        		Environment.setUnobsEnvironment(wInv);
		
	}

	/**
	 * This method allocates the position of the agent
	 * @param temp
	 * @return the position of the agent
	 */
	@SuppressWarnings("static-access")
	private static int move(int temp) {
	
		int value = 0;
		if(temp ==1)
		{
			value = env.getAgent()-4;
		}
		if(temp ==2)
		{
			value = env.getAgent()+4;
		}
		if(temp ==3)
		{
			value = env.getAgent()+1;
		}
		if(temp ==4)
		{
		
			value = env.getAgent()-1;
		}
		return value;
		
	}

	

	}