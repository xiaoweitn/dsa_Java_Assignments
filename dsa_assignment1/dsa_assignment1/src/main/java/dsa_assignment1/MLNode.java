package dsa_assignment1;

public class MLNode<E> implements MLNodeInterface<E>
{
	private E			item;
	private MLNodeInterface<E>	next1;
	private MLNodeInterface<E>	prev1;
	private MLNodeInterface<E>	next2;
	private MLNodeInterface<E>	prev2;

	MLNode(E element)
	{
		this.item = element;
		this.next1 = this.prev1 = this.next2 = this.prev2 = this;
	}

	public MLNodeInterface<E> remove1()
	{
		/* WRITE THIS CODE */
		MLNodeInterface<E> x = this.getPrev1();
		MLNodeInterface<E> y = this.getNext1();
		x.setNext1(y);
		y.setPrev1(x);
		next1 = prev1 = this;
		
		return this;
	}

	public MLNodeInterface<E> remove2()
	{
		/* WRITE THIS CODE */
		MLNodeInterface<E> x = this.getPrev2();
		MLNodeInterface<E> y = this.getNext2();
		x.setNext2(y);
		y.setPrev2(x);
		next2 = prev2 = this;
		
		return this;
	}

	public MLNodeInterface<E> addAfter1(MLNodeInterface<E> target)
	{
		/* WRITE THIS CODE */
		this.remove1();
		MLNodeInterface<E> x = target.getNext1();
		target.setNext1(this);
		this.setPrev1(target);
		this.setNext1(x);
		x.setPrev1(this);
		
		return this;
	}

	public MLNodeInterface<E> addAfter2(MLNodeInterface<E> target)
	{
		/* WRITE THIS CODE */
		this.remove2();
		MLNodeInterface<E> x = target.getNext2();
		target.setNext2(this);
		this.setPrev2(target);
		this.setNext2(x);
		x.setPrev2(this);
		
		return this;

	}

	public MLNodeInterface<E> addBefore1(MLNodeInterface<E> target)
	{
		/* WRITE THIS CODE */
		this.remove1();
		MLNodeInterface<E> x = target.getPrev1();
		target.setPrev1(this);
		this.setNext1(target);
		this.setPrev1(x);
		x.setNext1(this);
		
		return this;

	}

	public MLNodeInterface<E> addBefore2(MLNodeInterface<E> target)
	{
		/* WRITE THIS CODE */
		this.remove2();
		MLNodeInterface<E> x = target.getPrev2();
		target.setPrev2(this);
		this.setNext2(target);
		this.setPrev2(x);
		x.setNext2(this);
		
		return this;

	}
	
	public E getElement()
	{
		return item;
	}

	public MLNodeInterface<E> getNext1()
	{
		return next1;
	}

	public MLNodeInterface<E> getPrev1()
	{
		return prev1;
	}

	public MLNodeInterface<E> getNext2()
	{
		return next2;
	}

	public MLNodeInterface<E> getPrev2()
	{
		return prev2;
	}

	public void setNext1(MLNodeInterface<E> next1)
	{
		this.next1 = next1;
	}

	public void setPrev1(MLNodeInterface<E> prev1)
	{
		this.prev1 = prev1;
	}

	public void setNext2(MLNodeInterface<E> next2)
	{
		this.next2 = next2;
	}

	public void setPrev2(MLNodeInterface<E> prev2)
	{
		this.prev2 = prev2;
	}
}
