package AVL;

/**
 *
 * AVLTree
 *
 * An implementation of aמ AVL Tree with
 * distinct integer keys and info.
 *
 */
public class AVLTree {
	// Create External Leaf object ONCE for entire tree.
	// All sons of real leaves will be this single virtual node, for the sake of memory saving
	private final ExternalLeaf virtualNode = new ExternalLeaf();
	public IAVLNode root;
	static final int COUNT = 10;

	/**
	 * public AVLTree()
	 * Default constructor. Sets root as the virtual node.
	 * Complexity: O(1)
	 */
	public AVLTree(){
		this.root = virtualNode;
	}

	/**
	 * public AVLTree(IAVLNode node)
	 * Second constructor. Allows creating a new Tree based rooted with an existing node.
	 * Complexity: O(1)
	 */
	public AVLTree(IAVLNode node){
		this();
		this.root = node;
		this.root.setParent(null);
	}

	/**
	 * public boolean empty()
	 * <p>
	 * Returns true if and only if the tree is empty.
	 */
	public boolean empty() {
		return !this.root.isRealNode();
	}

	/**
	 * public IAVLNode searchNode(int k)
	 * Returns a pointer to the Node with key k if it exists in the tree.
	 * otherwise, returns null.
	 * Complexity: O(log(n))
	 */
	public IAVLNode searchNode(int k) {
		// Uses BST properties to find node
		// Iteratively traverses through tree
		IAVLNode curr = this.root;
		while (curr.isRealNode()) {
			// If current Node Key equals k, we found requested node. returns node
			if (k == curr.getKey()) {
				return curr;
			// If key is smaller than current node, go left
			} else if (k < curr.getKey()) {
				curr = curr.getLeft();
			// If key is bigger than current node, go right
			} else {
				curr = curr.getRight();
			}
		}
		// Reaches here only if node is not in tree, in which case returns null
		return null;
	}

	/**
	 * public String search(int k)
	 * <p>
	 * Returns the info of an item with key k if it exists in the tree, using SearchNode helper function.
	 * otherwise, returns null.
	 * Complexity: O(log(n))
	 */
	public String search(int k) {
		// Calls SearchNode helper function.
		IAVLNode node = searchNode(k);
		// If node exists in tree, returns its value
		if (node != null){
			return node.getValue();
		}
		// Else node does not exist in tree, in which case returns null
		return null;
	}

	/**
	 * private int insertNode(IAVLNode node)
	 * Helper function for insert. Can be used to insert an existing node, only for internal use, therefore private.
	 * Function is private in order to prevent an insert of a node with children, which can defy AVLTree invariants.
	 * If Tree already has a node with the same key, returns -1. Otherwise inserts and calls rebalance function.
	 * Complexity: O(log(n))
	 */
	private int insertNode(IAVLNode node){
		// Uses BST properties to find node
		// If tree is empty, sets Tree root as the new node. Returns 0 since no rebalancing ops were made
		if (empty()) {
			this.root = node;
			return 0;
		}
		// Else tree is not empty, Iteratively traverses through tree to find insert location
		IAVLNode curr = this.root;
		while ((node.getKey() < curr.getKey() && curr.getLeft().isRealNode()) ||
				(node.getKey() > curr.getKey() && curr.getRight().isRealNode())) {
			if (node.getKey() < curr.getKey()) {
				curr = curr.getLeft();
			} else {
				curr = curr.getRight();
			}
		}

		// If current key is the same key as node intended for insert then it is already in the tree
		// Does not insert, returns -1
		if (node.getKey() == curr.getKey()) {
			return -1;
		}

		// If key of new node is smaller than current key, insert as left child to retain BST properties
		if (node.getKey() < curr.getKey()) {
			curr.setLeft(node);
		}

		// If key of new node is bigger than current key, insert as right child to retain BST properties
		if (node.getKey() > curr.getKey()) {
			curr.setRight(node);
		}

		// Set current node as the parent of the new node
		node.setParent(curr);
		// Send the current node for rebalancing, and return rebalance function output i.e number of rebalance ops
		return this.rebalance(curr);
	}

	/**
	 * public int insert(int k, String i)
	 * <p>
	 * Inserts an item with key k and info i to the AVL tree.
	 * The tree must remain valid, i.e. keep its invariants.
	 * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
	 * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
	 * Returns -1 if an item with key k already exists in the tree.
	 * Uses insertNode helper function.
	 * Complexity: O(log(n))
	 */
	public int insert(int k, String i) {
		// Create new AVLNode with key k and info i
		// Send new Node to insertNode function and return its output, i.e. number of rebalancing ops
		return this.insertNode(new AVLNode(k, i));
	}

	/**
	 * public int delete(int k)
	 * <p>
	 * Deletes an item with key k from the binary tree, if it is there, and calls rebalance function.
	 * The tree must remain valid, i.e. keep its invariants.
	 * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
	 * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
	 * Returns -1 if an item with key k was not found in the tree.
	 * Complexity: O(log(n))
	 */
	public int delete(int k) {
		// If tree is empty return -1
		if (empty()) {
			return -1;
		}
		// Get pointer to the requested node
		// If there is no node with key k in Tree, return -1
		IAVLNode curr = searchNode(k);
		if (curr == null){
			return -1;
		}

		// Else tree is not empty and node with key k exists in Tree. Node in curr pointer needs to be deleted
		// Declares and sets successor temporarily as null. Will be used later if deleted node has 2 children
		IAVLNode succ = null;

		// Set default node to rebalance after delete - the parent of node k, in case it is a leaf or unary node
		// In case of 2 children, the node to rebalance would be later changed to parent of successor or successor
		IAVLNode toRebalance = curr.getParent();

		// If deleted node is leaf
		if (!curr.getLeft().isRealNode() && !curr.getRight().isRealNode()) {
			if (curr == this.root){
				this.root = null;
			}
			else if (curr.getParent().getLeft() == curr){
				curr.getParent().setLeft(virtualNode);
			}
			else{
				curr.getParent().setRight(virtualNode);
			}
		}
		// If deleted node has 2 children
		else if (curr.getLeft().isRealNode() && curr.getRight().isRealNode()) {
			// Set succ as the successor of current node
			succ =  Successor(curr);

			// If successor is son of curr, set successor as the node to rebalance
			if (succ.getParent() == curr){
				toRebalance = succ;
			}
			else{
				// If successor is NOT son of curr, set successor's parent as the node for later rebalancing
				toRebalance = succ.getParent();
				// If successor has right child, set it as successor's parent's left child
				if (succ.getRight().isRealNode()) {
					succ.getParent().setLeft(succ.getRight());
					succ.getRight().setParent(succ.getParent());
				}
				// If successor is the left child of its parent, set parent left child as virtual Node
				if (succ.getParent().getLeft()==succ){
					succ.getParent().setLeft(virtualNode);
				}
				// Else successor is the right child of its parent, set parent right child as virtual Node
				else{
					succ.getParent().setRight(virtualNode);
				}
				// Set deleted node's right child as the new right child of the successor
				succ.setRight(curr.getRight());
				curr.getRight().setParent(succ);
			}
			// Set deleted node's left child as the new left child of the successor
			succ.setLeft(curr.getLeft());
			curr.getLeft().setParent(succ);
			// Set parent of Deleted Node as parent of successor
			succ.setParent(curr.getParent());
			// If deleted node has parent, i.e. it is NOT the Tree root
			if (curr.getParent() != null) {
				// If it is the right son of its parent, make the successor as the parent's new right child
				if (curr.getParent().getRight() == curr) {
					curr.getParent().setRight(succ);
				// Else it is the left son of its parent, make the successor as the parent's new left child
				} else {
					curr.getParent().setLeft(succ);
				}
			}
			// Else delete node IS the Tree root. Make the Successor the new Tree root
			else{
				this.root = succ;
			}

			// Set the rank of the successor to be the rank of the deleted node, since it replaced it in the Tree
			succ.setRank(curr.getHeight());
		}

		// If deleted node has one child, i.e. it is an Unary Node
		// If deleted node has a right child
		else if (curr.getRight().isRealNode()) {
			// Set deleted node right child's parent as the deleted node's parent
			curr.getRight().setParent(curr.getParent());
			// If deleted node has parent, i.e. it is NOT the Tree root
			if (curr.getParent() != null) {
				// If it is the right son of its parent, set its right child as the new right child of its parent
				if (curr.getParent().getRight() == curr){
					curr.getParent().setRight(curr.getRight());
				}
				// Else it is the left son of its parent, set its right child as the new left child of its parent
				else{
					curr.getParent().setLeft(curr.getRight());
				}
			}
			// Else delete node IS the Tree root. Make the deleted node's right child the new Tree root
			else{
				this.root = curr.getRight();
			}
		// Else deleted node has a left child
		} else  {
			// Set deleted node left child's parent as the deleted node's parent
			curr.getLeft().setParent(curr.getParent());
			// If deleted node has parent, i.e. it is NOT the Tree root
			if (curr.getParent() != null) {
				// If it is the right son of its parent, set its left child as the new right child of its parent
				if (curr.getParent().getRight() == curr){
					curr.getParent().setRight(curr.getLeft());
				}
				// Else it is the left son of its parent, set its left child as the new left child of its parent
				else{
					curr.getParent().setLeft(curr.getLeft());
				}
			}
			// Else delete node IS the Tree root. Make the deleted node's left child the new Tree root
			else{
				this.root = curr.getLeft();
			}
		}

		// Sends relevant node for rebalancing. Returns rebalance function output i.e. number of rebalancing ops
		return rebalance(toRebalance);
	}

	/**
	 * public IAVLNode Successor(IAVLNode node)
	 * Returns the Successor of the current node, if a successor exists.
	 * For the maximum element which has no successor, returns null.
	 * Complexity: O(log(n))
	 */
	public IAVLNode Successor(IAVLNode node) {
		// If node has a right child
	   if(node.getRight().isRealNode()){
		   // Find the minimum child in the right child subtree. That is the successor. Returns it.
		   IAVLNode curr = node.getRight();
		   while (curr.getLeft().isRealNode()) {
			   curr = curr.getLeft();
		   }
		   return curr;
	   }
	   // Else the successor is further up in the Tree.
	   // Sets succ temporarily as the node's parent
	   IAVLNode succ = node.getParent();
	   // While the node is its parent's right child, its parent is smaller than the current node and is not the successor
	   while(succ.isRealNode() && succ.getRight() == node){
	   	   // Keep Traversing up the tree
		   node = succ;
		   succ = succ.getParent();
	   }
	   // Here satisifies the condition that the current node is its parent left child.
	   // Therefore its parent is bigger hence it is the successor
	   // Returns succ as the successor
	   return succ;
   }

   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty.
	* Complexity: O(log(n))
    */
   public String min()
   {
	   return this.root.getMin().getValue();
   }

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty.
	* Complexity: O(log(n))
    */
   public String max()
   {
	   return this.root.getMax().getValue();
   }

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   * Uses helper Recursion function inOrderKeys
   * Complexity: O(n)
   */
  public int[] keysToArray(){

	  // Initializes new array with the size of the Tree
  	  int[] keys = new int[size()];
  	  // Initializes new Array with 1 cell. Used as a "global" parameter for inOrderKeys to insert in correct position
	  int[] index = new int[]{0};
	  // Calls inOrderKeys which fills up the keys array, then returns it
	  inOrderKeys(this.root, keys,index);
	  return keys;
  }

	/**
	 * public static void inOrderKeys
 	 * Helper Recursion function for keysToArray. Traverses tree In-Order and adds keys to keys[] array
	 * Complexity: O(n))
	 */
  public static void inOrderKeys (IAVLNode node, int [] keys, int[] index){
  		// Recursively traverses Tree in-order and fills up the keys array

  		// If node is virtual node, return and do nothing
		if (!node.isRealNode()) {
			return;
		}
		// Else it is a real node
	    // Recursive call for node's left child
		inOrderKeys(node.getLeft(), keys, index);
		// Insert node key to keys array in the correct position, in index[] array first cell
		keys[index[0]] = node.getKey();
		// Increase index by 1 so next node will be inserted in the next position in the array
		index[0]++;
	    // Recursive call for node's right child
		inOrderKeys(node.getRight(), keys, index);
	}

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   * Uses helper Recursion function inOrderVals
   * Complexity: O(n)
   */
  public String[] infoToArray()
  {
	  // Initializes new array with the size of the Tree
  	  String[] values = new String[size()];
	  // Initializes new Array with 1 cell. Used as a "global" parameter for inOrderKeys to insert in correct position
	  int[] index = new int[]{0};
	  // Calls inOrderVals which fills up the values array, then returns it
	  inOrderVals(this.root, values ,index);
	  return values;
  }

	/**
	 * public static void inOrderVals
	 * Helper Recursion function for infoToArray. Traverses tree In-Order and adds values to values[] array
	 * Complexity: O(n))
	 */
	public static void inOrderVals (IAVLNode node, String [] values, int[] index){
		// Recursively traverses Tree in-order and fills up the values array

		// If node is virtual node, return and do nothing
		if (!node.isRealNode()) {
			return;
		}
		// Else it is a real node
		// Recursive call for node's left child
		inOrderVals(node.getLeft(), values, index);
		// Insert node info to values array in the correct position, in index[] array first cell
		values[index[0]] = node.getValue();
		// Increase index by 1 so next node will be inserted in the next position in the array
		index[0]++;
		// Recursive call for node's right child
		inOrderVals(node.getRight(), values, index);
	}

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
	* Each node holds the number of nodes in Subtree, so size() returns the size field of the Tree's root node
	* Complexity: O(1)
    */
   public int size()
   {
	   return this.root.getSize();
   }

   /**
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
	* Complexity: O(1)
    */
   public IAVLNode getRoot()
   {
	   if(empty()){
		   return null;
	   }
	   return this.root;
   }
   
   /**
    * public AVLTree[] split(int x)
    *
    * splits the tree into 2 trees according to the key x. 
    * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
    * 
	* precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
    * postcondition: none
	*
	* Complexity: O(log(n))
    */   
   public AVLTree[] split(int x)
   {
	   // Searches tree to get pointer to node with key x
   	   IAVLNode curr = this.searchNode(x);

	   // Declares new AVL Tree t1 - this Tree will sustain for every node m in t1, m.key < x
   	   AVLTree t1 = new AVLTree(curr.getLeft());
	   // Declares new AVL Tree t2 - this Tree will sustain for every node m in t2, m.key > x
	   AVLTree t2 = new AVLTree(curr.getRight());

	   // Disconnect node x from its children and parent
	   curr.setLeft(virtualNode);
	   curr.setRight(virtualNode);
	   IAVLNode parent = curr.getParent();
	   curr.setParent(null);

	   // Declare new Tree toJoin. In every iteration will point to the Tree being joined to either t1 or t2
	   AVLTree toJoin;

	   // While we haven't yet reached root
	   while(parent != null){
	   		// If node is the right son of its parent, i.e. node key is bigger than parent key
	   		if (parent.getRight() == curr){
	   			// subTree to join is the left son of parent
	   			toJoin = new AVLTree(parent.getLeft());
	   			parent.setLeft(virtualNode);

	   			// Set curr as parent and parent as next parent to keep traversing up the tree to the root
	   			curr = parent;
	   			parent = curr.getParent();
	   			curr.setParent(null);

	   			// Join t1 with toJoin and curr (now the parent)
	   			t1.join(curr, toJoin);
			}
	   		// Else node is the left son of its parent, i.e. node key is smaller than parent key
	   		else{
				// subTree to join is the right son of parent
				toJoin = new AVLTree(parent.getRight());
				parent.setRight(virtualNode);
				// Set curr as parent and parent as next parent to keep traversing up the tree to the root
				curr = parent;
				parent = curr.getParent();
				curr.setParent(null);

				// Join t2 with toJoin and curr (now the parent)
				t2.join(curr, toJoin);
			}
	   }

	   // Sets root of old tree as the root of t1
	   // ** NOTE **
	   // The old tree root MUST be changed since the Tree sent to this function no longer exists as a valid AVL tree
	   // Our choice is ARBITRARY. The root of the old tree could alternatively be pointed to t2 or null.
	   this.root = t1.root;

	   // Returns a list of the 2 trees
	   return new AVLTree[]{t1, t2};
   }
   
   /**
    * public int join(IAVLNode x, AVLTree t)
    *
    * joins t and x with the tree, then calls rebalance function.
    * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
	*
	* precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
    * postcondition: none
	*
	* Complexity: O(|this.rank - t.rank| + 1)
    */   
   public int join(IAVLNode x, AVLTree t) {
   	   // If one of the trees is empty
   	   if (empty() || t.empty()){
   	   	   // Disconnect x from its children and parents
		   x.setLeft(virtualNode);
		   x.setRight(virtualNode);
		   x.setParent(null);
		   x.setRank(0);

		   // If this Tree is empty
		   if (empty()){
		   	   // Insert node x to t tree and change this root's pointer to t's root
			   t.insertNode(x);
			   this.root = t.root;
		   }
		   // Else t is empty
		   else{
		   	   // Insert node x to this tree
			   this.insertNode(x);
		   }
		   // Send x for rebalancing
		   this.rebalance(x);
		   // Return complexity of the operation - In this case it is the height of the NOT EMPTY tree + 1
		   return Math.max(this.root.getHeight(), t.root.getHeight()) + 1;
	   }

	   // Else neither tree is empty. Value to return is the complexity (|tree.rank - t.rank| + 1)
   	   int valuetoreturn = Math.abs(this.root.getHeight() - t.root.getHeight())+1;

	   AVLTree smaller, bigger;

	   // Set smaller to point to the tree who's keys are smaller that the other tree
	   // Set bigger to point to the tree who's keys are bigger than the other tree
	   if (this.root.getKey() < x.getKey()){
	   		smaller = this;
	   		bigger = t;
	   }
	   else{
	   		smaller = t;
	   		bigger = this;
	   }

	   IAVLNode curr;
	   // If Height of smaller is shorter than bigger
	   if (smaller.root.getHeight() <= bigger.root.getHeight()){
	   		// In this case, traverse along the left side of the trees with bigger keys
	   		curr = bigger.root;
	   		// while current rank is bigger than the smaller's root rank, keep going left
	   		while ((curr.getLeft().isRealNode()) && (curr.getHeight() > smaller.root.getHeight())){
	   			curr = curr.getLeft();
			}
	   		// Set smaller's root as x's left child
	   		x.setLeft(smaller.root);
		    smaller.root.setParent(x);
	   		// Set current node as x's right child
	   		x.setRight(curr);
		    // Set x's rank as 1 higher than smaller's root
		    x.setRank(smaller.root.getHeight() + 1);

		    // If curr is NOT this tree's root
		    if (curr.getParent() != null) {
		    	// Set x as the new left child of curr's parent
				curr.getParent().setLeft(x);
				// Set the current root as bigger's root, since bigger is the taller tree
				this.root = bigger.root;
			}
		    // Else curr IS this tree's root
	   		else{
	   			// Set x as the new tree root, since it is now the parent of the old root
	   			this.root = x;
			}
	   }
	   // Else Height of smaller is longer than bigger
	   else{
		   // In this case, traverse along the right side of the trees with smaller keys
		   curr = smaller.root;
		   // while current rank is bigger than the bigger's root rank, keep going right
		   while ((curr.getRight().isRealNode()) && (curr.getHeight() > bigger.root.getHeight())){
			   curr = curr.getRight();
		   }
		   // Set bigger's root as x's right child
		   x.setRight(bigger.root);
		   bigger.root.setParent(x);
		   // Set current node as x's left child
		   x.setLeft(curr);
		   // Set x's rank as 1 higher than bigger's root
		   x.setRank(bigger.root.getHeight() + 1);

		   // If curr is NOT this tree's root
		   if (curr.getParent() != null){
			   // Set x as the new right child of curr's parent
			   curr.getParent().setRight(x);
			   // Set the current root as smaller's root, since smaller is the taller tree
			   this.root = smaller.root;
		   }
		   // Else curr IS this tree's root
		   else{
			   // Set x as the new tree root, since it is now the parent of the old root
		   	   this.root = x;
		   }
	   }
	   // Set x's parent as curr's parent, and curr's parent as x
	   x.setParent(curr.getParent());
	   curr.setParent(x);

	   // Send x for rebalancing
	   this.rebalance(x);
	   // Return operation complexity
	   return valuetoreturn;

   }

	/**
	 *  public IAVLNode rotateLeft(IAVLNode node)
	 *
	 *  Rotates Node Left. Returns the node which after the rotation, is the new root of subtree
	 *
	 *  Complexity: O(1)
	 */
	public IAVLNode rotateLeft(IAVLNode node) {
		// If node is virtual node or does not have a real right child, cannot rotate left. Returns same node
		if (!node.isRealNode()) {
			return node;
		}
		if (!node.getRight().isRealNode()) {
			return node;
		}
		// Declare new root of sub-tree after rotation - the right child of the rotated node
		IAVLNode new_mid = node.getRight();
		new_mid.setParent(node.getParent());
		// If rotated node is NOT the tree root, i.e. has a parent, set the new root of subtree to be its new child
		if (node.getParent() != null) {
			if (node.getParent().getLeft() == node) {
				node.getParent().setLeft(new_mid);
			} else {
				node.getParent().setRight(new_mid);
			}
		}
		// Change children pointers according to Rotation Algorithm
		node.setRight(new_mid.getLeft());
		new_mid.getLeft().setParent(node);
		new_mid.setLeft(node);
		node.setParent(new_mid);
		// If new root of sub-tree has no parent, then it is the new root of the entire tree
		if (new_mid.getParent() == null) {
			this.root = new_mid;
		}
		// Update fields of node that changed positions in the tree and their fields may have changed
		node.updateFields();
		new_mid.updateFields();
		// Return new root of sub-tree after the rotation
		return new_mid;
	}

	/**
	 *  public IAVLNode rotateRight(IAVLNode node)
	 *
	 *  Rotates Node Right. Returns the node which after the rotation, is the new root of subtree
	 *
	 *  Complexity: O(1)
	 */
	public IAVLNode rotateRight(IAVLNode node) {
		// If node is virtual node or does not have a real left child, cannot rotate right. Returns same node
		if(!node.isRealNode()){ return node;}
		if(!node.getLeft().isRealNode()){ return node;}
		// Declare new root of sub-tree after rotation - the left child of the rotated node
		IAVLNode new_mid = node.getLeft();
		new_mid.setParent(node.getParent());
		// If rotated node is NOT the tree root, i.e. has a parent, set the new root of subtree to be its new child
		if(node.getParent()!=null) {
			if (node.getParent().getLeft() == node) {
				node.getParent().setLeft(new_mid);
			} else {
				node.getParent().setRight(new_mid);
			}
		}
		// Change children pointers according to Rotation Algorithm
		node.setLeft(new_mid.getRight());
		new_mid.getRight().setParent(node);
		new_mid.setRight(node);
		node.setParent(new_mid);
		// If new root of sub-tree has no parent, then it is the new root of the entire tree
		if(new_mid.getParent() == null){
			this.root = new_mid;
		}
		// Update fields of node that changed positions in the tree and their fields may have changed
		node.updateFields();
		new_mid.updateFields();
		// Return new root of sub-tree after the rotation
		return new_mid;
	}


	// Wrapper over print2DUtil()

	public static void print2DUtil(IAVLNode root, int space)
	{

		// Base case
		if (!root.isRealNode())
			return;

		// Increase distance between levels
		space += COUNT;

		// Process right child first
		print2DUtil(root.getRight(), space);

		// Print current node after space
		// count
		System.out.print("\n");
		for (int i = COUNT; i < space; i++)
			System.out.print(" ");
		System.out.print(root.getKey() + "\n");

		// Process left child
		print2DUtil(root.getLeft(), space);
	}

	// Wrapper over print2DUtil()
	public static void print2D(IAVLNode root)
	{
		System.out.println("_______________");
		// Pass initial space count as 0

		print2DUtil(root, 0);
	}

	/**
	 *  public int rebalance(IAVLNode node)
	 *
	 *  Rebalances Tree.
	 *  Returns number of rebalancing operations. Each promote, demote, or single rotation is counted once.
	 *  For example, 1 promote and a double rotation are 3 rebalancing operations.
	 *
	 *  Complexity: O(log(n))
	 */
	public int rebalance(IAVLNode node){

		// Initiate counter for number of rebalancing operations
		int numOps = 0;

		// While we have not yet reached the top of the tree, perform the following
		while(node != null){
			// Update fields of node, as its fields may have changed due to insertion / deletion / rotation
			node.updateFields();

			// Cases Relevant for after Insertion or Join
			if (((node.getLeftEdgeRank() == 0) && (node.getRightEdgeRank() == 1)) ||
				((node.getLeftEdgeRank() == 1) && (node.getRightEdgeRank() == 0))){
				node.promote();
				numOps++;
			}
			else if ((node.getLeftEdgeRank() == 0) && (node.getRightEdgeRank() == 2)){
				// Relevant for Join
				if ((node.getLeft().getLeftEdgeRank() == 1) && (node.getLeft().getRightEdgeRank() == 1)){
					node.getLeft().promote();
				}
				// Relevant for Insert
				else {
					if ((node.getLeft().getLeftEdgeRank() == 2) && (node.getLeft().getRightEdgeRank() == 1)) {
						node.getLeft().demote();
						node.getLeft().getRight().promote();
						this.rotateLeft(node.getLeft());
						numOps += 3;
					}
					node.demote();
				}
				node = this.rotateRight(node);
				numOps += 2;
			}
			else if ((node.getLeftEdgeRank() == 2) && (node.getRightEdgeRank() == 0)){
				// Relevant for Join
				if ((node.getRight().getLeftEdgeRank() == 1) && (node.getRight().getRightEdgeRank() == 1)){
					node.getRight().promote();
				}
				// Relevant for Insert
				else {
					if ((node.getRight().getLeftEdgeRank() == 1) && (node.getRight().getRightEdgeRank() == 2)) {
						node.getRight().demote();
						node.getRight().getLeft().promote();
						this.rotateRight(node.getRight());
						numOps += 3;
					}
					node.demote();
				}
				node = this.rotateLeft(node);
				numOps += 2;
			}

			// Cases Relevant for after Deletion
			else if ((node.getLeftEdgeRank() == 2) && (node.getRightEdgeRank() == 2)){
				node.demote();
				numOps++;
			}
			else if ((node.getLeftEdgeRank() == 3) && (node.getRightEdgeRank() == 1)){
				if ((node.getRight().getLeftEdgeRank() == 1) && (node.getRight().getRightEdgeRank() == 2)){
					node.getRight().getLeft().promote();
					node.getRight().demote();
					node.demote();
					node.demote();
					this.rotateRight(node.getRight());
					node = this.rotateLeft(node);
					numOps += 6;
				}
				else if ((node.getRight().getLeftEdgeRank() == 2) && (node.getRight().getRightEdgeRank() == 1)){
					node.demote();
					node.demote();
					node = this.rotateLeft(node);
					numOps += 3;
				}
				else if ((node.getRight().getLeftEdgeRank() == 1) && (node.getRight().getRightEdgeRank() == 1)){
					node.demote();
					node.getRight().promote();
					node = this.rotateLeft(node);
					numOps += 3;
				}
			}
			else if ((node.getLeftEdgeRank() == 1) && (node.getRightEdgeRank() == 3)){
				if ((node.getLeft().getLeftEdgeRank() == 2) && (node.getLeft().getRightEdgeRank() == 1)){
					node.getLeft().getRight().promote();
					node.getLeft().demote();
					node.demote();
					node.demote();
					this.rotateLeft(node.getLeft());
					node = this.rotateRight(node);
					numOps += 6;
				}
				else if ((node.getLeft().getLeftEdgeRank() == 1) && (node.getLeft().getRightEdgeRank() == 2)){
					node.demote();
					node.demote();
					node = this.rotateRight(node);
					numOps += 3;
				}
				else if ((node.getLeft().getLeftEdgeRank() == 1) && (node.getLeft().getRightEdgeRank() == 1)){
					node.demote();
					node.getLeft().promote();
					node = this.rotateRight(node);
					numOps += 3;
				}
			}

			// Change node pointer to its parent to continue traversing up the tree towards the root
			node = node.getParent();
		}

		// If tree is empty after rebalance - set root as virtual node
		if (this.root == null){
			this.root = virtualNode;
		}

		return numOps;
	}

	/** 
	 * public interface IAVLNode
	 * ! Do not delete or modify this - otherwise all tests will fail !
	 */
	public interface IAVLNode{	
		public int getKey(); // Returns node's key (for virtual node return -1).
		public String getValue(); // Returns node's value [info], for virtual node returns null.
		public void setLeft(IAVLNode node); // Sets left child.
		public IAVLNode getLeft(); // Returns left child, if there is no left child returns null.
		public void setRight(IAVLNode node); // Sets right child.
		public IAVLNode getRight(); // Returns right child, if there is no right child return null.
		public void setParent(IAVLNode node); // Sets parent.
		public IAVLNode getParent(); // Returns the parent, if there is no parent return null.
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node.
    	public int getHeight(); // Returns the height of the node (-1 for virtual nodes).
		public int getSize(); // Returns size of sub-tree
		public int getLeftEdgeRank(); // Returns rank diff of right edge
		public int getRightEdgeRank(); // Returns rank diff of left edge
		public void updateSize(); // Updates size of tree: left.size + right.size + 1
		public void promote(); // promotes node rank
		public void demote(); // demotes node rank
		public void setRank(int rank); // setsRank
		public IAVLNode getMin();
		public IAVLNode getMax();
		public void updateMin();
		public void updateMax();
		public void updateFields();
	}

   /** 
    * public class AVLNode
    *
    * If you wish to implement classes other than AVLTree
    * (for example AVLNode), do it in this file, not in another file. 
    * 
    * This class can and MUST be modified (It must implement IAVLNode).
    */
  public class AVLNode implements IAVLNode{
  		private int key;
  		private String info;
  		private IAVLNode left;
  		private IAVLNode right;
  		private IAVLNode parent;
  		private int rank;
		private int size;
		private IAVLNode minNode;
		private IAVLNode maxNode;

	   /**
		* public AVLNode(int key, String info)
		* Constructor for AVLNode. creates new node with stated key and value
		* Sets rank as 0, size as 1, parent as null.
		* Sets left and right children as the Tree's virtual leaf, created in AVLTree enclosing class
		*
		* Complexity: O(1)
		*/
		public AVLNode(int key, String info){
			this.key = key;
			this.info = info;
			this.rank = 0;
			this.left = AVLTree.this.virtualNode;
			this.right = AVLTree.this.virtualNode;
			this.size = 1;
			this.minNode = this;
			this.maxNode = this;
		}

	   /**
		* public int getKey()
		* Returns key of node
		* Complexity: O(1)
		*/
		public int getKey()
		{
			return this.key;
		}

	   /**
		* public String getValue()
		* Returns info of node
		* Complexity: O(1)
		*/
		public String getValue()
		{
			return this.info;
		}

	   /**
		* public void setLeft(IAVLNode node)
		* Sets node left child
		* Complexity: O(1)
		*/
		public void setLeft(IAVLNode node)
		{
			this.left = node;
		}

	   /**
		* public IAVLNode getLeft()
		* Returns node left child
		* Complexity: O(1)
		*/
		public IAVLNode getLeft()
		{
			return this.left;
		}

	   /**
		* public void setRight(IAVLNode node)
		* Sets node right child
		* Complexity: O(1)
		*/
		public void setRight(IAVLNode node)
		{
			this.right = node;
		}

	   /**
		* public IAVLNode getRight()
		* Returns node right child
		* Complexity: O(1)
		*/
		public IAVLNode getRight()
		{
			return this.right;
		}

	   /**
		* public void setParent(IAVLNode node)
		* Sets parent node
		* Complexity: O(1)
		*/
		public void setParent(IAVLNode node)
		{
			this.parent = node;
		}

	   /**
		* public IAVLNode getParent()
		* Returns parent node
		* Complexity: O(1)
		*/
		public IAVLNode getParent()
		{
			return this.parent;
		}

	   /**
		* public boolean isRealNode()
		* Returns true if node is "real" i.e has rank >= 0
		* Else returns false i.e is a virtual node
		* Complexity: O(1)
		*/
		public boolean isRealNode()
		{
			return this.rank > -1;
		}

	   /**
		* public int getHeight()
		* Returns Height (rank) of node
		* Complexity: O(1)
		*/
	    public int getHeight()
	    {
	     	return this.rank;
	    }

	   /**
		* public int getLeftEdgeRank()
		* Returns rank difference between node and its left child
		* Complexity: O(1)
		*/
	    public int getLeftEdgeRank(){return this.rank - this.getLeft().getHeight();}

	   /**
		* public int getRightEdgeRank()
		* Returns rank difference between node and its right child
		* Complexity: O(1)
		*/
	    public int getRightEdgeRank(){return this.rank - this.getRight().getHeight();}

	   /**
		* public int getSize()
		* Returns size of node subtree, including node.
		* Complexity: O(1)
		*/
	    public int getSize(){
  			return this.size;
		}

	   /**
		* public void updateSize()
		* updates size of node subtree, including node, based on its direct children.
		* Complexity: O(1)
		*/
		public void updateSize(){
  			this.size = this.left.getSize() + this.right.getSize() + 1;
		}

	   /**
		* public void promote()
		* Increases tree rank by 1
		* Complexity: O(1)
		*/
		public void promote(){
  			this.rank++;
		}

	   /**
		* public void demote()
		* Decreases tree rank by 1
		* Complexity: O(1)
		*/
		public void demote(){
		   this.rank--;
	   }

	   /**
		* public void setRank(int rank)
		* Sets tree rank
		* Complexity: O(1)
		*/
	    public void setRank(int rank){
  			this.rank = rank;
	   }

	    public IAVLNode getMin(){return this.minNode;}

	    public IAVLNode getMax(){return this.maxNode;}

	    public void updateMin(){
	    	if (this.left.isRealNode()){
	    		this.minNode = this.left.getMin();
			}
	    	else{
	    		this.minNode = this;
			}
	    }

	    public void updateMax(){
			if (this.right.isRealNode()){
				this.maxNode = this.right.getMax();
			}
			else{
				this.maxNode = this;
			}
	    }

	    public void updateFields(){
	    	this.updateMin();
	    	this.updateMax();
	    	this.updateSize();
		}
  }

	/**
	 *  public class ExternalLeaf
	 *
	 *  This class extends AVLNode and represents a Virtual Node, with rank -1 and size 0
	 */
	public class ExternalLeaf extends AVLNode {
		/**
		 * public ExternalLeaf()
		 * External Leaf constructor.
		 * Sets rank -1 and size 0
		 * Complexity: O(1)
		 */
		public ExternalLeaf() {
			super(-1, null);
			super.rank = -1;
			super.size = 0;
		}
	}
  }
