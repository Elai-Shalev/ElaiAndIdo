package LastYearTests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

import AVL.AVLTree;

import static org.junit.Assert.*;

public class AVLTreeTest {

	private static int[] randomArray(int size, int min, int max)
	{
		int[] arr = new int[size];
		for (int i = 0; i < arr.length; i++)
		{
			arr[i] = (int)(Math.random() * (max - min) + min);
		}
		
		return arr;
	}
	
	private static AVLTree arrayToTree(int[] arr)
	{
		//System.out.println("tree from " + Arrays.toString(arr));
		AVLTree tree = new AVLTree();
		for (int x : arr)
		{
			//System.out.println("inserting " + x);
			tree.insert(x, Integer.toString(x));
		//	assertTrue(AVLSanitizer.sanitizeTree(tree));
		}
		
		return tree;
	}
	
	@Test
	public void testEmpty() {
		AVLTree tree = new AVLTree();
		
		assertTrue(tree.empty());
		
		tree.insert(1, "1");
		assertFalse(tree.empty());
		
		tree.delete(1);
		assertTrue(tree.empty());
	}

	@Test
    public void testSearch() {
		AVLTree tree = new AVLTree();
		
		assertEquals(tree.search(1), null);
		
		tree.insert(1, "1");
		assertEquals(tree.search(1), "1");
		
		tree.delete(1);
		assertEquals(tree.search(1), null);
	}

	@Test
    public void testInsert() {
		AVLTree tree = new AVLTree();
		
		assertEquals(tree.insert(1, "1"), 0);
		assertEquals(tree.insert(1, "1"), -1);
		
		assertEquals(tree.insert(2, "2"), 1); // was 0
		// RR
		assertEquals(tree.insert(3, "3"), 3); // was 1
	}

	@Test
    public void testDelete() {
		AVLTree tree = new AVLTree();
		
		assertEquals(tree.delete(1), -1);
		
		tree.insert(1, "1");
		assertEquals(tree.delete(1), 0);
		
		tree.insert(2, "2");
		tree.insert(1, "1");
		tree.insert(3, "3");
		tree.insert(4, "4");
		// should be RR rotation
		int c = tree.delete(1);
		assertEquals(c, 3);
		
		//assertTrue(AVLSanitizer.sanitizeTree(tree));
		
		int[] values = randomArray(100, 0, 100);
		//values = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99};
		//int[] shuffDeter = new int[]{65, 10, 75, 99, 33, 2, 98, 36, 13, 95, 28, 30, 24, 12, 56, 77, 50, 39, 16, 81, 68, 19, 63, 59, 76, 34, 21, 3, 96, 1, 35, 46, 11, 45, 79, 14, 85, 69, 42, 64, 60, 41, 31, 27, 37, 9, 23, 55, 91, 6, 48, 17, 7, 72, 83, 97, 52, 54, 25, 67, 4, 40, 22, 51, 32, 53, 57, 47, 70, 74, 29, 88, 92, 58, 38, 93, 73, 61, 5, 66, 86, 94, 20, 49, 15, 90, 84, 26, 8, 43, 82, 78, 71, 89, 62, 18, 87, 80, 44};
		tree = arrayToTree(values);
		List<Integer> valuesShuffled = Arrays.stream(values).boxed().collect(Collectors.toList());
		Collections.shuffle(valuesShuffled);
		System.out.println(valuesShuffled.toString());
		for (int x : valuesShuffled)
		{
			System.out.format("Size %d%n", tree.root.getSize());
            AVLTree.print2D(tree.root);
            if (tree.search(x) != null){
                System.out.println("In Tree");
            }
            else{
                System.out.println("NOT In Tree");
            }
		    System.out.format("deleting %d%n", x);
			tree.delete(x);
			//assertTrue(AVLSanitizer.sanitizeTree(tree));
		}
	}

	// Simulate random insert and delete operations
	@Test
    public void testInsertAndDelete() {
		AVLTree tree = new AVLTree();
		
		for (int tries = 0; tries < 50; tries++)
		{
			int[] values = randomArray(100, -30, 30);
			System.out.println(Arrays.toString(values));
			List<Integer> valuesShuffled = Arrays.stream(values).boxed().collect(Collectors.toList());
			Collections.shuffle(valuesShuffled);
			
			List<Integer> valuesShuffled2 = new ArrayList<Integer>(valuesShuffled);
			Collections.shuffle(valuesShuffled2);
			for (int j = 0; j < 5; j++)
			{
				for (int i = 0; i < valuesShuffled.size(); i++)
				{
					tree.insert(valuesShuffled.get(i), Integer.toString(valuesShuffled.get(i)));
					tree.delete(valuesShuffled2.get(i));
					//assertTrue(tree.sanitize());
				}
			}
			for (int x : valuesShuffled)
			{
				System.out.println("x is: " + x);
				if (x < 0) {
					//System.out.println("tree before delete: ");
					//AVL.AVLTree.print2DUtil(tree.getRoot(),1);
					tree.delete(-x);
				}
				else {
					//System.out.println("tree before insert: ");
					//AVL.AVLTree.print2DUtil(tree.getRoot(),1);
					tree.insert(x, Integer.toString(x));
				}
			}
		}
	}

	@Test
    public void testMinMax() {
		for (int i = 0; i < 10; i++)
		{
			int[] values = randomArray(100, 0, 100);
		
			//System.out.println("values: " + Arrays.toString(values));
			AVLTree tree = arrayToTree(values);
			values = Arrays.stream(values).distinct().toArray();
			Arrays.sort(values);
			//System.out.println("sorted values:" + Arrays.toString(values));
			
			System.out.println("checking min max");
		    assertEquals(tree.max(), Integer.toString(values[values.length - 1]));
			assertEquals(tree.min(), Integer.toString(values[0]));
			
			System.out.println("deleting min max");
			int test = Integer.parseInt(tree.max());
			tree.delete(test);
			//System.out.println("\nnew max:" + tree.max() + "should be:" + Integer.toString(values[values.length - 2]));
			assertEquals(tree.max(), Integer.toString(values[values.length - 2]));
			
			tree.delete(Integer.parseInt(tree.min()));
			//System.out.println("\nnew min:" + tree.min() + "should be:" + Integer.toString(values[1]));
			assertEquals(tree.min(), Integer.toString(values[1]));
		}
		
		System.out.println("checking empty tree");
		//Empty tree
		AVLTree tree = new AVLTree();
		assertEquals(tree.max(), null);
		assertEquals(tree.min(), null);
	}

	@Test
    public void testKeysToArray() {
		int[] values = randomArray(100, 0, 100);
		AVLTree tree = arrayToTree(values);
		Arrays.sort(values);
		
		assertArrayEquals(tree.keysToArray(), Arrays.stream(values).distinct().toArray());
	}

	@Test
    public void testInfoToArray() {
		int[] values = randomArray(100, 0, 100);
		AVLTree tree = arrayToTree(values);
		Arrays.sort(values);
		
		assertArrayEquals(tree.infoToArray(), Arrays.stream(values).distinct().mapToObj(x -> Integer.toString(x)).toArray());
	}

	@Test
    public void testSize() {
		int[] values = randomArray(100, 0, 100);
		AVLTree tree = arrayToTree(values);
		int realSize = (int)Arrays.stream(values).distinct().count();

		System.out.println(realSize + " vs " + tree.size());
		assertEquals(tree.size(), realSize);
	}

	@Test
    public void testGetRoot() {
		AVLTree tree = new AVLTree();
		assertEquals(null, tree.getRoot());
		
		tree.insert(1, "1");
		assertEquals(1, tree.getRoot().getKey());
		
		tree.insert(2, "2");
		assertEquals(1, tree.getRoot().getKey());
		
		tree.delete(1);
		assertEquals(2, tree.getRoot().getKey());
		
		assertEquals(null, tree.getRoot().getParent());
	}

}
