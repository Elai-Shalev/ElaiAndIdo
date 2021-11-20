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
	private int size = 0;

	public void printArray(int[] arr){
		for (int item :
				arr) {
			System.out.println(item);
		}
	}
	public boolean isEqualkeys(int[] keys){
		if(this.size!=keys.length){
			return false;
		}
		int [] them_keys = this.keysToArray();
		for(int i=0; i<this.size; i++){
			if(them_keys[i]!=keys[i]){
				return false;
			}
		}
		return true;

	}

  /**
   * public boolean empty()
   *
   * Returns true if and only if the tree is empty.
   *
   */
  public boolean empty() {
	  return this.root == null;
  }

 /**
   * public String search(int k)
   *
   * Returns the info of an item with key k if it exists in the tree.
   * otherwise, returns null.
   */
  public String search(int k)
  {
		IAVLNode curr = this.root;
		while (curr.isRealNode()){
			if (k == curr.getKey()){
				return curr.getValue();
			}
			else if (k < curr.getKey()){
				curr = curr.getLeft();
			}
			else {
				curr = curr.getRight();
			}
		}
		return null;
  }

  /**
   * public int insert(int k, String i)
   *
   * Inserts an item with key k and info i to the AVL tree.
   * The tree must remain valid, i.e. keep its invariants.
   * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
   * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
   * Returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String i) {
   		if (empty()){
   			this.root = new AVLNode(k, i, null);
			this.size++;
   			return 0;
	    }
   		IAVLNode curr = this.root;
   		while ((k < curr.getKey() && curr.getLeft().isRealNode()) ||
			   (k > curr.getKey() && curr.getRight().isRealNode())){
   			if (k < curr.getKey()){
				curr = curr.getLeft();
			}
   			else{
   				curr = curr.getRight();
			}
		}

	    if (k == curr.getKey()){
		    return -1;
	    }

	    if (k < curr.getKey()){
			curr.setLeft(new AVLNode(k, i, curr));
		}

	    if (k > curr.getKey()){
		    curr.setRight(new AVLNode(k, i, curr));
	    }

	    this.size++;
	    return this.rebalance(curr);
   }

  /**
   * public int delete(int k)
   *
   * Deletes an item with key k from the binary tree, if it is there.
   * The tree must remain valid, i.e. keep its invariants.
   * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
   * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
   * Returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k)
   {
	   if(empty()){
		   return 0;
	   }
	   int [] changes = new int[1];
	   this.root = deleteRec(k, root, changes);

	   updateHeight(node);
	   this.rebalance(node);


	   return 421;	// to be replaced by student code
   }

   public IAVLNode deleteRec(int k, IAVLNode curr, int [] changes) {

	   if (k < curr.getKey()) {
		   curr.setLeft(deleteRec(k, curr.getLeft(), changes));
	   } else if (k > curr.getKey()) {
		   curr.setRight(deleteRec(k, curr.getRight(), changes));
	   }
	   //now k is curr key

	   //is leaf
	   else if (!curr.getLeft().isRealNode() && !curr.getRight().isRealNode()) {
		   ExternalLeaf ex = new ExternalLeaf();
		   curr = ex;
	   }

	   //two children
	   else if(curr.getLeft().isRealNode() && curr.getRight().isRealNode()) {

		   IAVLNode succ = Successor(curr.getRight());
		   int succkey = succ.getKey();

		   curr.setRight(deleteRec(succkey, this.getRoot(),changes));
	   }

	   //one child
	   else if (!curr.getLeft().isRealNode()) {
		   curr = curr.getRight();
	   } else if (!curr.getRight().isRealNode()) {
		   curr = curr.getLeft();
	   }







		   //int heightDiff = root.heightDiff();

	   return 0;
	   }



   public IAVLNode Successor(IAVLNode node) {
	   if(!node.getRight().isRealNode()){
		   //minimumleaf
		   IAVLNode curr = node;
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

	  int[] keys = new int[size];
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

	private void updateHeight(IAVLNode node) {
		int leftHeight = node.getLeft().getHeight();
		int rightHeight = node.getRight().getHeight();
		node.setHeight(Math.max(leftHeight, rightHeight) + 1);
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
	  String[] values = new String[size];
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
	   return this.size;
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
    */   
   public int join(IAVLNode x, AVLTree t)
   {
	   return -1;
   }

   /**
	* updates the rooted_size field upwards in every node changed -
	* to usa after insert, delete and rebalance
    */
   public void Update(IAVLNode node){

   }


	/** isEqual
	 *
	 *
	 *
	 *
	 */


	/**
	 *  public IAVLNode rotateLeft
	 *
	 *  Rotates Node Left
	 */
	public IAVLNode rotateLeft(IAVLNode node){

		if(!node.isRealNode()){ return node;}

		IAVLNode new_mid = node.getRight();
		node.setRight(new_mid.getLeft());
		new_mid.setLeft(node);
		updateHeight(node);
		updateHeight(new_mid);

		return new_mid;

	}

	/**
	 *  public IAVLNode rotateLeft
	 *
	 *  Rotates Node Right
	 */
	public IAVLNode rotateRight(IAVLNode node) {
		IAVLNode new_mid = node.getLeft();
		node.setLeft(new_mid.getRight());
		new_mid.setRight(node);
		updateHeight(node);
		updateHeight(new_mid);

		return new_mid;
	}

	/**
	 *  public IAVLNode rotateLeft
	 *
	 *  Rebalances Tree
	 */
	public int rebalance(IAVLNode node){ return 0;}

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
		private int rooted_size;

  		public AVLNode(int key, String info, IAVLNode parent){
			this.key = key;
			this.info = info;
			this.parent = parent;
			this.height = 0;
			this.left = AVLTree.this.virtualNode;
			this.right = AVLTree.this.virtualNode;
			this.rooted_size = 0;
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
  			return this.right.getHeight() - this.left.getHeight();
		}
		public void updateHeight(){
  			this.setHeight(Math.max(this.left.getHeight(), this.right.getHeight()) + 1);
		}
		public int getRooted_size(){ return this.rooted_size;}
  }

	/**
	 *  public class ExternalLeaf
	 *
	 *  This class extends AVLNode and represents a Virtual Node, with height -1
	 */
	public class ExternalLeaf extends AVLNode{
  		public ExternalLeaf(){
  			super(-1, null, null);
  			this.setHeight(-1);
		}
  }
}
  
