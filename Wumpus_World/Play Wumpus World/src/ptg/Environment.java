package ptg;


public class Environment {
	   private static int b_pos[]=new int[20];  //Breeze
	   private static int s_pos[]=new int[20];  //Stench
	   private int value;
	   private static boolean environmentCreated = false;
	   private static String[] pos = new String[16];
	   private static String[] posInv = new String[16];
	   private static String w[][]=new String[4][4];    //Observable Environment
	   private static String wInv[][]=new String[4][4];  //Unobservable Environment
	   private static int agent =0;
	   private static int arrow_At = 0;
	   private static String direction = null;
	   private static boolean wumpus_killed = false;
	   private static int arrows = 2;
	 
	  
	  
	   public static int getArrows() {
		return arrows;
	}

	public static void setArrows(int arrows) {
		Environment.arrows = arrows;
	}

	public static String[] getPosInv() {
			return posInv;
		}

		public static void setPosInv(String[] posInv) {
			Environment.posInv = posInv;
			wInv = Environment.updateTwoD(posInv);
		}
	   
		 public static String[] getPos() {
				return pos;
			}

			public static void setPos(String[] pos) {
				Environment.pos = pos;
				w = Environment.updateTwoD(pos);
			}
			
		 public static int getAgent() {
				return agent;
			}

			public static void setAgent(int agent) {
				Environment.agent = agent;
				setArrow_At(agent); //arrow value from agent position
			}

			public static int getArrow_At() {
				
				return arrow_At;
			}

			public static void setArrow_At(int arrow_At) {
				Environment.arrow_At = arrow_At;
			}

			public static String getDirection() {
				return direction;
			}

			public static void setDirection(String direction) {
				Environment.direction = direction;
			}

			public static boolean isWumpus_killed() {
				return wumpus_killed;
			}

			public static void setWumpus_killed(boolean wumpus_killed) {
				Environment.wumpus_killed = wumpus_killed;
			}
	   
	   
	   
	   
	  public static void setObsEnvironment(String[][] w)
	  {
		  if(environmentCreated)
		  {
			  Environment.w=w;
		  }
		  else
		  {
		  for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				if(w[i][j].matches("X"))
					w[i][j]="";
			  }
		    }
            System.out.println("The Wumpus-world according to the file read is as follows:");
		    display(w);
            //Agent's position
			w[3][0]="A";
			System.out.println(" ");
			System.out.println("Agent starts from [3,0]");
		    Environment.w = w;
		    pos=updateOneD(w);  //From 2'D array to 1'D array			
			for(int i=0;i<pos.length;i++)
			{
				if(pos[i].matches("P"))
				{
					show_sense(i,1,w);
					
				}
				if(pos[i].matches("W"))
				{
					show_sense(i,2,w);
					
				}
				
			}
			
			setUnobsEnvironment();
			environmentCreated=true;
		  }
		  pos=updateOneD(w);
	  }
	  
	  public static void setUnobsEnvironment()
	  {
		  
		  for(int i=0;i<4;i++){
				for(int j=0;j<4;j++){
					wInv[i][j]="";
					wInv[3][0]="A";
					
				}
			}
		  posInv=updateOneD(wInv);
	  }
	  
	  public static void setUnobsEnvironment(String[][] w)
	  {
		  Environment.wInv=w;
		  posInv=updateOneD(w);
	  }
	  
	  public static String[][] getObsEnvironment()
	  {
		  return w;
	  }
	  
	  public static String[][] getUnobsEnvironment()
	  {
		  return wInv;
	  }
	  
	  public static void display(String[][] w)
		 {
		                  
		                  for(int i=0;i<4;i++)
		              	{
		                    System.out.println("\n-----------------------------------------------------------------");
		                         System.out.print("|\t");
		                         for(int j=0;j<4;j++)
		                         System.out.print(w[i][j]+"\t|\t");
		                      }
		                     System.out.println("\n-----------------------------------------------------------------");
		                     }
		
	  
		 /**
		  * This method updates 1'D array values
		  */
		 public static String[] updateOneD(String[][] w)
		 {
				int k=0;
				String[] pos1=new String[16];
				for(int i=0;i<4;i++)
				{
					for(int j=0;j<4;j++)
					{
						pos1[k]=w[i][j];
						k++;
					}
					
				}
				return pos1;
		 }
		 

		 /**
		  * This method updates 2'D array values
		  */
		 public static String[][] updateTwoD(String[] pos)
		 {
			 int count=-1;
			 String w[][]=new String[4][4]; 
			 while(count!=pos.length-1)
			 for(int i=0;i<4;i++){
			 	 for(int j=0;j<4;j++){
			 		 count++;
			 		w[i][j] =  pos[count];
			 	 } 
			 } 
            return w;
		 }
		 
		 /**
			 * This method will create an environment around the wumpus world. Breeze around pit,
			 * and Stinch around wumpus
			 * @param a
			 * @param b
			 * @param w
			 */
			 public static void show_sense(int a,int b,String w[][])
			  {
			            int t1,t2,t3,t4;
			            t1=a-1;
			            t2=a+1;
			            t3=a+4;
			            t4=a-4;

			               if(a==0||a==4 || a==8 || a==12)
			                 t1=-1;
			              if(a==3 || a==7 || a==11 || a==15)
			                  t2=-1;
			              if(t3>15)
			                 t3=-1;
			              if(t4<0)
			                 t4=-1;

			             //int temp[]=new int[4];

			                 if(b==1)
			                  {
			                    b_pos[0]=t1;b_pos[1]=t2;b_pos[2]=t3;b_pos[3]=t4;}
			                 else
			                if(b==2)
			                 {
				             s_pos[0]=t1;s_pos[1]=t2;s_pos[2]=t3;s_pos[3]=t4;
				             }

			                  int temp1,count;

			                  for(int i=0;i<4;++i)
			                   {
			                  if(b==1)
			                 temp1=b_pos[i];
			                   else
			                   temp1=s_pos[i];
			                    count=-1;
			                   for(int j=0;j<4;j++)
			                   {
			                   for(int k=0;k<4;k++)
			                  {
			                   ++count;
			                    if(count==temp1 && b==1 && !w[j][k].contains("B"))
			                     {
			                    w[j][k]+="B";
			                      }
			                  else
			                  if(count==temp1 && b==2 && !w[j][k].contains("S"))
			                 w[j][k]+="S";
			                }
			              }
			            }

			           } 
				/**
				 * This method locates the position of certain characteristics
				 * @param pos
				 * @param string
				 * @return The location
				 */
			     public int position(String string ) {
					int m=0;
					for(int i=0;i<pos.length;i++)
					{
						if(pos[i].contains(string))
							m=i;
					}
					return m;
				}
				
			     
			     /**
			 	 * This method prints the location of the agent, and the characteristics of the tiles the agent is on.
			 	 * @param position
			 	 * @param w
			 	 */
			 	public void display_details() {
			 		int count=-1;
			 		int m=0,n=0;
			 		display(wInv); 
			 			out:for(int i=0;i<4;i++)
			 			{
			 				for(int j=0;j<4;j++)
			 				{
			 					m=i;
			 					n=j;
			 					count++;
			 					if(agent==count)
			 					{
			 						break out;
			 					}
			 				}	
			 			}
			 		
			 		System.out.println("You are in room ["+m+","+n+"] of the cave.\t\t");
			 		if(direction!=null)
			 			System.out.println("Agent Moved: "+direction);
			 		if(!check())
			 	{
			 			if(Environment.pos[agent].contains("B"))
			 		{
			 			//	convert(agent,"B");
			 			System.out.println("There is a BREEZE in here!");
			 		}
			 		if(Environment.pos[agent].contains("S"))
			 		{       
			 			//convert(agent,"S");
			 			System.out.println("There is a STENCH in here!");
			 		}
			 	}	
			 	}
			 	
			 	static boolean check()
			 	{
			 		
			 	int temp = sense(agent);
			 	if(temp==1 || temp==2)
			 	return false;

			 	return true;
			 	}
			 	
			 	
			 	/**
				 * This method checks what is on the present tile
				 * @param p
				 * @return The integer value based on different characteristics
				 */
				public static int sense(int p)
				{
					Environment e = new Environment();
				if(pos[p].contains("B"))
				return 1;
				else
					if(pos[p].contains("S"))
				return 2;
				else
					if(pos[p].contains("G"))
				return 3;
				if(pos[p].contains("W"))
				return 4;
				else
				return 0;
				}
				
//			 	  public String[][] convert(int pos, String value)
//			 	  {
//			 		  int count=-1;
//			 		  out:for (int i = 0; i < 4; i++) {
//			 			  for (int j = 0; j<4; j++) {
//							count++;
//							if(count==pos)
//								wInv[i][j] += value;
//							break out;
//						}
//						
//					}
//			 		 return wInv;
//			 	  }
	  
}
