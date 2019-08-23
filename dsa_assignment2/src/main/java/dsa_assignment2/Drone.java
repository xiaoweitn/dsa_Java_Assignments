package dsa_assignment2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * A Drone class to simulate the decisions and information collected by a drone
 * on exploring an underground maze.
 * 
 */
public class Drone implements DroneInterface
{
	private static final Logger logger     = Logger.getLogger(Drone.class);
	
	public String getStudentID()
	{
		//change this return value to return your student id number
		return "1986487";
	}

	public String getStudentName()
	{
		//change this return value to return your name
		return "Tan Xiao Wei";
	}

	/**
	 * The Maze that the Drone is in
	 */
	private Maze                maze;

	/**
	 * The stack containing the portals to backtrack through when all other
	 * doorways of the current chamber have been explored (see assignment
	 * handout). Note that in Java, the standard collection class for both
	 * Stacks and Queues are Deques
	 */
	private Deque<Portal>       visitStack = new ArrayDeque<>();

	/**
	 * The set of portals that have been explored so far.
	 */
	private Set<Portal>         visited    = new HashSet<>();

	/**
	 * The Queue that contains the sequence of portals that the Drone has
	 * followed from the start
	 */
	private Deque<Portal>       visitQueue = new ArrayDeque<>();

	/**
	 * This constructor should never be used. It is private to make it
	 * uncallable by any other class and has the assert(false) to ensure that if
	 * it is ever called it will throw an exception.
	 */
	@SuppressWarnings("unused")
	private Drone()
	{
		assert (false);
	}

	/**
	 * Create a new Drone object and place it in chamber 0 of the given Maze
	 * 
	 * @param maze
	 *            the maze to put the Drone in.
	 */
	public Drone(Maze maze)
	{
		this.maze = maze;
	}

	/* 
	 * @see dsa_assignment2.DroneInterface#searchStep()
	 */
	@Override
	public Portal searchStep()
	{
		/* WRITE YOUR CODE HERE */
		int door = maze.getNumDoors();
		int curChamber = maze.getCurrentChamber();
		
		for (int i = 0; i < door; i++)
		{
			Portal x = new Portal(curChamber, i);

			
			if (visited.contains(x) == false) // if a door in a chamber has not been visited
			{
			
				visitQueue.add(x); // add it to the queue
				visitStack.push(x);// push it to the stack
				visited.add(x); // add it to the set
				x = maze.traverse(i);
				visitQueue.add(x); // add it to the queue
				visitStack.push(x);// add it to the stack
				visited.add(x); // add it to the set
				return x;
			}

			
		}
		
		if (visitStack.isEmpty() && maze.getCurrentChamber() == 0) // if the whole maze has been explored 
		{
			return null;
		}
		
		// if all the doors in a chamber has been visited
		Portal y = visitStack.pop(); // pop the most recent portals off the stack
		visitStack.pop(); 
		int prevDoor = y.getDoor();
		Portal z = maze.traverse(prevDoor); // backtracks
		visitQueue.add(y); // adding the backtracking to the queue
		visitQueue.add(z);
		return z;
		
	}

	/* 
	 * @see dsa_assignment2.DroneInterface#getVisitOrder()
	 */
	@Override
	public Portal[] getVisitOrder()
	{
		/* WRITE YOUR CODE HERE */
		// return an array containing all the items from visitQueue
		Deque<Portal> x = ((ArrayDeque<Portal>) visitQueue).clone(); // clones visitQueue
		Portal[] y = x.toArray(new Portal[visitQueue.size()]); // changes it to an array
		return y;
	}

	/*
	 * @see dsa_assignment2.DroneInterface#findPathBack()
	 */
	@Override
	public Portal[] findPathBack()
	{
		/* WRITE YOUR CODE HERE */
		Deque<Portal> x = ((ArrayDeque<Portal>) visitQueue).clone(); //clones visitQueue
		Portal[] y = x.toArray(new Portal[visitQueue.size()]); // changes it to an array
		
		for  (int i = 0; i < y.length/2; i++) // switches the first and last element to reverse the order of the array
		{
			Portal z = y[i];
			y[i] = y[y.length-1-i];
			y[y.length-1-i] = z;
		}
		
		Portal[] z = new Portal[y.length/2];
		int j = 0;
		for (int i = 0; i < y.length; i++) // removing the odd indexes
		{
			if (i % 2 == 0) // taking the even indexes
			{
				z[j] = y[i]; // adding it to a new array
				j++;				
			}
		}
		
	
//		int k = 0;
//		int counter = 0;
//		
//		for (k = z.length-1; k >= 0; k--)
//		{
//			
//			if (z[0].getChamber() == 0) // if the current chamber is 0
//			{
//				return new Portal[0]; // simply return an empty array
//			}
//			
//			counter++;
//			
//			if (z[k].getChamber() == z[0].getChamber()) // if the current chamber matches a chamber in visit order
//			{
//				break; // break out of the loop
//			}
//		}
//		
//		Portal[] e = new Portal[counter];
//		counter = 0;
//		for (int i = k; counter < e.length; i++) // taking everything after k as the path back
//		{
//			e[counter] = z[i]; // adding it to another array 
//			counter++;
//			
//		}
//		
//		return e;
		
		
		ArrayList<Portal> f = new ArrayList<Portal>(1);
		int k = 0;
		int index = 0;
		
		do
		{
			for (k = z.length-1; k >= index; k--)
			{
				if (z[0].getChamber() == 0)
				{
					return new Portal[0];
				}
				
//				index++;
				
				if (z[k].getChamber() == z[index].getChamber())
				{
					f.add(z[k]);
					index = k + 1;
					break;
				}
			}
		} while (z[z.length-1].getChamber() == f.get(f.size()-1).getChamber());
		
		Portal[] pathBack = f.toArray(new Portal[f.size()]);
		
		return pathBack;
	}

}
