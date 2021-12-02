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
		IAVLNode curr = this.root;
		while (curr.isRealNode()) {
			if (k == curr.getKey()) {
				return curr;
			} else if (k < curr.getKey()) {
				curr = curr.getLeft();
			} else {
				curr = curr.getRight();
			}
		}
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
		IAVLNode node = searchNode(k);
		if (node != null){
			return node.getValue();
		}
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
		if (empty()) {
			this.root = node;
			return 0;
		}
		IAVLNode curr = this.root;
		while ((node.getKey() < curr.getKey() && curr.getLeft().isRealNode()) ||
				(node.getKey() > curr.getKey() && curr.getRight().isRealNode())) {
			if (node.getKey() < curr.getKey()) {
				curr = curr.getLeft();
			} else {
				curr = curr.getRight();
			}
		}

		if (node.getKey() == curr.getKey()) {
			return -1;
		}

		if (node.getKey() < curr.getKey()) {
			curr.setLeft(node);
		}

		if (node.getKey() > curr.getKey()) {
			curr.setRight(node);
		}

		node.setParent(curr);
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
		if (empty()) {
			return -1;
		}
		IAVLNode curr = searchNode(k);
		if (curr == null){
			return -1;
		}
		IAVLNode succ = null;

		// Set default node to rebalance after delete - the parent of node k, in case it is a leaf or unary node
		// If it is a node with 2 children, the node to rebalance would be changed to parent of successor or successor
		IAVLNode toRebalance = curr.getParent();

		//is leaf
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
		//two children
		else if (curr.getLeft().isRealNode() && curr.getRight().isRealNode()) {
			succ =  Successor(curr);

			// If successor is son of curr, set node to rebalance the as the successor. Otherwise set its parent
			if (succ.getParent() == curr){
				toRebalance = succ;
			}
			else{
				toRebalance = succ.getParent();
				if (succ.getRight().isRealNode()) {
					succ.getParent().setLeft(succ.getRight());
					succ.getRight().setParent(succ.getParent());
				}
				if (succ.getParent().getLeft()==succ){
					succ.getParent().setLeft(virtualNode);
				}
				else{
					succ.getParent().setRight(virtualNode);
				}
				succ.setRight(curr.getRight());
				curr.getRight().setParent(succ);
			}
			succ.setLeft(curr.getLeft());
			curr.getLeft().setParent(succ);
			succ.setParent(curr.getParent());
			if (curr.getParent() != null) {
				if (curr.getParent().getRight() == curr) {
					curr.getParent().setRight(succ);
				} else {
					curr.getParent().setLeft(succ);
				}
			}
			else{
				this.root = succ;
			}

			succ.setRank(curr.getHeight());
		}

		//one child
		else if (curr.getRight().isRealNode()) {
			curr.getRight().setParent(curr.getParent());
			if (curr.getParent() != null) {
				if (curr.getParent().getRight() == curr){
					curr.getParent().setRight(curr.getRight());
				}
				else{
					curr.getParent().setLeft(curr.getRight());
				}
			}
			else{
				this.root = curr.getRight();
			}
		} else  {
			curr.getLeft().setParent(curr.getParent());
			if (curr.getParent() != null) {
				if (curr.getParent().getRight() == curr){
					curr.getParent().setRight(curr.getLeft());
				}
				else{
					curr.getParent().setLeft(curr.getLeft());
				}
			}
			else{
				this.root = curr.getLeft();
			}
		}

		return rebalance(toRebalance);
	}

	/**
	 * public IAVLNode Successor(IAVLNode node)
	 * Returns the Successor of the current node, if a successor exists.
	 * For the maximum element which has no successor, returns null.
	 * Complexity: O(log(n))
	 */
	public IAVLNode Successor(IAVLNode node) {
	   if(node.getRight().isRealNode()){
		   //minimumleaf
		   IAVLNode curr = node.getRight();
		   while (curr.getLeft().isRealNode()) {
			   curr = curr.getLeft();
		   }
		   return curr;
	   }
	   IAVLNode succ = node.getParent();
	   while(succ.isRealNode() && succ.getRight() == node){
		   node = succ;
		   succ = succ.getParent();
	   }
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
	   if (empty()){
	   		return null;
	   }
   	   IAVLNode x = this.root;
	   while (x.getLeft().isRealNode()){
	   		x = x.getLeft();
	   }
	   return x.getValue();
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
	   if(this.empty()){
		   return null;
	   }
	   IAVLNode curr = this.root;
	   while(curr.getRight().isRealNode()){
		   curr = curr.getRight();
	   }
	   return curr.getValue();
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

	  int[] keys = new int[size()];
	  int[] index = new int[]{0};
	  inOrderKeys(this.root, keys,index);
	  return keys;
  }

	/**
	 * public static void inOrderKeys
 	 * Helper Recursion function for keysToArray. Traverses tree In-Order and adds keys to keys[] array
	 * Complexity: O(n))
	 */
  public static void inOrderKeys (IAVLNode node, int [] keys, int[] index){
		if (!node.isRealNode()) {
			return;
		}
		inOrderKeys(node.getLeft(), keys, index);
		keys[index[0]] = node.getKey();
		index[0]++;
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
	  String[] values = new String[size()];
	  int[] index = new int[]{0};
	  inOrderVals(this.root, values ,index);
	  return values;
  }

	/**
	 * public static void inOrderVals
	 * Helper Recursion function for infoToArray. Traverses tree In-Order and adds values to values[] array
	 * Complexity: O(n))
	 */
	public static void inOrderVals (IAVLNode node, String [] values, int[] index){
		if (!node.isRealNode()) {
			return;
		}
		inOrderVals(node.getLeft(), values, index);
		values[index[0]] = node.getValue();
		index[0]++;
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
	   IAVLNode curr = this.searchNode(x);

	   AVLTree t1 = new AVLTree(curr.getLeft());
	   curr.setLeft(virtualNode);
	   AVLTree t2 = new AVLTree(curr.getRight());
	   curr.setRight(virtualNode);
	   IAVLNode parent = curr.getParent();
	   curr.setParent(null);
	   AVLTree toJoin;

	   while(parent != null){
	   		if (parent.getRight() == curr){
	   			toJoin = new AVLTree(parent.getLeft());
	   			parent.setLeft(virtualNode);
	   			curr = parent;
	   			parent = curr.getParent();
	   			curr.setParent(null);
	   			t1.join(curr, toJoin);
			}
	   		else{
				toJoin = new AVLTree(parent.getRight());
				parent.setRight(virtualNode);
				curr = parent;
				parent = curr.getParent();
				curr.setParent(null);
				t2.join(curr, toJoin);
			}
	   }
	   this.root = t1.root;
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
	* Complexity: O(log(M)) where M = max{this.size(), t.size()}
    */   
   public int join(IAVLNode x, AVLTree t) {
   	   if (empty() || t.empty()){
		   x.setLeft(virtualNode);
		   x.setRight(virtualNode);
		   x.setParent(null);
		   x.setRank(0);
		   if (empty()){
			   t.insertNode(x);
			   this.root = t.root;
		   }
		   else{
			   this.insertNode(x);
		   }
		   this.rebalance(x);
		   return Math.max(this.root.getHeight(), t.root.getHeight()) + 1;
	   }

	   int valuetoreturn = Math.abs(this.root.getHeight() - t.root.getHeight())+1;

	   AVLTree smaller, bigger;

	   if (this.root.getKey() < x.getKey()){
	   		smaller = this;
	   		bigger = t;
	   }
	   else{
	   		smaller = t;
	   		bigger = this;
	   }

	   IAVLNode curr;
	   if (smaller.root.getHeight() <= bigger.root.getHeight()){
	   		// Height of smaller is shorter than bigger
	   		curr = bigger.root;
	   		while ((curr.getLeft().isRealNode()) && (curr.getHeight() > smaller.root.getHeight())){
	   			curr = curr.getLeft();
			}
	   		x.setLeft(smaller.root);
	   		x.setRight(curr);
		    smaller.root.setParent(x);
		    x.setRank(smaller.root.getHeight() + 1);
		    if (curr.getParent() != null) {
				curr.getParent().setLeft(x);
				this.root = bigger.root;
			}
	   		else{
	   			this.root = x;
			}
	   }
	   else{
		   // Height of smaller is longer than bigger
		   curr = smaller.root;
		   while ((curr.getRight().isRealNode()) && (curr.getHeight() > smaller.root.getHeight())){
			   curr = curr.getRight();
		   }
		   x.setRight(bigger.root);
		   x.setLeft(curr);
		   bigger.root.setParent(x);
		   x.setRank(bigger.root.getHeight() + 1);
		   if (curr.getParent() != null){
			   curr.getParent().setRight(x);
			   this.root = smaller.root;
		   }
		   else{
		   	   this.root = x;
		   }
	   }
	   x.setParent(curr.getParent());
	   curr.setParent(x);

	   this.rebalance(x);
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
		if (!node.isRealNode()) {
			return node;
		}
		if (!node.getRight().isRealNode()) {
			return node;
		}
		IAVLNode new_mid = node.getRight();
		new_mid.setParent(node.getParent());
		if (node.getParent() != null) {
			if (node.getParent().getLeft() == node) {
				node.getParent().setLeft(new_mid);
			} else {
				node.getParent().setRight(new_mid);
			}
		}
		node.setRight(new_mid.getLeft());
		new_mid.getLeft().setParent(node);
		new_mid.setLeft(node);
		node.setParent(new_mid);
		if (new_mid.getParent() == null) {
			this.root = new_mid;
		}
		node.updateSize();
		new_mid.updateSize();
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
		if(!node.isRealNode()){ return node;}
		if(!node.getLeft().isRealNode()){ return node;}
		IAVLNode new_mid = node.getLeft();

		new_mid.setParent(node.getParent());
		if(node.getParent()!=null) {
			if (node.getParent().getLeft() == node) {
				node.getParent().setLeft(new_mid);
			} else {
				node.getParent().setRight(new_mid);
			}
		}

		node.setLeft(new_mid.getRight());
		new_mid.getRight().setParent(node);
		new_mid.setRight(node);
		node.setParent(new_mid);
		if(new_mid.getParent() == null){
			this.root = new_mid;
		}
		node.updateSize();
		new_mid.updateSize();
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

		int numOps = 0;

		while(node != null){
			node.updateSize();
			// After Insertion
			if (((node.getLeftEdgeRank() == 0) && (node.getRightEdgeRank() == 1)) ||
				((node.getLeftEdgeRank() == 1) && (node.getRightEdgeRank() == 0))){
				node.promote();
				numOps++;
			}
			else if ((node.getLeftEdgeRank() == 0) && (node.getRightEdgeRank() == 2)){
				if ((node.getLeft().getLeftEdgeRank() == 2) && (node.getLeft().getRightEdgeRank() == 1)){
					node.getLeft().demote();
					node.getLeft().getRight().promote();
					this.rotateLeft(node.getLeft());
					numOps += 3;
				}
				node.demote();
				node = this.rotateRight(node);
				numOps += 2;
			}
			else if ((node.getLeftEdgeRank() == 2) && (node.getRightEdgeRank() == 0)){
				if ((node.getRight().getLeftEdgeRank() == 1) && (node.getRight().getRightEdgeRank() == 2)){
					node.getRight().demote();
					node.getRight().getLeft().promote();
					this.rotateRight(node.getRight());
					numOps += 3;
				}
				node.demote();
				node = this.rotateLeft(node);
				numOps += 2;
			}
			// After Deletion
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
