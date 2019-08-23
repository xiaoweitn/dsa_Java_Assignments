package dsa_assignment3;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * A node in a binary tree representing a Propositional Logic expression.
 * <p>
 * Each node has a type (AND node, OR node, NOT node etc.) and can have zero one
 * or two children as required by the node type (AND has two children, NOT has
 * one, TRUE, FALSE and variables have none
 * </p>
 * <p>
 * This class is mutable, and some of the operations are intended to modify the
 * tree internally. There are a few cases when copies need to be made of whole
 * sub-tree nodes in such a way that these copied trees do not share any nodes
 * with their originals. To do this the class implements a deep copying copy
 * constructor <code>PLTreeNode(PLTreeNode node)</code>
 * </p>
 * 
 */
public final class PLTreeNode implements PLTreeNodeInterface
{
	private static final Logger logger = Logger.getLogger(PLTreeNode.class);

	NodeType                    type;
	PLTreeNode                  child1;
	PLTreeNode                  child2;

	/**
	 * For marking purposes
	 * 
	 * @return Your student id
	 */
	public static String getStudentID()
	{
		//change this return value to return your student id number
		return "1986487";
	}

	/**
	 * For marking purposes
	 * 
	 * @return Your name
	 */
	public static String getStudentName()
	{
		//change this return value to return your name
		return "Tan Xiao Wei";
	}

	/**
	 * The default constructor should never be called and has been made private
	 */
	private PLTreeNode()
	{
		throw new RuntimeException("Error: default PLTreeNode constuctor called");
	}

	/**
	 * The usual constructor used internally in this class. No checks made on
	 * validity of parameters as this has been made private, so can only be
	 * called within this class.
	 * 
	 * @param type
	 *            The <code>NodeType</code> represented by this node
	 * @param child1
	 *            The first child, if there is one, or null if there isn't
	 * @param child2
	 *            The second child, if there is one, or null if there isn't
	 */
	private PLTreeNode(NodeType type, PLTreeNode child1, PLTreeNode child2)
	{
		// Don't need to do lots of tests because only this class can create nodes directly,
		// any construction required by another class has to go through reversePolishBuilder,
		// which does all the checks
		this.type = type;
		this.child1 = child1;
		this.child2 = child2;
	}

	/**
	 * A copy constructor to take a (recursive) deep copy of the sub-tree based
	 * on this node. Guarantees that no sub-node is shared with the original
	 * other
	 * 
	 * @param node
	 *            The node that should be deep copied
	 */
	private PLTreeNode(PLTreeNode node)
	{
		if (node == null)
			throw new RuntimeException("Error: tried to call the deep copy constructor on a null PLTreeNode");
		type = node.type;
		if (node.child1 == null)
			child1 = null;
		else
			child1 = new PLTreeNode(node.child1);
		if (node.child2 == null)
			child2 = null;
		else
			child2 = new PLTreeNode(node.child2);
	}


	/**
	 * Takes a list of <code>NodeType</code> values describing a valid
	 * propositional logic expression in reverse polish notation and constructs
	 * the corresponding expression tree. c.f. <a href=
	 * "https://en.wikipedia.org/wiki/Reverse_Polish_notation">https://en.wikipedia.org/wiki/Reverse_Polish_notation</a>
	 * <p>
	 * Thus an input containing
	 * </p>
	 * <pre>
	 * {NodeType.P, NodeType.Q, NodeType.NOT, NodeType.AND.
	 * </pre>
	 * 
	 * corresponds to
	 * 
	 * <pre>
	 * P∧¬Q
	 * </pre>
	 * 
	 * Leaving out the <code>NodeType</code> enum class specifier, we get that
	 * 
	 * <pre>
	 * { R, P, OR, TRUE, Q, NOT, AND, IMPLIES }
	 * </pre>
	 * 
	 * represents
	 * 
	 * <pre>
	 * ((R∨P)→(⊤∧¬Q))
	 * </pre>
	 * 
	 * @param typeList
	 *            An <code>NodeType</code> array in reverse polish order
	 * @return the <code>PLTreeNode</code> of the root of the tree representing
	 *         the expression constructed for the reverse polish order
	 *         <code>NodeType</code> array
	 */
	public static PLTreeNodeInterface reversePolishBuilder(NodeType[] typeList)
	{
		if (typeList == null || typeList.length == 0)
		{
			logger.error("Trying to create an empty PLTree");
			return null;
		}

		Deque<PLTreeNode> nodeStack = new LinkedList<>();

		for (NodeType type : typeList)
		{
			int arity = type.getArity();

			if (nodeStack.size() < arity)
			{
				logger.error(String.format(
						"Error: Malformed reverse polish type list: \"%s\" has arity %d, but is being applied to only %d arguments", //
						type, arity, nodeStack.size()));
				return null;
			}
			if (arity == 0)
				nodeStack.addFirst(new PLTreeNode(type, null, null));
			else if (arity == 1)
			{
				PLTreeNode node1 = nodeStack.removeFirst();
				nodeStack.addFirst(new PLTreeNode(type, node1, null));
			}
			else
			{
				PLTreeNode node2 = nodeStack.removeFirst();
				PLTreeNode node1 = nodeStack.removeFirst();
				nodeStack.addFirst(new PLTreeNode(type, node1, node2));
			}
		}
		if (nodeStack.size() > 1)
		{
			logger.error("Error: Incomplete term: multiple subterms not combined by top level symbol");
			return null;
		}

		return nodeStack.removeFirst();
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#getReversePolish()
	 */
	@Override
	public NodeType[] getReversePolish()
	{
		Deque<NodeType> nodeQueue = new LinkedList<>();

		getReversePolish(nodeQueue);

		return nodeQueue.toArray(new NodeType[0]);

	}

	/**
	 * A helper method for <code>getReversePolish()</code> used to accumulate
	 * the elements of the reverse polish notation description of the current
	 * tree
	 * 
	 * @param nodeQueue
	 *            A queue of <code>NodeType</code> objects used to accumulate
	 *            the values of the reverse polish notation description of the
	 *            current tree
	 */
	private void getReversePolish(Deque<NodeType> nodeQueue)
	{
		if (child1 != null)
			child1.getReversePolish(nodeQueue);
		if (child2 != null)
			child2.getReversePolish(nodeQueue);
		nodeQueue.addLast(type);
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#toString()
	 */
	@Override
	public String toString()
	{
		return toStringPrefix();
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#toStringPrefix()
	 */
	@Override
	public String toStringPrefix()
	{
		// WRITE YOUR CODE HERE, CHANGING THE RETURN STATEMENT IF NECESSARY
		int arity = type.getArity();
		String currentNode = type.getPrefixName();
		String temp = "";
		
		if (currentNode == null) // if the currentNode is null
		{
			return ""; // return empty
		}
		if (currentNode != null) // if the currentNode is not null
		{
			temp += type.getPrefixName(); // add the currentNode
		}

		if (arity == 1) // if the currentNode has 1 subtree
		{
			temp += "(" + child1.toStringPrefix() + ")"; // do the same thing for that subtree and add that to the output
		}
		if (arity == 2) // if the currentNode has 2 subtree
		{
			temp += "("+ child1.toStringPrefix() + "," + child2.toStringPrefix() +")"; // do the same thing for the 2 subtrees and add that to the output
		}
				
		return temp;
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#toStringInfix()
	 */
	@Override
	public String toStringInfix()
	{
		// WRITE YOUR CODE HERE, CHANGING THE RETURN STATEMENT IF NECESSARY
		int arity = type.getArity();
		String currentNode = type.getInfixName();
		if (currentNode == null) // if the currentNode is null
		{
			return ""; // return empty
		}
		if (currentNode != null)
		{	
			if (arity == 0) // if there are no subtrees for the currentNode
			{
				return type.getInfixName(); // simply return the currentNode
			}
			if (arity == 1) // if there is one subtree
			{
				return  type.getInfixName() + child1.toStringInfix() ; // do the same thing for the subtree and add that to the output
			}
			if (arity == 2) // if there are 2 subtrees
			{
				return "(" + child1.toStringInfix() + type.getInfixName() + child2.toStringInfix() + ")"; // do the same thing for the subtrees and add that to the output
			}
		}
			
		return null;
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#applyVarBindings(java.util.Map)
	 */
	@Override
	public void applyVarBindings(Map<NodeType, Boolean> bindings)
	{
		// WRITE YOUR CODE HERE
		int arity = type.getArity();
		if (type != null) // if the current node is not null
		{			
			if (arity == 0) // if the node has no trees
			{
				for (Map.Entry<NodeType, Boolean> entry : bindings.entrySet())  // iterates through the map
				{
				    NodeType key = entry.getKey();
				    Boolean value = entry.getValue();
				    if (type == key) // checking if the node matches the key
				    {
				    	if (value == true) // if it does
				    	{
				    		type = NodeType.TRUE; // set it to true
				    	}
				    	else // if it doesn't
				    	{
				    		type = NodeType.FALSE; // set it to false
				    	}
				    }   
				}
			}
			
			if (arity == 1) // if there is one subtree
			{
				child1.applyVarBindings(bindings); // do the same for that subtree
			}
			if (arity == 2) // if there are 2 subtrees
			{
				child1.applyVarBindings(bindings); // do the same for both subtrees
				child2.applyVarBindings(bindings);
			}
		}
		return;
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#evaluateConstantSubtrees()
	 */
	@Override
	public Boolean evaluateConstantSubtrees()
	{
		// WRITE YOUR CODE HERE, CHANGING THE RETURN STATEMENT IF NECESSARY
		
		if (child1 != null && child2 != null)
		{
			child1.evaluateConstantSubtrees();
			child2.evaluateConstantSubtrees();
		}
		
		else if (child1 != null && child2 == null)
		{
			child1.evaluateConstantSubtrees();
		}
		
		else if (child1 == null && child2 != null)
		{
			child2.evaluateConstantSubtrees();
		}
		
		
//		if (type.getArity() == 1)
//		{
//			child1.evaluateConstantSubtrees();
//		}
//		
//		if (type.getArity() == 2)
//		{
//			child1.evaluateConstantSubtrees();
//			child2.evaluateConstantSubtrees();
//		}
		
//		if (type.getArity() == 1)
//		{
//			if (child1.type.equals(NodeType.TRUE))
//			{
//				type = NodeType.FALSE;
//				child1 = null;
//				child2 = null;
//				return false;
//			}
//				
//			else if (child1.type.equals(NodeType.FALSE))
//			{
//				type = NodeType.TRUE;
//				child1 = null;
//				child2 = null;
//				return true;
//			}
//			
//			if (type.getArity() == 2)
//			{
//				if ((type.equals(NodeType.AND)) || (type.equals(NodeType.OR)) || (type.equals(NodeType.IMPLIES)) && child1.type.equals(NodeType.TRUE) && child2.type.equals(NodeType.TRUE)) // T T RIGHT
//				{
//					type = NodeType.TRUE;
//					child1 = null;
//					child2 = null;
//					return true;
//				}	
//					
//				else if ((type.equals(NodeType.AND)) || (type.equals(NodeType.IMPLIES)) && child1.type.equals(NodeType.TRUE) && child2.type.equals(NodeType.FALSE)) // T F RIGHT
//				{
//					type = NodeType.FALSE;
//					child1 = null;
//					child2 = null;
//					return false;
//				}
//					
//				else if (type.equals(NodeType.OR) && child1.type.equals(NodeType.TRUE) && child2.type.equals(NodeType.FALSE)) // T F RIGHT
//				{
//					type = NodeType.TRUE;
//					child1 = null;
//					child2 = null;
//					return true;
//				}
//				
//					
//				else if ((type.equals(NodeType.AND) || (type.equals(NodeType.IMPLIES)) && child1.type.equals(NodeType.TRUE) && child2.type != NodeType.TRUE && child2.type != NodeType.FALSE)) // T N RIGHT
//				{
//					PLTreeNode temp = (child2.child1);
//					PLTreeNode temp2 = (child2.child2);
//					type = child2.type;
//					child1 = temp;
//					child2 = temp2;
//					return null;
//			
//				}
//					
//				else if (type.equals(NodeType.OR) && child1.type.equals(NodeType.TRUE) && child2.type!= NodeType.TRUE && child2.type != NodeType.FALSE) // T N RIGHT
//				{
//					type = NodeType.TRUE;
//					child1 = null;
//					child2 = null;
//					return true;
//				}
//					
//				else if (type.equals(NodeType.AND) && child1.type.equals(NodeType.FALSE) && child2.type.equals(NodeType.TRUE)) // F T RIGHT
//				{
//					type = NodeType.FALSE;
//					child1 = null;
//					child2 = null;
//					return false;
//				}
//					
//				else if ((type.equals(NodeType.OR) || (type.equals(NodeType.IMPLIES) && child1.type.equals(NodeType.FALSE) && child2.type.equals(NodeType.TRUE)))) // F T RIGHT
//				{
//					type = NodeType.TRUE;
//					child1 = null;
//					child2 = null;
//					return true;
//				}
//					
//				else if ((type.equals(NodeType.AND) || (type.equals(NodeType.OR) && child1.type.equals(NodeType.FALSE) && child2.type.equals(NodeType.FALSE)))) // F F RIGHT
//				{
//					type = NodeType.FALSE;
//					child1 = null;
//					child2 = null;
//					return false;
//				}
//					
//				else if (type.equals(NodeType.IMPLIES) && child1.type.equals(NodeType.FALSE) && child2.type.equals(NodeType.FALSE)) // F F RIGHT
//				{
//					type = NodeType.TRUE;
//					child1 = null;
//					child2 = null;
//					return true;
//				}
//					
//				else if (type.equals(NodeType.AND) && child1.type.equals(NodeType.FALSE) && child2.type != NodeType.TRUE && child2.type != NodeType.FALSE) // F N RIGHT
//				{
//					type = NodeType.FALSE;
//					child1 = null;
//					child2 = null;
//					return false;
//				}
//					
//				else if (type.equals(NodeType.OR) && child1.type.equals(NodeType.FALSE) && child2.type != NodeType.TRUE && child2.type != NodeType.FALSE) // F N RIGHT
//				{
//					PLTreeNode temp = (child2.child1);
//					PLTreeNode temp2 = (child2.child2);
//					type = child2.type;
//					child1 = temp;
//					child2 = temp2;
//					return null;
//			
//				}
//					
//				else if (type.equals(NodeType.IMPLIES) && child1.type.equals(NodeType.FALSE) && child2.type != NodeType.TRUE && child2.type != NodeType.FALSE) // F N RIGHT
//				{
//					type = NodeType.TRUE;
//					child1 = null;
//					child2 = null;
//					return true;
//				}
//					
//				else if (type.equals(NodeType.AND) && child2.type.equals(NodeType.TRUE) && child1.type != NodeType.TRUE && child2.type != NodeType.FALSE) // N T RIGHT
//				{
//					PLTreeNode temp = (child1.child1);
//					PLTreeNode temp2 = (child1.child2);
//					type = child1.type;
//					child1 = temp;
//					child2 = temp2;
//					return null;
//			
//				}
//				
//				else if ((type.equals(NodeType.OR) || (type.equals(NodeType.IMPLIES) && child2.type.equals(NodeType.TRUE) && child1.type != NodeType.TRUE && child2.type != NodeType.FALSE))) // N T RIGHT
//				{
//					type = NodeType.TRUE;
//					child1 = null;
//					child2 = null;
//					return true;
//				}
//					
//				else if (type.equals(NodeType.AND) && child2.type.equals(NodeType.FALSE) && child1.type != NodeType.TRUE && child2.type != NodeType.FALSE) // N F RIGHT
//				{
//					type = NodeType.FALSE;
//					child1 = null;
//					child2 = null;
//					return false;
//				}
//				
//				else if (type.equals(NodeType.OR) && child2.type.equals(NodeType.FALSE) && child1.type != NodeType.TRUE && child2.type != NodeType.FALSE) // N F RIGHT
//				{
//					PLTreeNode temp = (child1.child1);
//					PLTreeNode temp2 = (child1.child2);
//					type = child1.type;
//					child1 = temp;
//					child2 = temp2;
//					return null;
//			
//				}
//					
//				else if (type.equals(NodeType.IMPLIES) && child2.type.equals(NodeType.FALSE) && child1.type != NodeType.TRUE && child2.type != NodeType.FALSE) // N F RIGHT
//				{
//					type = NodeType.NOT;
//					child2 = null;
//					return null;
//				}
//			}
//		}
		
		
		if (type.equals(NodeType.NOT))
		{
			if (child1.type.equals(NodeType.TRUE))
			{
				type = NodeType.FALSE;
				child1 = null;
				child2 = null;
				return false;
			}
				
			else if (child1.type.equals(NodeType.FALSE))
			{
				type = NodeType.TRUE;
				child1 = null;
				child2 = null;
				return true;
			}
		}
		
		
		else if (type.equals(NodeType.AND) && child2.type.equals(NodeType.TRUE) && child1.type != NodeType.TRUE && child1.type != NodeType.FALSE) // N T RIGHT
		{
			PLTreeNode temp = (child1.child1);
			PLTreeNode temp2 = (child1.child2);
			type = child1.type;
			child1 = temp;
			child2 = temp2;
			return null;
	
		}
		
		else if (type.equals(NodeType.OR) && child2.type.equals(NodeType.FALSE) && child1.type != NodeType.TRUE && child1.type != NodeType.FALSE) // N F RIGHT
		{
			PLTreeNode temp = (child1.child1);
			PLTreeNode temp2 = (child1.child2);
			type = child1.type;
			child1 = temp;
			child2 = temp2;
			return null;
	
		}
		
		else if (type.equals(NodeType.OR) && child1.type.equals(NodeType.FALSE) && child2.type != NodeType.TRUE && child2.type != NodeType.FALSE) // F N RIGHT
		{
			PLTreeNode temp = (child2.child1);
			PLTreeNode temp2 = (child2.child2);
			type = child2.type;
			child1 = temp;
			child2 = temp2;
			return null;
	
		}
		
		else if ((type.equals(NodeType.AND) || (type.equals(NodeType.IMPLIES)) && child1.type.equals(NodeType.TRUE) && child2.type != NodeType.TRUE && child2.type != NodeType.FALSE)) // T N RIGHT
		{
			PLTreeNode temp = (child2.child1);
			PLTreeNode temp2 = (child2.child2);
			type = child2.type;
			child1 = temp;
			child2 = temp2;
			return null;
	
		}
		
		else if (type.equals(NodeType.IMPLIES) && child2.type.equals(NodeType.FALSE) && child1.type != NodeType.TRUE && child1.type != NodeType.FALSE) // N F RIGHT
		{
			type = NodeType.NOT;
			child2 = null;
			return null;
		}
		
		else if (type.equals(NodeType.AND) && child2.type.equals(NodeType.FALSE) && child1.type != NodeType.TRUE && child1.type != NodeType.FALSE) // N F RIGHT
		{
			type = NodeType.FALSE;
			child1 = null;
			child2 = null;
			return false;
		}
		
		else if (type.equals(NodeType.AND) && child1.type.equals(NodeType.FALSE) && child2.type != NodeType.TRUE && child2.type != NodeType.FALSE) // F N RIGHT
		{
			type = NodeType.FALSE;
			child1 = null;
			child2 = null;
			return false;
		}
		
		else if ((type.equals(NodeType.AND) || (type.equals(NodeType.OR) && child1.type.equals(NodeType.FALSE) && child2.type.equals(NodeType.FALSE)))) // F F RIGHT
		{
			type = NodeType.FALSE;
			child1 = null;
			child2 = null;
			return false;
		}
		
		else if (type.equals(NodeType.AND) && child1.type.equals(NodeType.FALSE) && child2.type.equals(NodeType.TRUE)) // F T RIGHT
		{
			type = NodeType.FALSE;
			child1 = null;
			child2 = null;
			return false;
		}
		
		else if ((type.equals(NodeType.AND)) || (type.equals(NodeType.IMPLIES)) && child1.type.equals(NodeType.TRUE) && child2.type.equals(NodeType.FALSE)) // T F RIGHT
		{
			type = NodeType.FALSE;
			child1 = null;
			child2 = null;
			return false;
		}
		
		else if (type.equals(NodeType.IMPLIES) && child1.type.equals(NodeType.FALSE) && child2.type != NodeType.TRUE && child2.type != NodeType.FALSE) // F N RIGHT
		{
			type = NodeType.TRUE;
			child1 = null;
			child2 = null;
			return true;
		}
		
		else if ((type.equals(NodeType.OR) || (type.equals(NodeType.IMPLIES) && child2.type.equals(NodeType.TRUE) && child1.type != NodeType.TRUE && child1.type != NodeType.FALSE))) // N T RIGHT
		{
			type = NodeType.TRUE;
			child1 = null;
			child2 = null;
			return true;
		}
		
		else if (type.equals(NodeType.IMPLIES) && child1.type.equals(NodeType.FALSE) && child2.type.equals(NodeType.FALSE)) // F F RIGHT
		{
			type = NodeType.TRUE;
			child1 = null;
			child2 = null;
			return true;
		}
		
		else if ((type.equals(NodeType.OR) || (type.equals(NodeType.IMPLIES) && child1.type.equals(NodeType.FALSE) && child2.type.equals(NodeType.TRUE)))) // F T RIGHT
		{
			type = NodeType.TRUE;
			child1 = null;
			child2 = null;
			return true;
		}
	
	
		else if (type.equals(NodeType.OR) && child1.type.equals(NodeType.TRUE) && child2.type!= NodeType.TRUE && child2.type != NodeType.FALSE) // T N RIGHT
		{
			type = NodeType.TRUE;
			child1 = null;
			child2 = null;
			return true;
		}
		
		else if (type.equals(NodeType.OR) && child1.type.equals(NodeType.TRUE) && child2.type.equals(NodeType.FALSE)) // T F RIGHT
		{
			type = NodeType.TRUE;
			child1 = null;
			child2 = null;
			return true;
		}
		
		else if ((type.equals(NodeType.AND)) || (type.equals(NodeType.OR)) || (type.equals(NodeType.IMPLIES)) && child1.type.equals(NodeType.TRUE) && child2.type.equals(NodeType.TRUE)) // T T RIGHT
		{
			type = NodeType.TRUE;
			child1 = null;
			child2 = null;
			return true;
		}
		
	
	return null;
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#reduceToCNF()
	 */
	@Override
	public void reduceToCNF()
	{
		eliminateImplies();
		pushNotDown();
		pushOrBelowAnd();
		makeAndOrRightDeep();
		return;
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#eliminateImplies()
	 */
	@Override
	public void eliminateImplies()
	{
		// WRITE YOUR CODE HERE
		int arity = type.getArity();
		if (type != null)
		{
			if (type == NodeType.IMPLIES)
			{
				type = NodeType.OR;
				PLTreeNode not = new PLTreeNode(NodeType.NOT, child1, null);
				child1 = not;
			}
			if (type.getArity() == 1)
			{
				child1.eliminateImplies();
			}
			if (type.getArity() == 2)
			{
				child1.eliminateImplies();
				child2.eliminateImplies();
			}
		}
		return;
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#pushNotDown()
	 */
	@Override
	public void pushNotDown()
	{
		// WRITE YOUR CODE HERE
		int arity = type.getArity();
		if (type.getArity() == 1) // if the currentNode is a not 
		{
			if (child1.type.equals(NodeType.NOT)) // if the child is also a not
			{
				type = child1.child1.type; // reduce the 2 not's and simply make the currentNode the first node that isn't a node
				
				if (type.getArity() == 0)
				{
					child1 = null;
					return;
				}
				else if (type.getArity() == 1) // if the currentNode has a subtree
				{
//					PLTreeNode temp = new PLTreeNode(child1.child1.child1);
					PLTreeNode temp = child1.child1.child1;
					child1 = temp; // make its child its original child
					child1.pushNotDown();
				}
					
				else if (type.getArity() == 2) // if the currentNode has 2 subtrees
				{
//					PLTreeNode temp = new PLTreeNode(child1.child1.child1);
//					PLTreeNode temp2 = new PLTreeNode(child1.child1.child2);
					PLTreeNode temp = (child1.child1.child1);
					PLTreeNode temp2 =(child1.child1.child2);
					child1 = temp; // make both its children both its original children
					child2 = temp2;
					child1.pushNotDown();
					child2.pushNotDown();
				}
				
			}
				
			else if (child1.type.equals(NodeType.OR)) // else if the child is an or
			{
				type = NodeType.AND; 
				PLTreeNode not = new PLTreeNode(NodeType.NOT, child1.child2, null);
	//			PLTreeNode temp = new PLTreeNode(child1.child1);
	//			PLTreeNode temp2 = new PLTreeNode(child1.child2);
				PLTreeNode temp = (child1.child1);
				PLTreeNode temp2 = (child1.child2);
				child1.type = NodeType.NOT;
//				child2.type = NodeType.NOT;					
				child1.child1 = temp;
//				child2.child1 = temp2;
				child1.child2 = null;
				child2 = not;
				child1.pushNotDown();
				child2.pushNotDown();

			}
					
			else if (child1.type.equals(NodeType.AND)) // if the child is an and
			{
						
				type = NodeType.OR;
				PLTreeNode not = new PLTreeNode(NodeType.NOT, child1.child2, null);
	//			PLTreeNode temp = new PLTreeNode(child1.child1);
	//			PLTreeNode temp2 = new PLTreeNode(child1.child2);
				PLTreeNode temp = (child1.child1);
				PLTreeNode temp2 = (child1.child2);
				child1.type = NodeType.NOT;
//				child2.type = NodeType.NOT;
				child1.child1 = temp;
//				child2.child1 = temp2;
				child1.child2 = null;
				child2 = not;
				child1.pushNotDown();
				child2.pushNotDown();
					
			}	
				
		}
//		if (type.getArity() == 1)
//		{
//			child1.pushNotDown();
//		}
//			
		if  (type.getArity() == 2)
		{
			child1.pushNotDown();
			child2.pushNotDown();
		}

	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#pushOrBelowAnd()
	 */
	@Override
	public void pushOrBelowAnd()
	{
		// WRITE YOUR CODE HERE	
			
			if (type.getArity() == 1)
			{
				child1.pushOrBelowAnd();
			}
			if (type.getArity() == 2)
			{
				child1.pushOrBelowAnd();
				child2.pushOrBelowAnd();
			}
			
			
//			logger.debug("testpushOrBelowAnd: " + toStringInfix());

			
			if (type.equals(NodeType.OR) && child1.type.equals(NodeType.AND)) // if the currentNode is an OR and the first child is an AND
			{
				PLTreeNode temp = new PLTreeNode(child1.child2); //Q
				PLTreeNode temp2 = new PLTreeNode(child2); //R
				PLTreeNode temp3 = new PLTreeNode(child2); //R
				type = NodeType.AND;
				child1.type = NodeType.OR;
				child2.type = NodeType.OR;
				child1.child2 = temp2; //R
				child2.child1 = temp; // Q
				child2.child2 = temp3; // R
				
				child1.pushOrBelowAnd();
				child2.pushOrBelowAnd();
			}
			
			if (type.equals(NodeType.OR) && child2.type.equals(NodeType.AND)) // if the currentNode is an OR and the second child is an AND
			{
				PLTreeNode temp = new PLTreeNode(child2.child1); //Q
				PLTreeNode temp2 = new PLTreeNode(child1); //P
				PLTreeNode temp3 = new PLTreeNode(child1); // P
				type = NodeType.AND;
				child1.type = NodeType.OR;
				child2.type = NodeType.OR;
				child2.child1 = temp2; // P
				child1.child1 = temp3; // P
				child1.child2 = temp; // Q
				
				child1.pushOrBelowAnd();
				child2.pushOrBelowAnd();

			}
			
			if ((type.equals(NodeType.OR)) && (!child1.type.equals(NodeType.AND)) && (!child2.type.equals(NodeType.AND)))
			{
				child1.pushOrBelowAnd();
				child2.pushOrBelowAnd();
			}
				
		return;
	}

	/* (non-Javadoc)
	 * @see dsa_assignment3.PLTreeNodeInterface#makeAndOrRightDeep()
	 */
	@Override
	public void makeAndOrRightDeep()
	{
		int arity = type.getArity();
		if (type != null)
		{
			
			if (type == NodeType.AND && child1.type == NodeType.AND)
			{
				PLTreeNode temp = new PLTreeNode(child1.child1);
				PLTreeNode temp2 = new PLTreeNode(child1.child2);
				PLTreeNode temp3 = new PLTreeNode(child2);
				child1 = temp;
				child2.type = NodeType.AND;
				child2.child1 = temp2;
				child2.child2 = temp3;
				
			}
			
			if (type == NodeType.OR && child1.type == NodeType.OR)
			{
				PLTreeNode temp = new PLTreeNode(child1.child1);
				PLTreeNode temp2 = new PLTreeNode(child1.child2);
				PLTreeNode temp3 = new PLTreeNode(child2);
				child1 = temp;
				child2.type = NodeType.OR;
				child2.child1 = temp2;
				child2.child2 = temp3;
			}
			
			if (type.getArity() == 1)
			{
				child1.makeAndOrRightDeep();
			}
			if (type.getArity() == 2)
			{
				child1.makeAndOrRightDeep();
				child2.makeAndOrRightDeep();
			}
			
		}
		return;
	}

}
