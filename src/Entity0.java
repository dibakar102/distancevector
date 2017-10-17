public class Entity0 extends Entity
{    
    // Perform any necessary initialization in the constructor
	//Array to hold which nodes to send updates to
		private static int[] directNeighbors = {1,2,3};
		
		//Array to hold the current minimum costs to all nodes
		private static int minimumCosts[] = new int[NetworkSimulator.NUMENTITIES];
    public Entity0()
    {
    	//Initialize distance table making everything "infinity"
    	for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
    	{
    		for (int j = 0; j < NetworkSimulator.NUMENTITIES; j++)
    		{
    			distanceTable[i][j] = 999;
    		}
    	}
    	
    	//Update distance table values with immediate neighbor values
    	distanceTable[0][0] = 0;
    	distanceTable[1][0] = 1;
    	distanceTable[2][0] = 3;
    	distanceTable[3][0] = 7;
    	
    	//Minimum cost to each of the other nodes
    	for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
    	{
    		int min = 999;
    		for (int j = 0; j < NetworkSimulator.NUMENTITIES; j++)
    		{
    			if (distanceTable[i][j] < min)
    			{
    				min = distanceTable[i][j];
    			}
    		}
    		minimumCosts[i] = min;
    	}
    	
    	//Send all direct neighbors the minimum costs to all nodes
    	for (int i : directNeighbors)
    	{
    		Packet p = new Packet (0, i, minimumCosts);
    		NetworkSimulator.toLayer2(p);
    	}    	
    }
    
    // Handle updates when a packet is received.  Students will need to call
    // NetworkSimulator.toLayer2() with new packets based upon what they
    // send to update.  Be careful to construct the source and destination of
    // the packet correctly.  Read the warning in NetworkSimulator.java for more
    // details.
    public void update(Packet p)
    {        
    	//Says whether the minimum costs updated given the new information
    	boolean didUpdate = false;
    	
    	for(int i = 0; i < minimumCosts.length; i++)
    	{
    		int newCostToNode = p.getMincost(i) + minimumCosts[p.getSource()];
    		int currentCostToNode = distanceTable[i][p.getSource()];
    		
    		//If the new cost to a node is less than the current, update the distance table
    		if (newCostToNode < currentCostToNode)
    		{
    			distanceTable[i][p.getSource()] = newCostToNode;
    			
    			//Check to see if this new cost is also a minimum cost
    			if (newCostToNode < minimumCosts[i])
    			{
    				minimumCosts[i] = newCostToNode;
    				didUpdate = true;
    			}
    		}
    	}
    	
    	//If update occurred then send update to direct neighbors
    	if (didUpdate)
    	{
    		for(int i : directNeighbors)
    		{
    			Packet updatePacket = new Packet (0, i, minimumCosts);
    			NetworkSimulator.toLayer2(updatePacket);
    		}
    	}
    }
    
    public void linkCostChangeHandler(int whichLink, int newCost)
    {
    }
    
    public void printDT()
    {
        System.out.println();
        System.out.println("           via");
        System.out.println(" D0 |   1   2   3");
        System.out.println("----+------------");
        for (int i = 1; i < NetworkSimulator.NUMENTITIES; i++)
        {
            System.out.print("   " + i + "|");
            for (int j = 1; j < NetworkSimulator.NUMENTITIES; j++)
            {
                if (distanceTable[i][j] < 10)
                {    
                    System.out.print("   ");
                }
                else if (distanceTable[i][j] < 100)
                {
                    System.out.print("  ");
                }
                else 
                {
                    System.out.print(" ");
                }
                
                System.out.print(distanceTable[i][j]);
            }
            System.out.println();
        }
    }
}