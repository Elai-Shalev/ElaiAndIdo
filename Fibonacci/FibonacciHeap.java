package Fibonacci;

/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
    public int size = 0;
    public HeapNode min;
    public HeapNode first;
    public int marked;
    public static int totalCuts;
    public static int totalLinks;
    public int totalTrees;


   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *
    * Time Complexity: O(1)
    */
    public boolean isEmpty()
    {
    	return size == 0;
    }

    /**
     * private HeapNode insertForKMin(HeapNode node)
     *
     * Uses insert() to insert a new node with same key as given node.
     * Updates KMinPointer of the newNode to point to the original node that was given to the function
     * Used in KMin()
     *
     * Time Complexity: O(1)
     */
    private HeapNode insertForKMin(HeapNode node){
        HeapNode newNode = this.insert(node.key);
        newNode.KMinPointer = node;
        return newNode;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    *
    * Time Complexity: O(1)
    */
    public HeapNode insert(int key)
    {
        // Create new HeapNode with requested key
        HeapNode newNode = new HeapNode(key);
        // If heap is empty, set the min and first nodes of the heap as the new Node.
        // Also set node as its next and prev to maintain cyclic list property
        if(isEmpty()){
            min = newNode;
            first = newNode;
            newNode.next = newNode;
            newNode.prev = newNode;
        }
        // Else heap is not empty
        else {
            // Set pointers to insert node as leftmost node. After insert, first = new Node
            newNode.prev = first.prev;
            first.prev.next = newNode;
            newNode.next = first;
            first.prev = newNode;
            first = newNode;

            // If key is smaller than min's key then the new Node is the new heap minimum
            if (key < min.key) {
                min = newNode;
            }
        }

        // Increase size by 1 because a new node was added
        size++;

        // Increase number of trees by 1 because every new node is inserted as a new tree of degree 0
        totalTrees++;

        // return pointer to the newly created node
        return newNode;
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    * After Deletion, Consolidate forest with method Consolidate()
    *
    * Time Complexity: O(log(n)) Amortized, O(n) Worst-Case
    */
    public void deleteMin()
    {
     	// If heap is empty, do nothing and return because there is no Min to delete
        if (this.isEmpty()){
     	    return;
        }

        // Else heap is not empty. Min will be deleted
        // Create new pointer curr to iterate over min's children. Begin with curr = min.child
     	HeapNode curr = min.child;

        // If Min node as children
     	if (min.child != null){
     	    // For every child, set its parent as null because it will be inserted as root of a new tree
            // In accordance with above, increase tree size by 1
            do{
                this.totalTrees++;
                curr.parent = null;
                curr = curr.getNext();
            }
            while (curr != min.child);

            // Insert children to list
            // If Min is not the only tree root in the heap
            if (min.next != min){
                // If min has only 1 child, Insert it between Min's prev and next
                if (min.child.next == min.child){
                    min.prev.next = min.child;
                    min.child.prev = min.prev;

                    min.next.prev = min.child;
                    min.child.next = min.next;
                }
                // Else min has more than 1 child. Insert them between Min's prev and next
                else {
                    min.child.prev.next = min.next;
                    min.next.prev = min.child.prev;

                    min.child.prev = min.prev;
                    min.prev.next = min.child;
                }
            }

            // If min was the first node in heap, set Min's leftmost child as first
            if (min == first){
                first = min.child;
            }
        }
     	// Else Min has no children
     	else{
     	    // Connect Min's prev and next together so Min is no longer between them
     	    min.prev.next = min.next;
     	    min.next.prev = min.prev;

            // If min was the first node in heap, set Min's next neighbour as first
            if (min == first){
                first = min.next;
            }
        }

     	// Set Min's child as null to disconnect it from its children
     	min.child = null;

     	// Decrease size of heap by 1 because the Min node was deleted
     	this.size--;

     	// Decrease number of trees by 1 because the tree containing the Min was deleted
     	this.totalTrees--;

        // If heap is not empty
     	if (!this.isEmpty()){
            // Find the new Minimum because the old one was deleted
            // Temporarily set the first node as the new Minimum
            HeapNode newMin = first;
            curr = first;
            // Iterate over all nodes in list
            do{
                // If current node key is smaller than newMin key, set current node as newMin
                if (curr.getKey() < newMin.getKey()){
                    newMin = curr;
                }
                curr = curr.getNext();
            }
            while (curr != first);

            // After iterating over list, newMin now holds the correct Min node of the tree. Set it as min
            this.min = newMin;

            // Consolidate list to iteratively link trees of same degree to decrease forest size
            this.Consolidate();
        }
     	// Else min is empty. Set min and first as null
        else{
            min = null;
            first = null;
        }
    }

   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    * Time Complexity: O(1)
    */
    public HeapNode findMin()
    {
        // If tree is empty return null. Else return the minimum node of the tree, saved in min
        if(isEmpty()) { return null;}
        return min;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    * Time Complexity: O(1)
    */
    public void meld (FibonacciHeap heap2)
    {
        // If heap2 is empty, do nothing and return. Forest will be unchanged
        if (heap2.isEmpty()){
            return;
        }

        // Else heap2 is not empty
        // Add heap2 totalTrees and marked to this heap's field values
        this.totalTrees += heap2.totalTrees;
        this.marked += heap2.marked;

        // If heap is empty, change this heap's min, first and size to be the same as heap2, and return
        if(isEmpty()){
            this.first = heap2.first;
            this.min = heap2.min;
            this.size = heap2.size;
            return;
        }

        // Else both heaps are not empty
        // Add heap2 size this heap's field values
        this.size += heap2.size;

        // Connect heap2 list at the end of this heap (this heap's last node with heap2 first node)
        HeapNode last = first.prev;
        first.prev = heap2.first.prev;
        first.prev.next = first;
        last.next = heap2.first;
        heap2.first.prev = last;

        // Choose the min of the Unified heap from the 2 previous min's of the melded heaps
        if(min.key < heap2.min.key){
            min = heap2.min;
        }
    }

    /**
     * public void InsertTreefromLeft(HeapNode x)
     *
     * inserts a node x as a root of a new tree to the left of the list, after it is cut from a tree by cut() method
     *
     * Time Complexity: O(1)
     */
    public void InsertTreefromLeft(HeapNode x){
        // Update pointers to set x as the first node of the forest list
        first.prev.next = x;
        x.prev = first.prev;
        first.prev = x;
        x.next = first;
        first = x;

        // Increase number of trees by 1 because x was added as a new tree to the forest
        totalTrees++;
    }

    /**
     * public void InsertChainFromLeft(HeapNode x, int chainLength)
     *
     * inserts a chain of nodes in the right order after cascading cut
     *
     * Time Complexity: O(1)
     */
    public void InsertChainFromLeft(HeapNode x, int chainLength){

        // Connect the end of the chain starting from x, to the beginning of the current tree list - first
        HeapNode heaplast = this.first.prev;
        HeapNode chainlast = x.prev;
        heaplast.next = x;
        x.prev = heaplast;
        first.prev = chainlast;
        chainlast.next = first;

        // Set x as the new first node
        first = x;

        // Add length of added chain to the total number of trees
        totalTrees += chainLength;
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *
    * Time Complexity: O(1)
    */
    public int size()
    {
    	return this.size;
    }

   /**
    * public static int log2(int x)
    *
    * Returns log in base 2 of x
    *
    * Time Complexity: O(1)
    */
    public static int log2(int x) {
        return (int) (Math.log(x) / Math.log(2));
    }

    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * Note: The size of of the array depends on the maximum order of a tree, and an empty heap returns an empty array.
    *
    * Time Complexity: O(Number of Trees), Worst Case: O(n)
    */
    public int[] countersRep()
    {
        // If tree is empty return empty array
        if (this.isEmpty()){
            return new int[0];
        }

        // Else tree is not empty
        // Initialize array, arr, with size of maximum possible rank + 1
        // In a Fibonacci Heap, maxRank <= log_phi(size) ~ 1.44*log2(size) < 2 * log2(size) + 1
    	int[] arr = new int[2*log2(this.size)+1];

        // Set maxRank as rank of first node. When iterating through list will be changed the true maximum Rank
        int maxRank = this.first.getRank();

        // Add 1 tree of the first's rank to the array
        arr[this.first.getRank()]++;

        // Declare pointer curr to next node of first
    	HeapNode curr = this.first.getNext();

    	// While there are still nodes in the list, i.e. current node is not the first node
    	while (curr != this.first){
    	    // If current rank is bigger than maxRank, set maxRank as current rank
    	    if (curr.getRank() > maxRank){
    	        maxRank = curr.getRank();
            }
            // Add 1 tree of the current rank to the array
            arr[curr.getRank()]++;

    	    // Move curr pointer to next node
            curr = curr.getNext();
        }

    	// Initialize result array, res, with size of true maximum rank + 1 (because the ranks start from 0)
        int[] res = new int[maxRank + 1];

    	// Copy arr into res
    	for (int i = 0; i < res.length; i++){
    	    res[i] = arr[i];
        }

    	// Return result
    	return res;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    * Time Complexity: Amortized: O(log(n)), Worst-Case: O(n) (Same as deleteMin())
    */
    public void delete(HeapNode x) 
    {    
    	// Call decreaseKeyInternal with boolean value true, to cut x from tree regardless of the key difference
        this.decreaseKeyInternal(x, 0, true);
        // Set x as if it is the minimum node, so as to be deleted by deleteMin()
        this.min = x;
        // Delete the "minimum node" (actually x) from the tree.
        // After deletion, deleteMin() will also find the "new" actual min
        this.deleteMin();
    }

    /**
     * public void decreaseKey(HeapNode x, int delta)
     *
     * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
     * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
     *
     * Time Complexity: O(Number of Cuts), Amortized: O(1)
     */
    public void decreaseKey(HeapNode x, int delta){
        this.decreaseKeyInternal(x, delta, false);
    }

    /**
     * private void decreaseKeyInternal(HeapNode x, int delta, boolean isInternal)
     *
     * Used by decreaseKey() with boolean value false to indicate regular decreaseKey as studied
     * When used by delete() operates with boolean value true in order to always cut it from the tree
     *
     * Time Complexity: O(Number of Cuts), Amortized: O(1)
     */
    private void decreaseKeyInternal(HeapNode x, int delta, boolean isInternal)
    {
        //decrease
        x.key = x.key-delta;
        if(x.key < min.key){
            min = x;
        }

        //handle violation - assert x isn't root.
        if(x.parent!=null && (isInternal || x.key < x.getParent().key)) {
            //case1: parent of x is unmarked
            if (!x.parent.marked) {
                if (x.parent.parent != null) { //parent isn't root
                    x.parent.marked = true;
                    marked++;
                }
                cut(x); //cut off x between its parent and siblings
                InsertTreefromLeft(x);
            }
            //case2: parent of x is marked
            else {
                cascadingCut(x);
            }
        }

    }

    /**
     * public void cut(HeapNode x)
     *
     * Cuts node x from its parent
     *
     * Time Complexity: O(1)
     */
    public void cut(HeapNode x){
        // If node is marked, unmark it and decrease number of marked nodes
        if (x.marked){
            x.marked = false;
            this.marked--;
        }

        // Decrease parent rank by 1, because we are cutting one of its children (Rank = Number of children)
        HeapNode parent = x.parent;
        parent.rank--;

        // If x is the leftmost child of its parent
        if(parent.child == x){
            // If x is the only child, set the parent's child as null, since it now has no children
            if(x.next == x){
                parent.child =null;
            }
            // Else x's parent has more children. Set its leftmost child as the next node after x
            else {
                parent.child = x.next;
            }
        }
        // Set x's parent as null since it will become a new root
        x.parent = null;

        // Connect the neighboring nodes of x, x's prev and x's next, together
        x.next.prev = x.prev;
        x.prev.next = x.next;

        // Increase number of cuts by 1, because we made a new cut
        totalCuts++;
    }

    /**
     * public void cascadingCut(HeapNode x)
     *
     * Iteratively cuts node from x and all of its marked ancestors
     * After cutting all marked in path to root, marks the unmarked parent if its not a root
     * Lastly, callls InsertChainFromLeft() to insert the formed sub-list to the left of the forest list
     *
     * Time Complexity: O(Number of Cuts)
     */
    public void cascadingCut(HeapNode x){

        // Create new chain Head and Last that will later be inserted to the left of the list by InsertChainFromLeft()
        HeapNode chainHead = x;
        HeapNode chainLast = x;
        HeapNode parent = x.parent;
        // If x is unmarked, mark it and increase marked by 1
        if (!x.marked){
            x.marked = true;
            marked++;
        }

        int chainLength =0;
        // While current node (x) is marked
        while(x.isMarked()){ //there are cuts to be performed
            // Cut current node (x)
            cut(x);
            // Update chain - insert current node at the end of the chain
            chainLast.next = x;
            x.next = chainHead;
            chainHead.prev = x;
            x.prev = chainLast;
            chainLast = x;
            chainLength++;

            // Update current node (x) and its parent to traverse up the tree
            x = parent;
            parent = x.parent;
        }

        // Here current node is no longer marked, so no more cutting is to be made
        // If current node is not a root, i.e. its parent != null, mark it and increase number of marked nodes
        if(parent != null){
            x.marked = true;
            marked++;
        }

        // After cascading-cut is complete, insert the assembled chain to the left of the forest list
        InsertChainFromLeft(chainHead, chainLength);
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * 
    * In words: The potential equals to the number of trees in the heap
    * plus twice the number of marked nodes in the heap.
    *
    * Time Complexity: O(1)
    */
    public int potential() 
    {
        return this.totalTrees + 2*this.marked;
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    *
    * Time Complexity: O(1)
    */
    public static int totalLinks()
    {    
    	return totalLinks;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods).
    *
    * Time Complexity: O(1)
    */
    public static int totalCuts()
    {    
    	return totalCuts;
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H.
    *
    * Time Complexity: O(k*deg(H))
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    
        // If k is 0, return empty list
        if (k == 0){
            return new int[0];
        }

        // Else k is not 0
        // Initialize new result array of length k
        int[] result = new int[k];

        // Define binary heap to keep track of next minimum
        FibonacciHeap fibHeap = new FibonacciHeap();

        // Insert the minimum of H for it should be the first in result array
        fibHeap.insertForKMin(H.min);
        HeapNode curr;

        // Insert k smallest elements
        for (int i = 0; i < k; i++){
            // Get next minimum from binary heap and insert to result
            curr = fibHeap.min.KMinPointer;
            result[i] = curr.getKey();

            // Insert the minimum's children to the binary heap
            HeapNode child = curr.getChild();
            if (child != null){
                do{
                    fibHeap.insertForKMin(child);
                    child = child.getNext();
                }
                while (child != curr.getChild());
            }

            fibHeap.deleteMin();
        }

        return result;
    }

    /**
     * public void Consolidate()
     *
     * Performed after DeleteMin(). Iterates over current tree list and consolidates to make a new list
     *
     * Time Complexity: O(Number of Trees). Worst Case: O(n)
     */
    public void Consolidate(){
        // If tree is not empty
        if (!this.isEmpty()){
            // Initialize bucket array arr, with size of the maximum possible rank + 1
            // In a Fibonacci Heap, maxRank <= log_phi(size) ~ 1.44*log2(size) < 2 * log2(size) + 1
            HeapNode[] arr = new HeapNode[2*log2(this.size)+1];

            // Initialize pointer curr to start with first node in forest
            HeapNode curr = this.first;
            // Insert curr to cell of index rank(curr) in bucket array
            arr[curr.getRank()] = curr;
            // Move pointer from first to next node
            curr = curr.getNext();

            // Initialize pointer next. Will be used as next node in list, so pointer doesn't "get lost" during link
            HeapNode next;
            // Initialize newTree pointer. Will be used for iterative linking
            HeapNode newTree;

            // While there are still nodes in the list, i.e. current node is not the first node
            while(curr != this.first){
                // Set next as the node after current node
                next = curr.getNext();
                // If cell of index rank(current) is already occupied in array
                if (arr[curr.getRank()] != null){
                    // Set newTree as current tree to be linked to the tree of same rank already in bucket array
                    newTree = curr;
                    // While cell of index rank(current) is already occupied in array
                    while(newTree.getRank() < arr.length && arr[newTree.getRank()] != null){
                        // Link newTree with tree of same rank
                        newTree = this.link(newTree, arr[newTree.getRank()]);
                        // Set the index of the previously connected tree as null, to vacant the cell for future trees
                        arr[newTree.getRank()-1] = null;
                    }
                    // Here there is no longer a tree of the same rank as newTree. Insert it in bucket array
                    arr[newTree.getRank()] = newTree;
                }
                // Else cell of index rank(current) is NOT occupied in array
                else{
                    // Insert current node into bucket array
                    arr[curr.getRank()] = curr;
                }
                // Move to next node in forest
                curr = next;
            }

            // Move trees from bucket array to the forest list
            // Set first as default null
            first = null;
            // For every cell in bucket array
            for (int i = 0; i < arr.length; i++){
                // If cell is not empty
                if (arr[i] != null){
                    // If first is null, then set the tree in the current cell, which of smallest degree, as first
                    if (first == null){
                        first = arr[i];
                        first.prev = first;
                        first.next = first;
                    }
                    // Else first is not null
                    else{
                        // Insert tree in current cell to the right of the forest list
                        first.prev.next=arr[i];
                        arr[i].prev = first.prev;
                        arr[i].next = first;
                        first.prev = arr[i];
                    }
                }
            }
        }
    }

    /**
     * public void link()
     *
     * Links 2 nodes by making the maxmimum of the two, the left child of the minimum. Uses linkLeft func of HeapNode
     *
     * Time Complexity: O(1)
     */
    public HeapNode link(HeapNode a, HeapNode b){
        // Increase total links by 1
        this.totalLinks++;
        // Decrease total trees by 1, since we are linking 2 trees into 1
        this.totalTrees--;

        // If a's key is bigger than b's
        if (a.getKey() > b.getKey()){
            // b should be the new root of the linked tree. Link a as the leftmost child of b
            b.linkLeft(a);
            // Return b, the new root of the linked tree
            return b;
        }
        // Else b's key is bigger than a's
        // a should be the new root of the linked tree. Link b as the leftmost child of a
        a.linkLeft(b);
        // Return a, the new root of the linked tree
        return a;
    }

    /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file. 
    *
    * Class representing the node elements contained in a Fibonacci Heap
    */
    public static class HeapNode {

       public int key;
       public int rank;
       public boolean marked;
       public HeapNode child;
       public HeapNode next;
       public HeapNode prev;
       public HeapNode parent;
       public HeapNode KMinPointer;

      /**
       * public HeapNode(int key)
       * Constructor for a new Heapnode with requested key
       * Time Complexity: O(1)
       */
       public HeapNode(int key) {
           this.key = key;
       }

      /**
       * public int getKey()
       * Returns key of node
       * Time Complexity: O(1)
       */
       public int getKey() {
           return this.key;
       }

      /**
       * public int getRank()
       * Returns rank of node
       * Time Complexity: O(1)
       */
       public int getRank() {return this.rank;};

      /**
       * public boolean isMarked()
       * Returns true if and only if node is marked
       * Time Complexity: O(1)
       */
       public boolean isMarked(){ return this.marked;}

      /**
       * public HeapNode getChild()
       * Returns leftmost child of node
       * Time Complexity: O(1)
       */
       public HeapNode getChild(){ return this.child;}

      /**
       * public HeapNode getNext()
       * Returns next sibling of this node
       * Time Complexity: O(1)
       */
       public HeapNode getNext(){ return this.next;}

      /**
       * public HeapNode getPrev()
       * Returns previous sibling of this node
       * Time Complexity: O(1)
       */
       public HeapNode getPrev(){ return this.prev;}

      /**
       * public HeapNode getParent()
       * Returns parent of node
       * Time Complexity: O(1)
       */
       public HeapNode getParent(){ return this.parent;}

      /**
       * public void linkLeft(HeapNode node)
       * Links the given node as the leftmost child of this node
       * Time Complexity: O(1)
       */
       public void linkLeft(HeapNode node){
           // Set this node as the parent of the given node
           node.parent = this;
           // If this node already has children
           if (this.child != null){
               // Connect node to its new siblings, while maintaining cyclic list property
               node.next = this.child;
               this.child.prev.next = node;
               node.prev = this.child.prev;
               this.child.prev = node;
           }
           // Else this node has no children
           else{
               // Then given node will be the first and only child of this node
               // Set the given node as its own prev and next to maintain cyclic list property
               node.next = node;
               node.prev = node;
           }
           // Set the given node as this node's leftmost child
           this.child = node;
           // Increase node's rank by 1, since rank = Number of Children
           this.rank++;
       }
   }
}
