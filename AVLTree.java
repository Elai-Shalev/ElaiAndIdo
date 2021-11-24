/**
 *
 * AVLTree
 *
 * An implementation of aמ AVL Tree with
 * distinct integer keys and info.
 *
 */
public class AVLTree {
	private final ExternalLeaf virtualNode = new ExternalLeaf();
	public IAVLNode root;
	static final int COUNT = 10;

	public void printArray(int[] arr) {
		for (int item :
				arr) {
			System.out.print(item);
		}
	}
	public boolean isEqualkeys(int[] keys) {
		if (this.size() != keys.length) {
			return false;
		}
		int[] them_keys = this.keysToArray();
		for (int i = 0; i < this.size(); i++) {
			if (them_keys[i] != keys[i]) {
				return false;
			}
		}
		return true;

	}
	/**
	 * public boolean empty()
	 * <p>
	 * Returns true if and only if the tree is empty.
	 */
	public boolean empty() {
		return this.root == null;
	}
	/**
	 * public String search(int k)
	 * <p>
	 * Returns the info of an item with key k if it exists in the tree.
	 * otherwise, returns null.
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

	public String search(int k) {
		return searchNode(k).getValue();
	}

	/**
	 * public int insert(int k, String i)
	 * <p>
	 * Inserts an item with key k and info i to the AVL tree.
	 * The tree must remain valid, i.e. keep its invariants.
	 * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
	 * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
	 * Returns -1 if an item with key k already exists in the tree.
	 */
	public int insert(int k, String i) {
		if (empty()) {
			this.root = new AVLNode(k, i, null);
			return 0;
		}
		IAVLNode curr = this.root;
		while ((k < curr.getKey() && curr.getLeft().isRealNode()) ||
				(k > curr.getKey() && curr.getRight().isRealNode())) {
			if (k < curr.getKey()) {
				curr = curr.getLeft();
			} else {
				curr = curr.getRight();
			}
		}

		if (k == curr.getKey()) {
			return -1;
		}

		if (k < curr.getKey()) {
			curr.setLeft(new AVLNode(k, i, curr));
		}

		if (k > curr.getKey()) {
			curr.setRight(new AVLNode(k, i, curr));
		}

		return this.rebalance(curr);
	}

	/**
	 * public int delete(int k)
	 * <p>
	 * Deletes an item with key k from the binary tree, if it is there.
	 * The tree must remain valid, i.e. keep its invariants.
	 * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
	 * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
	 * Returns -1 if an item with key k was not found in the tree.
	 */
	public int delete(int k) {
		if (empty()) {
			return 0;
		}
		IAVLNode curr = searchNode(k);
		IAVLNode succ = null;

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
			//succ = Successor(curr.getRight());
			succ =  Successor(curr);
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
			succ.setLeft(curr.getLeft());
			succ.setRight(curr.getRight());
			curr.getRight().setParent(succ);
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
		}

		//one child
		else if (!curr.getLeft().isRealNode()) {
			curr.getRight().setParent(curr.getParent());
			if (curr.getParent() != null) {
				curr.getParent().setRight(curr.getRight());
			}
			else{
				this.root = curr.getRight();
			}
		} else  {
			curr.getLeft().setParent(curr.getParent());
			if (curr.getParent() != null) {
				curr.getParent().setLeft(curr.getLeft());
			}
			else{
				this.root = curr.getLeft();
			}
		}

		return rebalance(succ);
	}

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
	* Time Complexity: O(log(n)) with n being No. of nodes in Tree.
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
   */
  public int[] keysToArray(){

	  int[] keys = new int[size()];
	  int[] index = new int[]{0};
	  inOrderKeys(this.root, keys,index);
	  return keys;
  }
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
   */
  public String[] infoToArray()
  {
	  String[] values = new String[size()];
	  int[] index = new int[]{0};
	  inOrderVals(this.root, values ,index);
	  return values;
  }
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
    */
   public int size()
   {
	   return this.root.getSize();
   }
   /**
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
	* TC: O(1)
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
    */   
   public AVLTree[] split(int x)
   {
	   return null; 
   }
   
   /**
    * public int join(IAVLNode x, AVLTree t)
    *
    * joins t and x with the tree. 	
    * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
	*
	* precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
    * postcondition: none
	*
    */   
   public int join(IAVLNode x, AVLTree t) {
	   if (empty() || t.empty()) {
		   return 1;
	   }
	   int valuetoreturn = Math.abs(this.root.getHeight() - t.root.getHeight())+1;
	   //CASE 1: if this.tree is shorter than t
	   if (x.getKey() > this.getRoot().getKey()) {


		   //find b
		   int a_height = this.getRoot().getHeight();

		   IAVLNode b = t.getRoot();
		   while (b.getHeight() >= a_height) {
			   b = b.getLeft();
		   }
		   IAVLNode c = b.getParent();
		   x.setParent(c);
		   x.setRight(b);
		   x.setLeft(this.getRoot());
		   this.root.setParent(x);
		   b.setParent(x);
		   c.setLeft(x);
		   //chaning the root - now one tree
		   //need to check - what is X is the root?
		   this.root = t.root;
		   rebalance(c);
		   rebalance(x);
		   rebalance(b);
		   return valuetoreturn;
	   } else {
		   //case 2: if this.tree is taller than t
		   int a_height = t.getRoot().getHeight();
		   IAVLNode b = t.getRoot();
		   while (b.getHeight() >= a_height) {
			   b = b.getRight();
		   }
		   IAVLNode c = b.getParent();
		   x.setParent(c);
		   x.setLeft(b);
		   x.setRight(this.getRoot());
		   this.root.setParent(x);
		   b.setParent(x);
		   c.setRight(x);
		   //chaning the root - now one tree
		   //need to check - what is X is the root?
		   this.root = t.root;
		   rebalance(c);
		   rebalance(x);
		   rebalance(b);
		   return valuetoreturn;

	   }

   }

	/**
	 *  public IAVLNode rotateLeft
	 *
	 *  Rotates Node Left
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
		node.updateHeight();
		new_mid.updateHeight();
		if (new_mid.getParent() == null) {
			this.root = new_mid;
		}
		node.updateSize();
		new_mid.updateSize();
		return new_mid;
	}

	/**
	 *  public IAVLNode rotateLeft
	 *
	 *  Rotates Node Right
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
		node.updateHeight();
		new_mid.updateHeight();
		if(new_mid.getParent() == null){
			this.root = new_mid;
		}
		node.updateSize();
		new_mid.updateSize();
		return new_mid;
	}


	// Wrapper over print2DUtil()

	static void print2DUtil(IAVLNode root, int space)
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
	static void print2D(IAVLNode root)
	{
		System.out.println("_______________");
		// Pass initial space count as 0

		print2DUtil(root, 0);
	}

	/**
	 *  public IAVLNode rotateLeft
	 *
	 *  Rebalances Tree
	 */
	public int rebalance(IAVLNode node){

		int numOps = 0;

		while(node != null){
			node.updateSize();
			numOps += node.updateHeight();
			if (node.heightDiff() > 1){
				if (node.getLeft().heightDiff() == -1){
					this.rotateLeft(node.getLeft());
					numOps++;
				}
				node = this.rotateRight(node);
			}
			else if (node.heightDiff() < -1){
				if (node.getRight().heightDiff() == 1){
					this.rotateRight(node.getRight());
					numOps++;
				}
				node = this.rotateLeft(node);
			}
			node = node.getParent();
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
    	public void setHeight(int height); // Sets the height of the node.
    	public int getHeight(); // Returns the height of the node (-1 for virtual nodes).
		public int updateHeight(); //Updates the height of the current node based on its children
		public int heightDiff(); // Returns right child height - left child height;
		public int getSize(); // Returns size of sub-tree
		public void updateSize(); // Updates size of tree: left.size + right.size + 1
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
  		private int height;
		private int size;

  		public AVLNode(int key, String info, IAVLNode parent){
			this.key = key;
			this.info = info;
			this.parent = parent;
			this.height = 0;
			this.left = AVLTree.this.virtualNode;
			this.right = AVLTree.this.virtualNode;
			this.size = 1;
		}

		public int getKey()
		{
			return this.key;
		}
		public String getValue()
		{
			return this.info;
		}
		public void setKey(int key) {this.key = key;}
		public void setLeft(IAVLNode node)
		{
			this.left = node;
		}
		public IAVLNode getLeft()
		{
			return this.left;
		}
		public void setRight(IAVLNode node)
		{
			this.right = node;
		}
		public IAVLNode getRight()
		{
			return this.right;
		}
		public void setParent(IAVLNode node)
		{
			this.parent = node;
		}
		public IAVLNode getParent()
		{
			return this.parent;
		}
		public boolean isRealNode()
		{
			return this.height > -1;
		}
	    public void setHeight(int height)
	    {
	      	this.height = height;
	    }
	    public int getHeight()
	    {
	     	return this.height;
	    }
	    public int heightDiff(){
  			return this.left.getHeight() - this.right.getHeight();
		}
		public int updateHeight() {
			int leftHeight = this.getLeft().getHeight();
			int rightHeight = this.getRight().getHeight();
			int neuheight = Math.max(leftHeight, rightHeight) + 1;
			if (neuheight != this.getHeight()) {
				this.setHeight(neuheight);
				return 1;
			}
			return 0;
		}

		public int getSize(){
  			return this.size;
		}

		public void updateSize(){
  			this.size = this.left.getSize() + this.right.getSize() + 1;
		}
  }

	/**
	 *  public class ExternalLeaf
	 *
	 *  This class extends AVLNode and represents a Virtual Node, with height -1
	 */
	public class ExternalLeaf extends AVLNode {
		public ExternalLeaf() {
			super(-1, null, null);
			this.setHeight(-1);
			super.size = 0;
		}

		@Override
		public int heightDiff() {
			return 0;
		}
	}

  }

  
