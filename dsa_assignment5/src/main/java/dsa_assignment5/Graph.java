package dsa_assignment5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * An Undirected Graph class implemented in an Adjacency List style
 * 
 * This is an adjacency list representation of a graph.
 * Nodes are integers starting at 0. The textbooks usually describe
 * an adjacency list as a list (or array), indexed by node numbers,
 * of connection lists. If we did that in Java we would continually
 * end up checking for our node numbers being within range. Further,
 * we normally want a whole object for the node rather than just an
 * integer, so a {@code Map} instead of an array is a good choice.
 * 
 * The graph is represented by a {@code Map} which maps the node number
 * to a LinkedList of Connection objects, which represent the nodes that
 * are directly connected to this node. 
 *
 */
public class Graph
{
	// The underlying map representing the graph
	private Map<Integer, List<Connection>> graph = new HashMap<>();


	
	/**
	 * For marking purposes
	 * 
	 * @return Your student id
	 */
	public static String getStudentID()
	{
		// Change this return value to return your student id number, e.g. 
		// return "1234567";
		return "1986487";
	}

	/**
	 * For marking purposes
	 * 
	 * @return Your name
	 */
	public static String getStudentName()
	{
		// Change this return value to return your name, e.g.
		// return "John Smith";
		return "Tan Xiao Wei";
	}

	/**
	 * It is okay to create an empty graph, as we can add edges to it
	 */
	public Graph()
	{
	}

	/**
	 * Create a graph from an array of edges. Each edge is itself an array of 3 integers,
	 * the source node, the destination node and the distance between them.
	 * 
	 * Each edge in the input array will be added using {@code addEdge(node1,node2,distance)},
	 * implying that the reverse of each edge is also added and should NOT be explicitly in
	 * the input array.
	 * 
	 * @param connections the array of edges to add
	 * @throws GraphException if there are not exactly 3 integers in each edge array
	 */
	public Graph(int[][] connections)
		throws GraphException
	{
		for (int[] connection : connections)
		{
			if (connection.length != 3)
				throw new GraphException("Connections in Graphs must have 3 integers: node 1, node 2 and the distance between them. This connection did not: "
						+ Arrays.toString(connection));
			addEdge(connection[0], connection[1], connection[2]);
		}
	}

	/**
	 * Get an array of edges in the same form as the array of edges Graph constructor.
	 * One important difference is that this returns ALL the individual edges, thus
	 * one for the forward and one for the reverse direction of each true edge.
	 * @return the array of edges in the graph
	 */
	public int[][] getConnections()
	{
		ArrayList<int[]> connections = new ArrayList<>();
		for (Map.Entry<Integer, List<Connection>> entry : graph.entrySet())
		{
			int node = entry.getKey();
			for (Connection edge : entry.getValue())
				connections.add(new int[] { node, edge.getNode(), edge.getDistance() });
		}

		return connections.toArray(new int[0][0]);
	}

	/**
	 * Add an edge to the graph
	 * 
	 * This graph is UNDIRECTED, so any time we add an edge we must add the reverse edge as well.
	 * 
	 * @param node1 The source node
	 * @param node2 The destination node
	 * @param distance The distance or weight on the edge
	 * @throws GraphException if the distance is negative
	 */
	public void addEdge(int node1, int node2, int distance)
		throws GraphException
	{
		if (distance < 0)
			throw new GraphException(String.format("All distances must be greater than or equal to 0: attempted to add node %d to node %d with distance %d",
					node1, node2, distance));
		List<Connection> edgeList = graph.get(node1);
		if (edgeList == null)
		{
			edgeList = new LinkedList<>();
			graph.put(node1, edgeList);
		}
		edgeList.add(new Connection(node2, distance));

		//now add the reverse connection
		edgeList = graph.get(node2);
		if (edgeList == null)
		{
			edgeList = new LinkedList<>();
			graph.put(node2, edgeList);
		}
		edgeList.add(new Connection(node1, distance));
	}

	/**
	 * Contract a node that has exactly two connecting edges
	 * <p>
	 * This is the simplest case of contracting nodes. If node X is connected to node A with
	 * distance a and node B with distance b, then this should remove both the X-A and the X-B
	 * edge (essentially removing X from the graph), and add a new edge (A-B) with distance a+b
	 * </p>
	 * <p>
	 * Note that there may already be a pre-existing edge between A and B. This should not be changed
	 * or removed.
	 * </p>
	 * @param node the node to be contracted
	 * @throws GraphException if the node to be contracted does not exist in the graph or
	 * does not have precisely two edges
	 */
	public void contractNodeWithTwoEdges(int node)
		throws GraphException
	{
		// WRITE YOUR CODE HERE
		List<Connection> edgeList = graph.get(node);
		if (edgeList.size() == 2 && graph.containsKey(node))
		{
			Connection connection1 = edgeList.get(0); // connection of the node to be contracted to targetNode1
			Connection connection2 = edgeList.get(1); // connection of the node to be contracted to targetNode2
			int targetNode1 = connection1.getNode(); 
			int targetNode2 = connection2.getNode();
			int distance1 = connection1.getDistance();
			int distance2 = connection2.getDistance();
			

			graph.remove(node); // remove the current node and its connections to the 2 nodes
			
			List<Connection> edgeList2 = graph.get(targetNode1); // getting a list of connections for the targetNode1
			
			for (int i = 0; i < edgeList2.size(); i++) // iterating through its edgeList to find its connection to the node to be extracted
			{
				if (edgeList2.get(i).getNode() == node)
				{
					edgeList2.remove(edgeList2.get(i)); // removing that connection to the node to be contracted
				}
			}
			
			List<Connection> edgeList3 = graph.get(targetNode2); // getting a list of connections for the targetNode
			
			for (int j = 0; j < edgeList3.size(); j++)
			{
				if (edgeList3.get(j).getNode() == node) // iterating through its edgeList to find its connection to the node to be extracted
				{
					edgeList3.remove(edgeList3.get(j)); // removing that connection to the node to be contracted
				}
			}
			
			addEdge(targetNode1, targetNode2, distance1 + distance2); // adding a new Edge
		}
		
		else 
		{
			throw new GraphException();
		}

	}

	/**
	 * Apply Dijkstra's algorithm to find the distance between 2 nodes in the graph
	 * <p>
	 * The version of the algorithm to implement is Version 1 from the Theory part
	 * handout of the module. Do not try to use Java's PriorityQueue to implement Version
	 * 2 as that cannot cope with the priority of an object in the queue changing while 
	 * the object is in the queue.
	 * </p>
	 * <p>
	 * Rather than using the arrays as described in the handout, I recommend you use 
	 * <ul>
	 * <li>
	 * {@code Integer.MAX_VALUE} for infinity 
	 * </li>
	 * <li>
	 * A {@code Map<Integer, Integer>} for the {@code D} array in the notes, to record the
	 * minimum distance found so far from the start node for each node in the graph
	 * </li>
	 * <li>
	 * A {@code Set<Integer>} called {@code nonTight} of nodes to represent the set of nodes
	 * for which you have not yet found tight distance values for. This is equivalent to the
	 * {@code tight} array of booleans in the handout but it is much easier and more efficient to
	 * iterate over the {@code nonTight} set than iterate over the nodes whose boolean values
	 * in the {@code tight} array are true 
	 * </li>
	 * </ul>
	 * </p>
	 * There is no need to have a {@code pred} variable as there is no need to calculate
	 * or return the route of the shortest path 
	 * <p>
	 * </p>
	 * @param node1 the start node in the pair between which the distance is to be found
	 * @param node2 the final node in the pair between which the distance is to be found
	 * @return the distance between the pair of nodes
	 * @throws GraphException if either of the nodes are not in the graph or there is no path
	 * between them
	 */


	
	public int dijkstra(int node1, int node2)
			throws GraphException
	{
		// WRITE YOUR CODE HERE AND CHANGE THE RETURN STATEMENT
		try {
			if (graph.containsKey(node1) == false || graph.containsKey(node2) == false) // check if the nodes are part of the graph, if they aren't
			{
				throw new GraphException(); // throw a graph exception
			}
					
			Map<Integer, Integer> D = new HashMap<Integer, Integer>(); // a map to record the minimum distance from the start node to each node
			Map<Integer, Integer> D2 = new HashMap<Integer, Integer>(); // a temporary map to find the node with the minimum distance
			Set<Integer> nonTight = new HashSet<Integer>(); // a set of nodes for which the minimum distance is yet to be found
					
			D.put(node1, 0); // initially node1 is already part of D and node1 has zero distance to itself
					
			List<Connection> edgeList = graph.get(node1);
			for (int i = 0; i < edgeList.size(); i++)
			{
				int x = edgeList.get(i).getNode();
				D.put(x, edgeList.get(i).getDistance()); // initially for each neighbour of node1, the distance to any node is the minimum distance
				D2.put(x, edgeList.get(i).getDistance());
				nonTight.add(x);
			}
					
			for (int y = 0; y < graph.size(); y++)
			{
				if (y != node1 && (D.containsKey(y) == false)) // if the node isn't the starting node or a neighbour of the starting node
				{
					D.put(y, Integer.MAX_VALUE); // the initial minimum distance is infinity
					D2.put(y, Integer.MAX_VALUE);
					nonTight.add(y); // the node is part of nonTight
				}
			}
							
			while (nonTight.size() != 0) // while nonTight is not empty
			{
				int k = getMinNode(D2); // getting the node with the shortest distance from node1
				D2.remove(k); // removing it from D2
				nonTight.remove(k); // removing it from nonTight
				List<Connection> edgeList2 = graph.get(k);
				for (int m = 0; m < edgeList2.size(); m++) // for every neighbour of k
				{
					int tempNode = edgeList2.get(m).getNode();
					int distance1 = D.get(k);
					int distance2 = edgeList2.get(m).getDistance();
					int distance3 = D.get(tempNode);
											
					if (distance1 < (distance3 - distance2))
					{
						D.replace(tempNode, distance1 + distance2);
					}
				}
			}
			return D.get(node2);
		}
		catch (Exception NullPointerException) // if there is a null pointer exception, this means node1 and node2 aren't connected
		{
			throw new GraphException(); // a graph exception should be thrown
		}
						
	}
	
	private int getMinNode(Map<Integer, Integer> map) // method to find the smallest node from the D map
	{
		Entry<Integer, Integer> min = null;
		for (Entry<Integer, Integer> entry : map.entrySet())
		{
			if (min == null || min.getValue() > entry.getValue())
			{
				min = entry;
			}
		}
		return (min.getKey());
	}

}
