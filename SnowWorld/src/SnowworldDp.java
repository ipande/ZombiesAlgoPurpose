package src;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
public class SnowworldDp {
	private ArrayList<Integer> outputArray = new ArrayList<Integer>();
	private int g[][], p[][], npow, N, d[][];
	public static long time;
	public SnowworldDp() {

	}

	public ArrayList<Integer> computeTSP(int[][] inputArray, int n) 
	{
		long start = System.nanoTime();
		N = n;npow = (int) Math.pow(2, n);
		g = new int[n][npow];p = new int[n][npow];
		d = inputArray;int i, j, k, l, m, s;
		for (i = 0; i < n; i++) {
			for (j = 0; j < npow; j++) {
				g[i][j] = -1;p[i][j] = -1;
			}
		} 
		
		//initialize based on distance matrix
		for (i = 0; i < n; i++) {
			g[i][0] = inputArray[i][0];
		}
		
		int result = tsp(0, npow - 2);
		outputArray.add(0);
		getPath(0, npow - 2);
		outputArray.add(result);
		long end = System.nanoTime();
		time = (end - start) / 1000;
		return outputArray;
	}
	
	private int tsp(int start, int set) {
		int masked, mask, result = -1, temp;
		
		if (g[start][set] != -1) {
			return g[start][set];
		} 
		
		else {
			for (int x = 0; x < N; x++) {
				mask = npow - 1 - (int) Math.pow(2, x);
				masked = set & mask;
				if (masked != set) {
					temp = d[start][x] + tsp(x, masked);
					if (result == -1 || result > temp) {
						result = temp;p[start][set] = x;
					}
				}
			}
			
			g[start][set] = result;
			return result;
			
		}
		
	}
	
	private void getPath(int start, int set) {
		
		if (p[start][set] == -1) {return;}
		
		int x = p[start][set];
		int mask = npow - 1 - (int) Math.pow(2, x);
		int masked = set & mask;
		outputArray.add(x);
		getPath(x, masked);
		
	}
	
	static ArrayList<Edge> getEdgeList(int e, Scanner reader){
        //Scanner reader = new Scanner(System.in);    
        ArrayList<Edge> edges = new ArrayList<Edge>();
        for(int i = 0; i < e; i++){
            edges.add(new Edge(reader.nextInt(), reader.nextInt(), 
                        reader.nextInt()));  
        }
        return edges;
        
    }
    static int[][] adjacencyMatrix(int n, ArrayList<Edge> edges){
        int adjMat[][] = new int[n][n];
        for(int i = 0; i < edges.size(); i++){
            adjMat[edges.get(i).getTo()][edges.get(i).getFrom()] = edges.get(i).getSnow();
            adjMat[edges.get(i).getFrom()][edges.get(i).getTo()] = edges.get(i).getSnow();
        }
        return adjMat;
    }  

	
	
	
	public static void main(String[] args) throws FileNotFoundException {
		
		FileReader fin = new FileReader( args[0] );
		Scanner reader = new Scanner(fin);
        int n;
        int e;
        double alpha;
        n = reader.nextInt();
        e = reader.nextInt();
        alpha = reader.nextDouble();
        
        ArrayList<Edge> edges = getEdgeList(e, reader);
        int[][] adjMat = adjacencyMatrix(n, edges);        
       
        for(int i=0; i<n;i++) {
        	for(int j=0; j<n ; j++) {
        		System.out.print(adjMat[i][j] + "\t");
        	}
        	System.out.println();
        }
        
        SnowworldDp snowWorld = new SnowworldDp(); 
        ArrayList<Integer> path = snowWorld.computeTSP(adjMat,n);
        System.out.println(path);
        
	}

}
