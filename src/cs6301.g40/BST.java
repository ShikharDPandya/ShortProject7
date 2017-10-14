/** @author 
 *  Binary search tree (starter code)
 **/

package cs6301.g40;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

public class BST<T extends Comparable<? super T>> implements Iterable<T> {
    static class Entry<T> {
        T element;
        Entry<T> left, right;

        Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
	    this.left = left;
	    this.right = right;
        }
    }
    
    Entry<T> root;
    int size;

    public BST() {
	root = null;
	size = 0;
    }


    /** TO DO: Is x contained in tree?
     */
    public boolean contains(T x) {
	    Stack<Entry<T>> stack = new Stack<>();
	    Entry<T> t = find(x,stack);
    	return (t!=null && (t.element.compareTo(x)==0));
    }
    
    Entry<T> find(T x, Stack<Entry<T>> stack){
    	stack.push(null);
    	return find(root,x,stack);
	}
	
	Entry<T> find(Entry<T> start, T x, Stack<Entry<T>> stack){
    	if(start == null || start.element == x) return start;
    	while(true){
    		if(x.compareTo(start.element)<0){
    			if(start.left ==null) break;
    			else{
    				stack.push(start);
    				start = start.left;
				}
    			
			}
			else if(x.compareTo(start.element)==0){break;}
			else{
				if(start.right ==null) break;
				else{
					stack.push(start);
					start = start.right;
				}
				
			}
		}
		
		return start;
	}

    /** TO DO: Is there an element that is equal to x in the tree?
     *  Element in tree that is equal to x is returned, null otherwise.
     */
    public T get(T x) {
		Stack<Entry<T>> stack = new Stack<>();
		Entry<T> t = find(x,stack);
		if(t.element.compareTo(x)==0) return t.element;
		else return null;
    }

    /** TO DO: Add x to tree. 
     *  If tree contains a node with same key, replace element by x.
     *  Returns true if x is a new element added to tree.
     */
    public boolean add(T x) {
	
    	if(root==null) {
    		root = new Entry<T>(x,null,null);
    		this.size = 1;
    		return true;
    	}
	
		Stack<Entry<T>> stack = new Stack<>();
		Entry<T> t = find(x,stack);
		if(x.compareTo(t.element) == 0){
			t.element = x;
			return false;
		}
    	else if(x.compareTo(t.element)<0){
			t.left = new Entry<T>(x,null,null);
			this.size++;
			return true;
		}
		else {
			t.right = new Entry<T>(x,null,null);
			this.size++;
			return true;
		}
    
    }

    /** TO DO: Remove x from tree. 
     *  Return x if found, otherwise return null
     */
    public T remove(T x) {
    	if(root==null) return null;
    	Stack<Entry<T>> stack = new Stack<>();
		Entry<T> t = find(x,stack);
		if(t.element.compareTo(x)!=0) return null;
		T result = t.element;
		if(t.left == null || t.right == null){
			bypass(t,stack);
		}
		else{
			stack.push(t);
			Stack<Entry<T>> stack1 = new Stack<>();
			Entry<T> minRight = find(t.right,t.element,stack1);
			t.element = minRight.element;
			bypass(minRight,stack1);
		}
		this.size--;
		return result;
	
    }

    public void bypass(Entry<T> t, Stack<Entry<T>> stack){
    	Entry<T> pt = stack.peek();
		Entry<T> c = t.left == null ? t.right : t.left;
		
		if(pt==null) root = c;
		else if(pt.left == t) pt.left = c;
		else pt.right = c;
	}
    /** TO DO: Iterate elements in sorted order of keys
     */
    public Iterator<T> iterator() {
	
    	T [] arr = (T[]) toArray();
    	return new ArrayIterator<T>(arr);
    	
    }

    public static void main(String[] args) {
	BST<Integer> t = new BST<>();
    /*   Scanner in = new Scanner(System.in);
        while(in.hasNext()) {
            int x = in.nextInt();
            if(x > 0) {
                System.out.print("Add " + x + " : ");
                t.add(x);
                t.printTree();
            } else if(x < 0) {
                System.out.print("Remove " + x + " : ");
                t.remove(-x);
                t.printTree();
            } else {
                Comparable[] arr = t.toArray();
                System.out.print("Final: ");
                for(int i=0; i<t.size; i++) {
                    System.out.print(arr[i] + " ");
                }
                System.out.println();
                return;
            }
        }   */
		t.add(15);
		t.add(9);
		t.add(25);
		t.add(6);
		t.add(12);
		t.add(20);
		t.add(27);
		t.add(5);
		t.add(7);
		t.add(11);
		t.add(13);
		t.add(18);
		t.add(22);
		t.add(26);
		t.add(29);
		t.add(8);
		t.add(10);
		t.add(14);
		t.add(17);
		t.add(23);
		t.add(28);
		t.add(30);
		
		t.toArray();
    }


    // TODO: Create an array with the elements using in-order traversal of tree
    public Comparable[] toArray() {
	Comparable[] arr = new Comparable[size];
	/* write code to place elements in array here */
	Stack<Entry<T>> stack = new Stack<>();
	Entry<T> start = root;
	stack.push(start);
	int i =0;
		Entry<T> removed = null;
	while(!stack.isEmpty()){
		
		start = stack.peek();
		while(start.left !=null){
			stack.push(start.left);
			start = start.left;
		}
		start =  stack.peek();
		
		
		if(start.right !=null){
			removed = stack.pop();
			arr[i++] = (Comparable) removed.element;
			stack.push(removed.right);
		}
	   else	if(start.right==null || start.right==removed ){
			while(!stack.isEmpty() && (removed==stack.peek().right ||  stack.peek().right==null)){
				arr[i++] = (Comparable) stack.peek().element;
				removed = stack.pop();
			}
			
		    if(!stack.isEmpty() && stack.peek().right!=null){
		    	arr[i++] =  (Comparable) stack.peek().element;
		    	removed = stack.pop();
		    	stack.push(removed.right);
			 }
		    
		}
	}
	
	return arr;
    }

    public void printTree() {
	System.out.print("[" + size + "]");
	printTree(root);
	System.out.println();
    }

    // Inorder traversal of tree
    void printTree(Entry<T> node) {
	if(node != null) {
	    printTree(node.left);
	    System.out.print(" " + node.element);
	    printTree(node.right);
	}
    }

}
/*
Sample input:
1 3 5 7 9 2 4 6 8 10 -3 -6 -3 0

Output:
Add 1 : [1] 1
Add 3 : [2] 1 3
Add 5 : [3] 1 3 5
Add 7 : [4] 1 3 5 7
Add 9 : [5] 1 3 5 7 9
Add 2 : [6] 1 2 3 5 7 9
Add 4 : [7] 1 2 3 4 5 7 9
Add 6 : [8] 1 2 3 4 5 6 7 9
Add 8 : [9] 1 2 3 4 5 6 7 8 9
Add 10 : [10] 1 2 3 4 5 6 7 8 9 10
Remove -3 : [9] 1 2 4 5 6 7 8 9 10
Remove -6 : [8] 1 2 4 5 7 8 9 10
Remove -3 : [8] 1 2 4 5 7 8 9 10
Final: 1 2 4 5 7 8 9 10 

*/
