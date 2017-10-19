
/** Starter code for Red-Black Tree
 */
package cs6301.g40;

import java.util.Comparator;
import java.util.Scanner;

import cs6301.g40.BST;
import cs6301.g40.BST.Entry;

public class RedBlackTree<T extends Comparable<? super T>> extends BST<T> {
	// create nullnode with color black;
	Entry<T> nullNode;
	Entry<T> root;
	int size;

	static class Entry<T> extends BST.Entry<T> {
		boolean isRed;
		Entry<T> parent;

		Entry(T x, Entry<T> left, Entry<T> right, Entry<T> parent, boolean isRed) {
			super(x, left, right);
			this.isRed = isRed;
			this.parent = parent;
		}
	}

	//initialize a sentinel node with a black node
	RedBlackTree() {
		super();
		nullNode = new Entry<T>(null, nullNode, nullNode, nullNode, false);
	}

	//function to add a new node and fix the structure of tree maintaining
	//the black height of left and right subtrees
	public boolean add(T x) {
		Entry<T> temp = root;
		boolean isAdded = false;
		if (root == null || root == nullNode) {
			Entry<T> node = new Entry<T>(x, nullNode, nullNode, nullNode, false);
			root = node;
			size++;
			isAdded = true;
		} else {
			Entry<T> node = new Entry<T>(x, nullNode, nullNode, nullNode, true);
			while (true) {
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
			fixStructureAfterInsert(node);
		}
		return isAdded;
	}

	// fix the structure of the tree according to the color of the uncle
	private void fixStructureAfterInsert(Entry<T> node) {
		while (node.parent.isRed) {
			Entry<T> uncle = nullNode;
			if (node.parent == node.parent.parent.left) {
				uncle = (Entry<T>) node.parent.parent.right;
				if (uncle != nullNode && uncle.isRed) {
					node.parent.isRed = false;
					uncle.isRed = false;
					node.parent.parent.isRed = true;
					node = node.parent.parent;
					continue;
				}
				if (node == node.parent.right) {
					node = node.parent;
					rotateLeft(node);
				}
				node.parent.isRed = false;
				node.parent.parent.isRed = true;
				rotateRight(node.parent.parent);
			} else {
				uncle = (Entry<T>) node.parent.parent.left;
				if (uncle != nullNode && uncle.isRed) {
					node.parent.isRed = false;
					uncle.isRed = false;
					node.parent.parent.isRed = true;
					node = node.parent.parent;
					continue;
				}
				if (node == node.parent.left) {
					node = node.parent;
					rotateRight(node);
				}
				node.parent.isRed = false;
				node.parent.parent.isRed = true;
				rotateLeft(node.parent.parent);
			}
		}
		root.isRed = false;
	}

	//rotate left the node to maintain height balance
	void rotateLeft(Entry<T> node) {
		if (node.parent != nullNode) {
			if (node == node.parent.left) {
				node.parent.left = node.right;
			} else {
				node.parent.right = node.right;
			}
			((RedBlackTree.Entry<T>) node.right).parent = node.parent;
			node.parent = (Entry<T>) node.right;
			if (node.right.left != nullNode) {
				((RedBlackTree.Entry<T>) node.right.left).parent = node;
			}
			node.right = node.right.left;
			node.parent.left = node;
		} else {
			Entry<T> right = (Entry<T>) root.right;
			root.right = right.left;
			((RedBlackTree.Entry<T>) right.left).parent = root;
			root.parent = right;
			right.left = root;
			right.parent = nullNode;
			root = right;
		}
	}

	//rotate right once to maintain height balance
	void rotateRight(Entry<T> node) {
		if (node.parent != nullNode) {
			if (node == node.parent.left) {
				node.parent.left = node.left;
			} else {
				node.parent.right = node.left;
			}

			((RedBlackTree.Entry<T>) node.left).parent = node.parent;
			node.parent = (Entry<T>) node.left;
			if (node.left.right != nullNode) {
				((RedBlackTree.Entry<T>) node.left.right).parent = node;
			}
			node.left = node.left.right;
			node.parent.right = node;
		} else {
			Entry<T> left = (Entry<T>) root.left;
			root.left = root.left.right;
			((RedBlackTree.Entry<T>) left.right).parent = root;
			root.parent = left;
			left.right = root;
			left.parent = nullNode;
			root = left;
		}
	}

	//search for a node in the tree and return if exists
	private Entry<T> find(T x, Entry<T> node) {
		if (root == null || root == nullNode) {
			return null;
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
		return null;
	}

	//function to remove a node by finding the inoder successor of a node and then
	//then fixing the tree structure 
	public T remove(T x) {
		Entry<T> z = find(x, root);
		if (z == null)
			return null;
		Entry<T> temp;
		Entry<T> y = z;
		boolean yInitialColor = y.isRed;

		if (z.left == nullNode) {
			temp = (Entry<T>) z.right;
			replace(z, (Entry<T>) z.right);
		} else if (z.right == nullNode) {
			temp = (Entry<T>) z.left;
			replace(z, (Entry<T>) z.left);
		} else {
			y = inOrderSuccessor((Entry<T>) z.right);
			yInitialColor = y.isRed;
			temp = (Entry<T>) y.right;
			if (y.parent == z)
				temp.parent = y;
			else {
				replace(y, (Entry<T>) y.right);
				y.right = z.right;
				((RedBlackTree.Entry<T>) y.right).parent = y;
			}
			replace(z, y);
			y.left = z.left;
			((RedBlackTree.Entry<T>) y.left).parent = y;
			y.isRed = z.isRed;
		}
		if (!yInitialColor)
			fixStructureAfterDelete(temp);
		size--;
		return x;

	}

	//fix the tree structure keeping note of the sibling color
	private void fixStructureAfterDelete(Entry<T> node) {
		while (node != root && !node.isRed) {
			if (node == node.parent.left) {
				Entry<T> w = (Entry<T>) node.parent.right;
				if (w.isRed) {
					w.isRed = false;
					node.parent.isRed = true;
					rotateLeft(node.parent);
					w = (Entry<T>) node.parent.right;
				}
				if (!((RedBlackTree.Entry<T>) w.left).isRed && !((RedBlackTree.Entry<T>) w.right).isRed) {
					w.isRed = true;
					node = node.parent;
					continue;
				} else if (!((RedBlackTree.Entry<T>) w.right).isRed) {
					((RedBlackTree.Entry<T>) w.left).isRed = false;
					w.isRed = true;
					rotateRight(w);
					w = (Entry<T>) node.parent.right;
				}
				if (((RedBlackTree.Entry<T>) w.right).isRed) {
					w.isRed = node.parent.isRed;
					node.parent.isRed = false;
					((RedBlackTree.Entry<T>) w.right).isRed = false;
					rotateLeft(node.parent);
					node = root;
				}
			} else {
				Entry<T> w = (Entry<T>) node.parent.left;
				if (w.isRed) {
					w.isRed = false;
					node.parent.isRed = true;
					rotateRight(node.parent);
					w = (Entry<T>) node.parent.left;
				}
				if (!((RedBlackTree.Entry<T>) w.right).isRed && !((RedBlackTree.Entry<T>) w.left).isRed) {
					w.isRed = true;
					node = node.parent;
					continue;
				} else if (!((RedBlackTree.Entry<T>) w.left).isRed) {
					((RedBlackTree.Entry<T>) w.right).isRed = false;
					w.isRed = true;
					rotateLeft(w);
					w = (Entry<T>) node.parent.left;
				}
				if (((RedBlackTree.Entry<T>) w.left).isRed) {
					w.isRed = node.parent.isRed;
					node.parent.isRed = false;
					((RedBlackTree.Entry<T>) w.left).isRed = false;
					rotateRight(node.parent);
					node = root;
				}
			}
		}
		node.isRed = false;
	}

	//function to find the inorder successor of a node
	private Entry<T> inOrderSuccessor(Entry<T> node) {
		while (node.left != nullNode) {
			node = (Entry<T>) node.left;
		}
		return node;
	}

	//replace a nnode with another
	public void replace(Entry<T> target, Entry<T> with) {
		if (target.parent == nullNode) {
			root = with;
		} else if (target == target.parent.left) {
			target.parent.left = with;
		} else
			target.parent.right = with;
		with.parent = target.parent;
	}

	public static void main(String[] args) {
		RedBlackTree<Integer> t = new RedBlackTree<>();
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
