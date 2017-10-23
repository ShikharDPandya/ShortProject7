

/** @Shikhar Pandya, Mukesh Kumar, Arijeet Roy
 *  Binary search tree map (starter code)
 *  Implement this class using one of the BST implementations: BST, AVLTree, RedBlackTree, or, SplayTree.
 *  Do not use TreeMap or any of Java's maps.
 **/

package cs6301.g40;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

public class BSTMap<K extends Comparable<? super K>, V> implements Iterable<K>
{
    Stack<Entry<K,V>> ancestors;
    Entry<K,V> root;
    int size;

    /**
     *
     * @param <K> Key for the Map
     * @param <V> Value of the Map
     *           Left and Right are for storing the keys in the form of BST
     */
    static class Entry<K,V>
    {
        K key;
        V value;
        Entry<K,V> left;
        Entry<K,V> right;
        Entry(K x, V y, Entry<K,V> left, Entry<K,V> right)
        {
            key = x;
            value = y;
            this.left = left;
            this.right = right;
        }
    }

    BSTMap()
    {
        ancestors = new Stack<>();
        root = null;
        size = 0;
    }

    /**
     * find function same as that in BST. Tries to find the key in the BST
     * @param x
     * @return
     */
    Entry<K,V> find(K x)
    {
        ancestors.clear();
        ancestors.push(null);
        return find(root,x);
    }

    /**
     * find Function when the root of a subtree is given
     * @param t
     * @param x
     * @return
     */
    Entry<K,V> find(Entry<K,V> t,K x)
    {
        if(t == null || x.compareTo(t.key)==0)
            return t;
        while(true)
        {
            if( x.compareTo(t.key) < 0 )
            {
                if(t.left == null)
                    break;
                else
                {
                    ancestors.push(t);
                    t = t.left;
                }
            }
            else if(x.compareTo(t.key)==0)
                break;
            else
            {
                if(t.right == null)
                    break;
                else
                {
                    ancestors.push(t);
                    t=t.right;
                }
            }
        }
        return t;
    }

    /**
     * Function to return key from BST
     * @param key
     * @return
     */
    public V get(K key)
    {
        Entry<K,V> t = find(key);
        if(key.compareTo(t.key) == 0)
            return t.value;
        else
            return null;
    }

    /**
     * Function to put key value pair into BST
     * @param key
     * @param value
     * @return
     */
    public boolean put(K key, V value)
    {
        if(root == null)
        {
            root = new Entry<K,V>(key,value,null,null);
            size++;
            return true;
        }
        Entry<K,V> t = find(key);
        if(key.compareTo(t.key)== 0 )
        {
            t.key = key;
            t.value = value;
            return false;
        }
        else if( key.compareTo(t.key) < 0 )
        {
            t.left = new Entry<>(key,value,null,null);
        }
        else if(key.compareTo(t.key) > 0)
        {
            t.right = new Entry<K,V>(key,value,null,null);
        }
        size++;
        return true;
    }

    // Iterate over the keys stored in the map, in order
    public Iterator<K> iterator()
    {
        return new BSTMapIterator<>(root);
    }

    /**
     * Class BSTMapIterator to implement iterator
     * @param <K> Key
     * @param <V> Value
     */
    public class BSTMapIterator<K,V> implements Iterator<K>
    {
        Entry<K,V> next; // stores next Key to be iterated upon
        Stack<Entry<K,V>> st; // stack to perform inorder traversal
        int itSize; // size of the tree
        BSTMapIterator(Entry<K,V> node)
        {
            st = new Stack<>();
            next=node;
            itSize=1;
            while(next.left!=null) // iterate to the leftmost element in BST
            {
                st.push(next);
                next=next.left; // points to the leftmost element in the tree
            }
        }
        public boolean hasNext()
        {
            return itSize<=size;
        }

        public K next()
        {
            K retITem=next.key; // returns key which points to the next element in "inOrder" notation
            itSize++;
            findNext(next); // shifts next to the appropriate element
            return retITem;
        }

        void findNext(Entry<K,V> curr)
        {
            if(curr.right == null && curr.left == null) // if leaf element
            {
                if(!hasNext())
                    return;
                next = st.pop();
            }
            else if(curr.right != null)   // if not leaf element then go to the leftmost element of right subtree
            {
                Entry<K,V> temp = curr.right;
                while(temp.left!=null)
                {
                    st.push(temp);
                    temp=temp.left;
                }
                next=temp;
            }
            else if(curr.right == null && curr.left!=null)   // if left subtree iterated upon and no right child
            {
                next=st.pop();
            }
            else    // if there is a right element and no left element
            {
                next = curr.right;
            }
        }
    }

    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        BSTMap<Integer,String> Bmap=new BSTMap<Integer, String>();
        Bmap.put(8,"Life is good 8");
        Bmap.put(4,"Life is bad 4");
        Bmap.put(12,"Life is good 12");
        Bmap.put(2,"Life is good 2");
        Bmap.put(6,"Life is bad 6");
        Bmap.put(10,"Life is good 10");
        Bmap.put(14,"Life is good 14");
        Bmap.put(1,"Life is bad 1");
        Bmap.put(3,"Life is bad 3");
        Bmap.put(5,"Life is bad 5");
        Bmap.put(7,"Life is bad 7");
        Bmap.put(9,"Life is bad 9");
        Bmap.put(11,"Life is bad 11");
        Bmap.put(13,"Life is bad 13");
        Bmap.put(15,"Life is bad 15");

        for (Integer i:Bmap)
        {
            System.out.println(Bmap.get(i));
        }
    }
}
