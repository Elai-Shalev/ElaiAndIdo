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
    */
    public boolean isEmpty()
    {
    	return size == 0;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    */
    public HeapNode insert(int key)
    {
        HeapNode newNode = new HeapNode(key);
        if(isEmpty()){
            min = newNode;
            first = newNode;
            newNode.next = newNode;
            newNode.prev = newNode;
        }
        else {
            //5 pointer updates
            newNode.prev = first.prev;
            first.prev.next = newNode;
            newNode.next = first;
            first.prev = newNode;
            first = newNode;
            //is new minimum?
            if (key < min.key) {
                min = newNode;
            }
        }
        size++;
        totalTrees++;
        return newNode;
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
     	if (totalTrees == 0){
     	    return;
        }

        // Disconnect children from parent
     	HeapNode curr = min.child;
     	if (min.child != null){
            do{
                this.totalTrees++;
                curr.parent = null;
                curr = curr.getNext();
            }
            while (curr != min.child);

            // Insert children to list
            if (min.next != min){
                if (min.child.next == min.child){
                    min.prev.next = min.child;
                    min.child.prev = min.prev;

                    min.next.prev = min.child;
                    min.child.next = min.next;
                }
                else {
                    min.child.prev.next = min.next;
                    min.next.prev = min.child.prev;

                    min.child.prev = min.prev;
                    min.prev.next = min.child;
                }
            }

            // Change first if necessary
            if (min == first){
                first = min.child;
            }
        }
     	else{
     	    min.prev.next = min.next;
     	    min.next.prev = min.prev;

            // Change first if necessary
            if (min == first){
                first = min.next;
            }
        }

     	min.child = null;

     	// Decrease size of heap and num trees
     	this.size--;
     	this.totalTrees--;

        if (this.size > 0){
            // Find new min
            HeapNode newMin = first;
            curr = first;
            do{
                if (curr.getKey() < newMin.getKey()){
                    newMin = curr;
                }
                curr = curr.getNext();
            }
            while (curr != first);

            this.min = newMin;

            // Consolidate
            this.Consolidate();
        }
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
    */
    public HeapNode findMin()
    {
        if(isEmpty()) { return null;}
        return min;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    */
    public void meld (FibonacciHeap heap2)
    {
        if(isEmpty()){
            this.first = heap2.first;
            this.min = heap2.min;
        }
        else{
            HeapNode last = first.prev;
            first.prev = heap2.first.prev;
            first.prev.next = first;
            last.next = heap2.first;
            heap2.first.prev = last;
        }
        if(min.key < heap2.min.key){
            min = heap2.min;
        }

        totalTrees+= heap2.totalTrees;
        this.size = this.size + heap2.size;
    }

    /**inserts a tree from the left to the first layer
    ** after cut() is called
     * //*/

    public void InsertTreefromLeft(HeapNode x){
        //update pointers
        first.prev.next = x;
        x.prev = first.prev;
        first.prev = x;
        x.next = first;
        first =x;
        totalTrees++;
    }

    /**
     * inserts a chain of nodes in the right order
     * after cut is called in cascading context
     *
     */
    public void InsertChainFromLeft(HeapNode x, int chainLength){

        HeapNode heaplast = this.first.prev;
        HeapNode chainlast = x.prev;
        heaplast.next = x;
        x.prev = heaplast;
        first.prev = chainlast;
        chainlast.next = first;
        first = x;

        totalTrees += chainLength;
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *   
    */
    public int size()
    {
    	return this.size;
    }

   /**
    * public static int log2(int x)
    *
    * Returns log in base 2 of x
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
    */
    public int[] countersRep()
    {
        if (this.isEmpty()){
            return new int[0];
        }

    	int[] arr = new int[log2(this.size) + 1];
        int maxRank = this.first.getRank();

        arr[this.first.getRank()]++;
    	HeapNode curr = this.first.getNext();

    	while (curr != this.first){
    	    if (curr.getRank() > maxRank){
    	        maxRank = curr.getRank();
            }
            arr[curr.getRank()]++;
            curr = curr.getNext();
        }

        int[] res = new int[maxRank + 1];
    	for (int i = 0; i < res.length; i++){
    	    res[i] = arr[i];
        }
    	return res;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    */
    public void delete(HeapNode x) 
    {    
    	this.decreaseKeyInternal(x, 0, true);
        this.min = x;
        this.deleteMin();
    }

    /**
     * public void decreaseKey(HeapNode x, int delta)
     *
     * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
     * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
     */
    public void decreaseKey(HeapNode x, int delta){
        this.decreaseKeyInternal(x, delta, false);
    }

    /**
     * private void decreaseKeyInternal(HeapNode x, int delta, boolean isInternal)
     *
     * Used by decreaseKey() with boolean value false to indicate regular decreaseKey as studied
     * When used by delete() operates with boolean value true in order to always cut it from the tree
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

    public void cut(HeapNode x){
        HeapNode parent = x.parent;

        //detaching from parent
        if(parent.child == x){
            if(x.next == x){
                parent.child =null;
            }
            else {
                parent.child = x.next;
            }
        }
        x.parent = null;
        //detaching from siblings
        x.next.prev = x.prev;
        x.prev.next = x.next;
        totalCuts++;
    }

    public void cascadingCut(HeapNode x) {

        HeapNode chainHead = x;
        HeapNode chainLast = x;
        HeapNode parent = x.parent;
        x.marked = true;
        marked++;

        int chainLength =0;
        while(x.isMarked()){ //there are cuts to be performed
            cut(x);
            x.marked = false;
            marked--;
            //update chain
            chainLast.next = x;
            x.next = chainHead;
            chainHead.prev = x;
            x.prev = chainLast;
            chainLast = x;
            chainLength++;
            //move up
            x = parent;
            parent = x.parent;
        }
        if(parent != null){
            x.marked = true;
            marked++;
        }
        //after chain is done
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
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    
        if (k == 0){
            return new int[0];
        }

        int[] result = new int[k];

        // Define binary heap to keep track of next minimum
        BinaryHeap binHeap = new BinaryHeap();

        // Insert the minimum of H for it should be the first in result array
        binHeap.insert(H.min);
        HeapNode curr;

        // Insert k smallest elements
        for (int i = 0; i < k; i++){
            // Get next minimum from binary heap and insert to result
            curr = binHeap.deleteMin();
            result[i] = curr.getKey();

            // Insert the minimum's children to the binary heap
            HeapNode child = curr.getChild();
            if (child != null){
                do{
                    binHeap.insert(child);
                    child = child.getNext();
                }
                while (child != curr.getChild());
            }
        }

        return result;
    }

    /**
     * public void Consolidate()
     *
     * Performed after DeleteMin(). Iterates over current tree list and consolidates to make a new list
     */
    public void Consolidate(){
        if (!this.isEmpty()){
            HeapNode[] arr = new HeapNode[2*log2(this.size)+1];
            HeapNode right = null;
            HeapNode left = null;

            HeapNode curr = this.first;
            arr[curr.getRank()] = curr;
            curr = curr.getNext();

            HeapNode next;
            HeapNode newTree;

            while(curr != this.first){
                next = curr.getNext();
                if (arr[curr.getRank()] != null){
                    newTree = curr;
                    while(newTree.getRank() < arr.length && arr[newTree.getRank()] != null){
                        newTree = this.link(newTree, arr[newTree.getRank()]);
                        arr[newTree.getRank()-1] = null;
                    }
                    arr[newTree.getRank()] = newTree;
                    curr = next;
                }
                else{
                    arr[curr.getRank()] = curr;
                    curr = curr.getNext();
                }
            }

            first = null;
            for (int i = 0; i < arr.length; i++){
                if (arr[i] != null){
                    if (first == null){
                        first = arr[i];
                        first.prev = first;
                        first.next = first;
                    }
                    else{
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
     */
    public HeapNode link(HeapNode a, HeapNode b){
        this.totalLinks++;
        this.totalTrees--;

        if (a.getKey() > b.getKey()){
            b.linkLeft(a);
            return b;
        }
        a.linkLeft(b);
        return a;
    }

    /**
     * public static class BinaryHeap
     */

    public static class BinaryHeap {
        public HeapNode[] arr = new HeapNode[2];
        public int size = 0;

        public void insert(HeapNode k){
            this.size++;
            this.arr[this.size] = k;
            this.HeapifyUp(this.size);
            this.checkAndDouble();
        }

        public HeapNode getMin(){
            return this.arr[1];
        }

        public HeapNode deleteMin(){
            HeapNode min = this.arr[1];
            this.arr[1] = this.arr[size];
            this.size--;
            this.HeapifyDown(1);
            return min;
        }

        public void HeapifyUp(int i){
            if (size > 1){
                while (i > 1 && this.arr[i / 2].getKey() > this.arr[i].getKey()){
                    this.SwitchIdxs(i / 2, i);
                    i = i / 2;
                }
            }
        }

        public void HeapifyDown(int i){
            if (size > 1){
                while ((i * 2 < size && this.arr[i * 2].getKey() < this.arr[i].getKey()) ||
                        (i * 2 + 1 < size && this.arr[i * 2 + 1].getKey() < this.arr[i].getKey())){
                    if (i * 2 < size && i * 2 + 1 < size){
                        int smallest = Math.min(this.arr[i * 2].getKey(), this.arr[i * 2 + 1].getKey());
                        if (this.arr[i * 2].getKey() == smallest){
                            this.SwitchIdxs(i * 2, i);
                            i = i * 2;
                        }
                        else{
                            this.SwitchIdxs(i * 2 + 1, i);
                            i = i * 2 + 1;
                        }
                    }
                    else if (i * 2 < size){
                        this.SwitchIdxs(i * 2, i);
                        i = i * 2;
                    }
                    else{
                        this.SwitchIdxs(i * 2 + 1, i);
                        i = i * 2 + 1;
                    }
                }
            }
        }

        public void SwitchIdxs(int i, int j){
            HeapNode temp = this.arr[i];
            this.arr[i] = this.arr[j];
            this.arr[j] = temp;
        }

        public void checkAndDouble(){
            if (this.size + 1 == this.arr.length){
                HeapNode[] newArr = new HeapNode[this.arr.length * 2];
                for (int i = 1; i <= size; i++){
                    newArr[i] = arr[i];
                }
                this.arr = newArr;
            }
        }
    }

    /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file. 
    *  
    */
    public static class HeapNode {

       public int key;
       public int rank;
       public boolean marked;
       public HeapNode child;
       public HeapNode next;
       public HeapNode prev;
       public HeapNode parent;

       //constructor
       public HeapNode(int key) {
           this.key = key;
       }

       public int getKey() {
           return this.key;
       }

       public int getRank() {return this.rank;};

       public boolean isMarked(){ return this.marked;}

       public HeapNode getChild(){ return this.child;}

       public HeapNode getNext(){ return this.next;}

       public HeapNode getPrev(){ return this.prev;}

       public HeapNode getParent(){ return this.parent;}

       public void linkLeft(HeapNode node){
           node.parent = this;
           if (this.child != null){
               node.next = this.child;
               this.child.prev.next = node;
               node.prev = this.child.prev;
               this.child.prev = node;
           }
           else{
               node.next = node;
               node.prev = node;
           }
           this.child = node;
           this.rank++;
       }
   }
}
