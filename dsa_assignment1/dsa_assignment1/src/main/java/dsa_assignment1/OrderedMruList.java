package dsa_assignment1;

public class OrderedMruList<E extends Comparable<E>> implements OrderedMruListInterface<E>
{
	MLNodeInterface<E>	headOrdered	= new MLNode<E>(null); //list 1
	MLNodeInterface<E>	headMRU		= new MLNode<E>(null); //list 2

	public OrderedMruList()
	{
	}
	
	public boolean isEmptyOrdered()
	{
		/* WRITE THIS CODE */
		return headOrdered.getNext1() == headOrdered;
	}
 
	public boolean isEmptyMru()
	{
		/* WRITE THIS CODE */
		return headMRU.getNext2() == headMRU;
	}

	public OrderedMruListInterface<E> touch(MLNodeInterface<E> target)
	{
		/* WRITE THIS CODE */
		target.remove2();
		target.addAfter2(headMRU);
		return this;
	}
	
	public MLNodeInterface<E> getFirstMru()
	{
		/* WRITE THIS CODE */
		if (isEmptyMru())
		{
			return null;
		}
		return headMRU.getNext2();
	
	}
	
	public MLNodeInterface<E> getFirstOrdered()
	{
		/* WRITE THIS CODE */
		if (isEmptyOrdered())
		{
			return null;
		}
		return headOrdered.getNext1();
	}
	
	public MLNodeInterface<E> getNextOrdered(MLNodeInterface<E> current)
	{
		/* WRITE THIS CODE */
		if (current.getNext1() == headOrdered)
		{
			return null;
		}
		else 
		{
			return current.getNext1();
		}

	}

	public MLNodeInterface<E> getNextMru(MLNodeInterface<E> current)
	{
		/* WRITE THIS CODE */
		if (current.getNext2() == headMRU)
		{
			return null;
		}
		else 
		{
			return current.getNext2();
		}
	}

	public OrderedMruListInterface<E> remove(MLNodeInterface<E> target)
	{
		/* WRITE THIS CODE */
		target.remove1();
		target.remove2();
		return this;
	
	}
	
	public OrderedMruListInterface<E> add(E element)
	{
		/* WRITE THIS CODE */
		//MLNodeInterface<E> headOrdered = new MLNode<E>(element);
		MLNodeInterface<E> newNode = new MLNode<E>(element);
		MLNodeInterface<E> nextOrdered = headOrdered.getNext1();
		while (nextOrdered != headOrdered && element.compareTo(nextOrdered.getElement()) > 0)
		{
			nextOrdered = nextOrdered.getNext1();
		}
		newNode.addBefore1(nextOrdered);
		newNode.addAfter2(headMRU);
		return this;
	}
}
