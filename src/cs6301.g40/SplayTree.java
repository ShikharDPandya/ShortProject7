/** Starter code for Splay Tree
 */
package cs6301.g40;

/*
 * Group members:
Mukesh Kumar(mxk170430)
Shikhar Pandya (sdp170030)
Arijeet Roy (axr165030)
*/

import java.util.Comparator;
import java.util.Scanner;

import cs6301.g40.RedBlackTree.Entry;

public class SplayTree<T extends Comparable<? super T>> extends BST<T> {
	Entry<T> nullNode;
	Entry<T> root;
	int size;

	static class Entry<T> extends BST.Entry<T> {
		Entry<T> parent;

		Entry(T x, Entry<T> left, Entry<T> right, Entry<T> parent) {
			super(x, left, right);
			this.parent = parent;
		}
	}

	SplayTree() {
		super();
		nullNode = new Entry<T>(null, nullNode, nullNode, nullNode);
		root = nullNode;
	}

	// add a node to the splay tree and call the splay function
	public boolean add(T x) {
		Entry<T> temp = root;
		boolean isAdded = false;
		Entry<T> node = new Entry<T>(x, nullNode, nullNode, nullNode);
		if (root == nullNode) {
			root = node;
			size++;
			isAdded = true;
		} else {
			while (temp != nullNode) {
				if (x.compareTo(temp.element) < 0) {
					if (temp.left == nullNode) {
						temp.left = node;
						node.parent = temp;
						size++;
						break;
					} else {
						temp = (Entry<T>) temp.left;
					}
					isAdded = true;
				} else if (x.compareTo(temp.element) > 0) {
					if (temp.right == nullNode) {
						temp.right = node;
						node.parent = temp;
						size++;
						break;
					} else {
						temp = (Entry<T>) temp.right;
					}
					isAdded = true;
				} else {
					temp.element = x;
					isAdded = false;
					break;
				}
			}
			performSplay(node);
		}
		return isAdded;
	}

	// perform splay in the node
	private void performSplay(Entry<T> node) {
		// TODO Auto-generated method stub
		while (node.parent != nullNode) {
			Entry<T> par = node.parent;
			Entry<T> grandPar = par.parent;
			// Zig rotation
			if (grandPar == nullNode) {
				if (node == par.left)
					rightZig(node, par);
				else
					leftZig(node, par);
			}
			// zig zig and zig zag rotation
			else {
				if (node == par.left) {
					if (par == grandPar.left) {
						rightZig(par, grandPar);
						rightZig(node, par);
					} else {
						rightZig(node, node.parent);
						leftZig(node, node.parent);
					}
				} else {
					if (par == grandPar.left) {
						leftZig(node, node.parent);
						rightZig(node, node.parent);
					} else {
						leftZig(par, grandPar);
						leftZig(node, par);
					}
				}
			}
		}
		root = node;
	}

	// search for a node in the tree and return if exists
	private Entry<T> find(T x, Entry<T> node) {
		if (root == nullNode) {
			return nullNode;
		}

		if (x.compareTo(node.element) < 0) {
			if (node.left != nullNode) {
				return find(x, (Entry<T>) node.left);
			}
		} else if (x.compareTo(node.element) > 0) {
			if (node.right != nullNode) {
				return find(x, (Entry<T>) node.right);
			}
		} else if (x.compareTo(node.element) == 0) {
			return node;
		}
		return nullNode;
	}

	// remove a node from the tree and perform the splay function
	public T remove(T x) {
		Entry<T> node = find(x, root);
		if (node == nullNode)
			return null;
		performSplay(node);
		Entry<T> temp;
		Entry<T> inOrderSucc = node;

		if (node.left == nullNode) {
			temp = (Entry<T>) node.right;
			replace(node, (Entry<T>) node.right);
		} else if (node.right == nullNode) {
			temp = (Entry<T>) node.left;
			replace(node, (Entry<T>) node.left);
		} else {
			inOrderSucc = inOrderSuccessor((Entry<T>) node.right);
			temp = (Entry<T>) inOrderSucc.right;
			if (inOrderSucc.parent == node)
				temp.parent = inOrderSucc;
			else {
				replace(inOrderSucc, (Entry<T>) inOrderSucc.right);
				inOrderSucc.right = node.right;
				((Entry<T>) inOrderSucc.right).parent = inOrderSucc;
			}
			replace(node, inOrderSucc);
			inOrderSucc.left = node.left;
			((Entry<T>) inOrderSucc.left).parent = inOrderSucc;
		}
		size--;
		return x;

	}

	// function to find the inorder successor of a node
	private Entry<T> inOrderSuccessor(Entry<T> node) {
		while (node.left != nullNode) {
			node = (Entry<T>) node.left;
		}
		return node;
	}

	// replace a nnode with another
	public void replace(Entry<T> target, Entry<T> with) {
		if (target.parent == nullNode) {
			root = with;
		} else if (target == target.parent.left) {
			target.parent.left = with;
		} else
			target.parent.right = with;
		with.parent = target.parent;
	}

	private void leftZig(Entry<T> node, Entry<T> par) {
		// TODO Auto-generated method stub
		if (par.parent != nullNode) {
			if (par == par.parent.left)
				par.parent.left = node;
			else
				par.parent.right = node;
		}
		if (node.left != nullNode)
			((Entry<T>) node.left).parent = par;
		node.parent = par.parent;
		par.parent = node;
		par.right = node.left;
		node.left = par;
	}

	private void rightZig(Entry<T> node, Entry<T> par) {
		// TODO Auto-generated method stub
		// if grandparent exists
		if (par.parent != nullNode) {
			if (par == par.parent.left)
				par.parent.left = node;
			else
				par.parent.right = node;
		}
		if (node.right != nullNode)
			((Entry<T>) node.right).parent = par;

		node.parent = par.parent;
		par.parent = node;
		par.left = node.right;
		node.right = par;
	}

	public static void main(String[] args) {
		BST<Integer> t = new SplayTree<>();
		Scanner in = new Scanner(System.in);
		while (in.hasNext()) {
			int x = in.nextInt();
			if (x > 0) {
				System.out.print("Add " + x + " : ");
				t.add(x);
				t.printTree();
			} else if (x < 0) {
				System.out.print("Remove " + x + " : ");
				t.remove(-x);
				t.printTree();
			}
		}
		in.close();
	}

	public void printTree() {
		System.out.print("[" + size + "]");
		printTree(root);
		System.out.println();
	}

	// Inorder traversal of tree
	void printTree(Entry<T> node) {
		if (node != nullNode) {
			printTree((Entry<T>) node.left);
			if (node.element != null) {
				System.out.print(" " + node.element);
			}
			printTree((Entry<T>) node.right);
		}
	}
}
