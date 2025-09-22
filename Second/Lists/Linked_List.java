package Lists;
import Nodes.*;

public abstract class Linked_List<T>{
	protected Node<T> head;
	protected Node<T> tail;
	protected int size;

	public Linked_List(){
		this.head = null;
		this.tail = null;
		this.size = 0;
	}

	// Can be append or push
	public abstract void add(T data);

	// Remove by first occurence
	public abstract boolean remove(T data);

	// Find first equal
	public abstract Node<T> find(T data);

	public int get_size(){
		return this.size;
	}

	public boolean is_empty(){
		return this.size == 0;
	}

	// Iterate and apply a visitor lambda
	public void for_each(Visitor<T> visitor){
		Node<T> current = this.head;
		int iter_count = 0;
		while(current != null && iter_count < this.size){
			visitor.visit(current.get_data());
			current = current.get_next();
			iter_count++;
		}
	}

	public Node<T> get_head(){
		return this.head;
	}

	public interface Visitor<T>{
		void visit(T item);
	}
}
