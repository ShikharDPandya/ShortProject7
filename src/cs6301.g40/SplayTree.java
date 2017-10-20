/** Starter code for Splay Tree
 */
package cs6301.g40;

import java.util.Comparator;
import java.util.Scanner;



public class SplayTree<T extends Comparable<? super T>> extends BST<T> {
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
	}

	public boolean add(T x) {
		Entry<T> temp = root;
		boolean isAdded = false;
		if (root == null) {
			Entry<T> node = new Entry<T>(x, null, null, null);
			root = node;
			size++;
			isAdded = true;
		} else {
			Entry<T> node = new Entry<T>(x, null, null, null);
			while (true) {
				if (x.compareTo(temp.element) < 0) {
					if (temp.left == null) {
						temp.left = node;
						node.parent = temp;
						size++;
						break;
					} else {
						temp = (Entry<T>) temp.left;
					}
					isAdded = true;
				} else if (x.compareTo(temp.element) > 0) {
					if (temp.right == null) {
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

	private void performSplay(Entry<T> node) {
		// TODO Auto-generated method stub
		while (node.parent != null) {
			Entry<T> par = node.parent;
			Entry<T> grandPar = par.parent;
			// Zig rotation
			if (grandPar == null) {
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
						rightZig(par, grandPar);
						rightZig(node, par);
					}
				}
			}
		}
		root = node;
	}

	// search for a node in the tree and return if exists
	private Entry<T> find(T x, Entry<T> node) {
		if (root == null) {
			return null;
		}

		if (x.compareTo(node.element) < 0) {
			if (node.left != null) {
				return find(x, (Entry<T>) node.left);
			}
		} else if (x.compareTo(node.element) > 0) {
			if (node.right != null) {
				return find(x, (Entry<T>) node.right);
			}
		} else if (x.compareTo(node.element) == 0) {
			return node;
		}
		return null;
	}

	public T remove(T x) {
		Entry<T> z = find(x, root);
		if (z == null)
			return null;
		Entry<T> temp;
		Entry<T> y = z;

		if (z.left == null) {
			temp = (Entry<T>) z.right;
			replace(z, (Entry<T>) z.right);
		} else if (z.right == null) {
			temp = (Entry<T>) z.left;
			replace(z, (Entry<T>) z.left);
		} else {
			y = inOrderSuccessor((Entry<T>) z.right);
			temp = (Entry<T>) y.right;
			if (y.parent == z)
				temp.parent = y;
			else {
				replace(y, (Entry<T>) y.right);
				y.right = z.right;
				((Entry<T>) y.right).parent = y;
			}
			replace(z, y);
			y.left = z.left;
			((Entry<T>) y.left).parent = y;
		}
		size--;
		return x;

	}

	// function to find the inorder successor of a node
	private Entry<T> inOrderSuccessor(Entry<T> node) {
		while (node.left != null) {
			node = (Entry<T>) node.left;
		}
		return node;
	}

	// replace a nnode with another
	public void replace(Entry<T> target, Entry<T> with) {
		if (target.parent == null) {
			root = with;
		} else if (target == target.parent.left) {
			target.parent.left = with;
		} else
			target.parent.right = with;
		with.parent = target.parent;
	}

	private void leftZig(Entry<T> node, Entry<T> par) {
		// TODO Auto-generated method stub
		if (par.parent != null) {
			if (par == par.parent.left)
				par.parent.left = node;
			else
				par.parent.right = node;
		}
		if (node.left != null)
			((Entry<T>) node.left).parent = par;
		node.parent = par.parent;
		par.parent = node;
		par.right = node.left;
		node.left = par;
	}

	private void rightZig(Entry<T> node, Entry<T> par) {
		// TODO Auto-generated method stub
		// if grandparent exists
		if (par.parent != null) {
			if (par == par.parent.left)
				par.parent.left = node;
			else
				par.parent.right = node;
		}
		if (node.right != null)
			((Entry<T>) node.right).parent = par;

		node.parent = par.parent;
		par.parent = node;
		par.left = node.right;
		node.right = par;
	}

	public static void main(String[] args) {
		SplayTree<Integer> t = new SplayTree<>();
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
		if (node != null) {
			printTree((Entry<T>) node.left);
			if (node.element != null) {
				System.out.print(" " + node.element);
			}
			printTree((Entry<T>) node.right);
		}
	}
}
