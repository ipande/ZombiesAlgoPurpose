package src;

import java.io.*;
import java.util.StringTokenizer;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.NoSuchElementException;

/**
 * <br/> This class is an implementation of Djikstra's shortest path algorithm<br/>
 * @author ishan
 *
 */
public class Graph 
{
	public static final double INFINITY = Double.MAX_VALUE;
	Map<String,Vertex> vertexMap = new HashMap<String,Vertex>( );
	public static String pathToReturn = "_";
	
	private void printPath( Vertex dest )
    {
        if( dest.prev != null )
        {
            printPath( dest.prev );
            System.out.print( " to " );
           
        }
        System.out.print( dest.Name );
        pathToReturn = pathToReturn+dest.Name+"_";
    }
	
	private Vertex getVertex( String vertexName ) 
	{
		 Vertex v = vertexMap.get(vertexName);
		 if(v==null)
		 {
			 v = new Vertex(vertexName);
			 vertexMap.put(vertexName, v);
		 }
		 return v;
	}
	
	public void addEdge( String sourceName, String destName, int cost )
	{
		Vertex v = getVertex(sourceName);
		Vertex w = getVertex(destName );
		v.adj.add(new Edge(w,cost)); 
	}
	
    
    public void printPath( String destName )
    {
        Vertex w = vertexMap.get( destName );
        if( w == null )
            throw new NoSuchElementException( "Destination vertex not found" );
        else if( w.dist == INFINITY )
            System.out.println( destName + " is unreachable" );
        else
        {
            System.out.print( "(Cost is: " + w.dist + ") " );
            printPath( w );
            System.out.println( );
        }
    }
    
    
	/**
	* <br/> Initializes the vertex output info prior to running * any shortest path algorithm.<br/>
	*/
	private void clearAll( )
	{
		for( Vertex v : vertexMap.values( ) ) 
			v.reset( );
	}
	
	// Represents an entry in the priority queue for Dijkstra's algorithm.
	class Path implements Comparable<Path>
	{
	    public Vertex     dest;   // w
	    public int     cost;   // d(w)
	    
	    public Path( Vertex d, int c )
	    {
	        dest = d;
	        cost = c;
	    }
	    
	    public int compareTo( Path rhs )
	    {
	        int otherCost = rhs.cost;
	        
	        return cost < otherCost ? -1 : cost > otherCost ? 1 : 0;
	    }
	}
	
	/**
    * <br/> Single-source weighted shortest-path algorithm.<br/>
    */
   public void dijkstra( String startName )
   {
       PriorityQueue<Path> pq = new PriorityQueue<Path>( );

       Vertex start = vertexMap.get( startName );
       if( start == null )
           throw new NoSuchElementException( "Start vertex not found" );

       clearAll( );
       pq.add( new Path( start, 0 ) ); start.dist = 0;
       
       int nodesSeen = 0;
       while( !pq.isEmpty( ) && nodesSeen < vertexMap.size( ) )
       {
           Path vrec = pq.remove( );
           Vertex v = vrec.dest;
           if( v.scratch != 0 )  // already processed v
               continue;
               
           v.scratch = 1;
           nodesSeen++;

           for( Edge e : v.adj )
           {
               Vertex w = e.dest;
               int cvw = e.steps;
               
               if( cvw < 0 )
                   throw new GraphException( "Graph has negative edges" );
                   
               if( w.dist > v.dist + cvw )
               {
                   w.dist = v.dist +cvw;
                   w.prev = v;
                   pq.add( new Path( w, w.dist ) );
               }
           }
       }
   }

	
	// Used to signal violations of preconditions for // various shortest path algorithms.
	class GraphException extends RuntimeException
	{
		public GraphException( String name ) { super( name ); }
	}
	
	public class Edge{
		public Vertex dest;
		public int steps;
		public Edge(Vertex d, int steps)
		{
			this.dest = d;
			this.steps = steps;
		}
	}

	public class Vertex{
		public String Name;
		public List<Edge> adj;
		public int dist;
		public Vertex prev;
		public int scratch;
		
		public Vertex(String name)
		{
			this.Name = name;
			this.adj = new LinkedList<Edge>();
			reset();
		}
		public void reset()
		{
			this.dist = (int) Graph.INFINITY;
			this.prev = null;
			this.scratch = 0;
		}
	}
	
	public List<Integer> greedyPath() {
		
		return null;
	}
	
	/**
     * <br/> Process a request; return false if end of file. <br/>
     */
    public static boolean processRequest( BufferedReader in, Graph g )
    {
        String startName = null;
        String destName = null;
        try
        {
            System.out.print( "Enter start node:" );
            if( ( startName = in.readLine( ) ) == null )
                return false;
            System.out.print( "Enter destination node:" );
            if( ( destName = in.readLine( ) ) == null )
                return false;
            g.dijkstra( startName );
            g.printPath( destName );
        }
        catch( IOException e )
          { System.err.println( e ); }
        catch( NoSuchElementException e )
          { System.err.println( e ); }          
        catch( GraphException e )
          { System.err.println( e ); }
        return true;
    }
    
    
    public static String myProcessNavigationRequest(String source, String destination, Graph g)
    {
		if(source==null || destination==null)
		{
			throw new IllegalArgumentException("source or destination incorrect, source provided = "+source+"\ndestination provided= "+destination);
		}
		else
		{
			pathToReturn = "_";
			try
	        {
				g.dijkstra( source );
				g.printPath( destination );
	        }
        
			catch( NoSuchElementException e )
				{ System.err.println( e ); }          
			catch( GraphException e )
				{ System.err.println( e ); }
			return pathToReturn;
		}
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Graph g = new Graph( );
        try
        {
            FileReader fin = new FileReader( args[0] );
            BufferedReader graphFile = new BufferedReader( fin );
            
            
            
            
            // Reads edges between nodes and constructs the graph in internal memory
            // Note: This can be done in the init method of the servlet for better efficiency
            // TODO: Make this part read from a database rather than a file
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
                    String source  = st.nextToken( );
                    String dest    = st.nextToken( );
                    int    cost    = Integer.parseInt( st.nextToken( ) );
                    g.addEdge( source, dest, cost );
                }
                catch( NumberFormatException e )
                  { System.err.println( "Skipping ill-formatted line " + line ); }
             }
         }
         catch( IOException e )
           { System.err.println( e ); }

         System.out.println( "File read..." );
         System.out.println( g.vertexMap.size( ) + " vertices" );

         System.out.println(g.greedyPath());
         
         
//         BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );
//         String source = "262";
//         String destination = "282";
//         
//         myProcessNavigationRequest(source, destination, g);
         
         
//         while( processRequest( in, g ) )
//             ;

	}

	
}