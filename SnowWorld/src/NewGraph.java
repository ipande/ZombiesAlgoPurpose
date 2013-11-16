package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;

public class NewGraph {

	HashMap<Integer,List<VertSnow>> graph = new HashMap<Integer,List<VertSnow>>();
	
	public void addNewEdge(int srcVertex, int destVertex, int snow) {
		LinkedList<VertSnow> list = (LinkedList<VertSnow>) graph.get(srcVertex);
		if(list == null)
			list = new LinkedList<VertSnow>();
		list.add(new VertSnow(destVertex, snow));
		graph.put(srcVertex, list);
	}
	public class VertSnow {
		public VertSnow(int destVertex, int snow2) {
			vertex = destVertex;
			snow = snow2;
		}
		int vertex;
		int snow;
	}
	
	public void printGraph() {
		for(Entry<Integer,List<VertSnow>> entry: graph.entrySet()) {
			System.out.print(entry.getKey() + ":" );
			for(VertSnow obj: entry.getValue())
				System.out.print(obj.vertex+ ","+obj.snow+" ");
			System.out.println();
		}
	}
	
	int getMaximumUnvisited(int node, HashSet<Integer> visited)
	{
	  LinkedList<VertSnow> edges = (LinkedList<VertSnow>) graph.get(node);
	  int snow=0;
	  int nextElement = -1;
	  
	  for(VertSnow element: edges) {
		  if(!visited.contains(element.vertex) && element.snow > snow)
		  {
			  snow=element.snow;
			  nextElement = element.vertex;
		  }
	  }
	  
	  return nextElement;
	  
	}
	
	
	public List<Integer> getBestPathGreedy() {
		
		List<Integer> path = new LinkedList<Integer>();
		HashSet<Integer> visited = new HashSet<Integer>();
		
		path.add(0);
		visited.add(0);

		int nextNode = getMaximumUnvisited(0,visited);
		
		while( nextNode != -1) {
			visited.add(nextNode);
			path.add(nextNode);
			nextNode = getMaximumUnvisited(nextNode,visited);
		}
		return path;
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NewGraph g = new NewGraph( );
        try
        {
            FileReader fin = new FileReader( args[0] );
            BufferedReader graphFile = new BufferedReader( fin );       
            String line;
            
            line = graphFile.readLine();
            int numCities = Integer.parseInt(line);
            
            line = graphFile.readLine();
            int edges = Integer.parseInt(line);
            
            line = graphFile.readLine();
            Double alpha = Double.parseDouble(line);
           
            
            while( ( line = graphFile.readLine( ) ) != null )
            {
                StringTokenizer st = new StringTokenizer( line );
                try
                {
                    if( st.countTokens( ) != 3 )
                    {
                        System.err.println( "Skipping ill-formatted line " + line );
                        continue;
                    }
                    int source  = Integer.parseInt(st.nextToken( ));
                    int dest    = Integer.parseInt(st.nextToken( ));
                    int snow    = Integer.parseInt( st.nextToken( ) );
                    g.addNewEdge(source, dest, snow);
                    g.addNewEdge(dest, source, snow);
                }
                catch( NumberFormatException e )
                  { System.err.println( "Skipping ill-formatted line " + line ); }
             }
         }
         catch( IOException e )
           { System.err.println( e ); }

         g.printGraph();
         System.out.println(g.getBestPathGreedy());
         
	}
}
