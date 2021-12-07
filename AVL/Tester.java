package AVL;

import AVL.AVLTree;
import org.junit.Assert;

import java.util.Arrays;

public class Tester {

    public static boolean checkSize(AVLTree.IAVLNode node){
        if (!node.isRealNode()){
            if (node.getSize() == 0){
                return true;
            }
            return false;
        }
        return (node.getSize() == node.getLeft().getSize() + node.getRight().getSize() + 1 &&
                checkSize(node.getLeft()) && checkSize(node.getRight()));
    }

    public static boolean checkHeight(AVLTree.IAVLNode node){
        if (!node.isRealNode()){
            if (node.getHeight() == -1){
                return true;
            }
            return false;
        }
        return (node.getHeight() == Math.max(node.getLeft().getHeight(), node.getRight().getHeight()) + 1 &&
                checkHeight(node.getLeft()) && checkHeight(node.getRight()));
    }

    public static boolean checkMin(AVLTree.IAVLNode node){
        if (!node.isRealNode()){
            return true;
        }

        if (!node.getLeft().isRealNode()){
            if (node.getMin().getKey() == node.getKey()){
                return checkMin(node.getRight());
            }
            return false;
        }
        return (node.getMin().getKey() == node.getLeft().getMin().getKey() &&
                checkMin(node.getLeft()) && checkMin(node.getRight()));
    }

    public static boolean checkMax(AVLTree.IAVLNode node){

        if (!node.isRealNode()){
            return true;
        }

        if (!node.getRight().isRealNode()){
            if (node.getMax().getKey() == node.getKey()){
                return checkMax(node.getLeft());
            }
            return false;
        }
        return (node.getMax().getKey() == node.getRight().getMax().getKey() &&
                checkMax(node.getLeft()) && checkMax(node.getRight()));
    }

    public static boolean checkFields(AVLTree tree){
        return checkSize(tree.getRoot()) && checkHeight(tree.getRoot()) && checkMin(tree.getRoot()) && checkMax(tree.getRoot());
    }



    public static void main(String [] args){

        AVLTree T1 = new AVLTree();
        int k = 0;
        System.out.print("{");
        while (k<100){
            k++;
            System.out.print(k + ", ");
        }
        System.out.print("}");


        T1.insert(2,"a");
        boolean b = checkFields(T1);
        Assert.assertTrue(checkFields(T1));
        T1.insert(1,"b");
        Assert.assertTrue(checkFields(T1));
        T1.insert(3,"elai");
        Assert.assertTrue(checkFields(T1));
        T1.insert(5,"elai");
        Assert.assertTrue(checkFields(T1));
        T1.insert(4,"elai");
        Assert.assertTrue(checkFields(T1));
        T1.insert(6,"elai");
        Assert.assertTrue(checkFields(T1));
        T1.insert(7,"elai");
        Assert.assertTrue(checkFields(T1));
        T1.insert(8,"elai");
        Assert.assertTrue(checkFields(T1));
        T1.insert(9,"elai");
        Assert.assertTrue(checkFields(T1));
        T1.insert(10,"elai");
        Assert.assertTrue(checkFields(T1));
        T1.insert(11,"elai");
        Assert.assertTrue(checkFields(T1));
        T1.insert(12,"elai");
        Assert.assertTrue(checkFields(T1));
        T1.insert(13,"elai");
        Assert.assertTrue(checkFields(T1));

        AVLTree T2 = new AVLTree();
        System.out.println(T2.insert(70,""));
        AVLTree.print2D(T2.root);
        System.out.println(T2.insert(40,""));
        AVLTree.print2D(T2.root);
        System.out.println(T2.insert(21,""));
        AVLTree.print2D(T2.root);
        System.out.println(T2.insert(30,""));
        AVLTree.print2D(T2.root);
        System.out.println(T2.insert(20,""));
        AVLTree.print2D(T2.root);
        System.out.println(T2.insert(15,""));

        AVLTree.print2D(T2.root);
        System.out.println(T2.insert(17,""));

        AVLTree Node = new AVLTree();
        Node.insert(14,"");
        AVLTree.print2D(T1.root);
        AVLTree.print2D(T2.root);
        T2.join(Node.getRoot(), T1);
        AVLTree.print2D(T2.root);
        int splitBy = 6;
        AVLTree[] splits = T2.split(splitBy);
        System.out.println("Smaller than " + splitBy);
        AVLTree.print2D(splits[0].root);
        System.out.println("Bigger than " + splitBy);
        AVLTree.print2D(splits[1].root);

        AVLTree.print2D(splits[1].root);
        System.out.println(Arrays.toString(splits[1].keysToArray()));
        splits[1].delete(14);
        System.out.println(Arrays.toString(splits[1].keysToArray()));
        AVLTree.print2D(splits[1].root);

    }
}
