
/** Starter code for AVL Tree
 */
package cs6301.g40;
/*
 * Group members:
Mukesh Kumar(mxk170430)
Shikhar Pandya (sdp170030)
Arijeet Roy (axr165030)
*/
import java.util.Comparator;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Iterator;

public class AVLTree<T extends Comparable<? super T>> extends BST<T> {
    
    static class Entry<T> extends BST.Entry<T> {
        int height;
        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            height = 0;
        }
        int getHeight(){
            return this.height;
        }
        void setHeight(int height){
            this.height = height;
        }
    }

    AVLTree() {
	super();
	
    }
    
    public void leftRotate(Entry about,Deque<Object> stack){
    //    Deque<Object> stack = new ArrayDeque<>();
   
   //     find(root,(T)about.getElement(),stack);
        Entry<T> parent = (Entry<T>) stack.pop();
        Entry<T> temp1 = (Entry<T>) about.getLeft();
        about.setLeft(parent);
        parent.setRight(temp1);
        Entry<T> grandParent = null;
        if(!stack.isEmpty())
         grandParent = (AVLTree.Entry<T>)stack.pop();
      //  if(grandParent.left.getElement().compareTo(parent.getElement())==0)
        if(grandParent!=null && grandParent.getLeft()==parent){
            grandParent.setLeft(about);
            stack.push(grandParent);
        }
            
        else if (grandParent!=null && grandParent.getRight()==parent){
            grandParent.setRight(about);
            stack.push(grandParent);
        }
        else
            root = about;
        
        stack.push(about);
        updateHeight(parent);
        for (Object t : stack
                ) {
            updateHeight((AVLTree.Entry<T>) t);
        }
    }
    
    public void rightRotate(Entry about,Deque<Object> stack){
       // Deque<Object> stack = new ArrayDeque<>();
       // find(root,(T)about.getElement(),stack);
        Entry<T> parent = (AVLTree.Entry<T>) stack.pop();
        Entry<T> temp1 = (AVLTree.Entry<T>) about.getRight();
        about.setRight(parent);
        parent.setLeft(temp1);
        
        Entry<T> grandParent = null;
        if(!stack.isEmpty())
            grandParent = (AVLTree.Entry<T>)stack.pop();
            if(grandParent!=null && grandParent.getLeft()==parent){
            grandParent.setLeft(about);
            stack.push(grandParent);
        }
    
        else if (grandParent!=null && grandParent.getRight()==parent){
            grandParent.setRight(about);
            stack.push(grandParent);
        }
        else
            root = about;
    
        stack.push(about);
        updateHeight(parent);
        for (Object t : stack
             ) {
            updateHeight((AVLTree.Entry<T>) t);
        }
    }
    
    public void updateHeight(Entry<T> node){
        Entry<T> leftTree = (AVLTree.Entry<T>) node.getLeft();
        int leftHeight;
        int rightHeight;
        if(leftTree==null)
             leftHeight = -1;
        else leftHeight = leftTree.getHeight();
        Entry<T> rightTree = (AVLTree.Entry<T>) node.getRight();
        if(rightTree==null)
            rightHeight = -1;
        else rightHeight = rightTree.getHeight();
        int newHeight;
        if(leftHeight>rightHeight)
            newHeight = leftHeight+1;
        else
            newHeight = rightHeight+1;
        
            node.setHeight(newHeight);
    }
    
    public void balanceTree(T x, Deque<Object> stack){
      //  Iterator<Object> it = stack.iterator();
        while(!stack.isEmpty()) {
            Entry<T> node = (AVLTree.Entry<T>) stack.pop();
            Entry<T> lChild = (AVLTree.Entry<T>) node.getLeft();
            Entry<T> rChild = (AVLTree.Entry<T>) node.getRight();
            int lht =-1;
            int rht =-1;
            
             if(lChild!=null){  lht = lChild.getHeight();}
             if(rChild!=null){  rht = rChild.getHeight();}
             
             
            
            if ((lht - rht) > 1) {
                Entry<T> llChild = (AVLTree.Entry<T>) lChild.getLeft();
                Entry<T> lrChild = (AVLTree.Entry<T>) lChild.getRight();
         
                int llht = -1;
                int lrht = -1;
                if(llChild!=null) llht=llChild.getHeight();
                if(lrChild!=null) lrht=lrChild.getHeight();
                
                if((llht - lrht) > 0){
                    stack.push(node);
                    rightRotate(lChild,stack);
                }
                else if ((lrht - llht)>0){
                    stack.push(node);
                    stack.push(lChild);
                    leftRotate(lrChild,stack);
                   // Deque<Object> st1 = new ArrayDeque<>();
                   // find(root,lrChild.getElement(),st1);
                    balanceTree(lrChild.getElement(),stack);
                }
                break;
            }
            if ((rht - lht) > 1) {
                Entry<T> rlChild = (AVLTree.Entry<T>) rChild.getLeft();
                Entry<T> rrChild = (AVLTree.Entry<T>) rChild.getRight();
                   
                int rlht = -1;
                int rrht = -1;
                if(rlChild!=null) rlht=rlChild.getHeight();
                if(rrChild!=null) rrht=rrChild.getHeight();
                if((rrht - rlht) > 0){
                    stack.push(node);
                    leftRotate(rChild,stack);
                }
                else if ((rlht - rrht)>0){
                    stack.push(node);
                    stack.push(rChild);
                    rightRotate(rlChild,stack);
                   // Deque<Object> st1 = new ArrayDeque<>();
                   // find(root,rlChild.getElement(),st1);
                    balanceTree(rlChild.getElement(),stack);
                }
                break;
            }
        }
    }
    
    public void insert(T x)
    {
        Entry avlNode = new AVLTree.Entry(x,null,null);
        Deque<Object> stack = new ArrayDeque<>();
        
        Entry<T> t = (AVLTree.Entry<T>) find(root,x,stack);
        super.add(avlNode,stack,t);
        if(t!=null)
        stack.push(t);
        
        for (Object entry : stack
                ) {
            updateHeight((AVLTree.Entry<T>) entry);
        }
        balanceTree(x,stack);
    }
    
    
    public T remove(T x){
        T y = super.remove(x);
        Deque<Object> st1 = new ArrayDeque<>();
        Entry<T> t = (AVLTree.Entry<T>) find(root,y,st1);
        balanceTree(y,st1);
        return y;
    }
    
    public static void main(String [] args){
        AVLTree avl = new AVLTree();
        avl.insert(15);
        avl.insert(20);
        avl.insert(17);
        avl.insert(25);
        avl.insert(30);
        avl.insert(10);
        avl.insert(7);
        avl.insert(5);
        avl.insert(3);
        
        avl.remove(25);
        avl.remove(5);
    }
}

