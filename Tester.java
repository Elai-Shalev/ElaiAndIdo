import java.util.Arrays;

public class Tester {

    public static void main(String [] args){

        AVLTree T1 = new AVLTree();


        T1.insert(2,"a");
        T1.insert(1,"b");
        T1.insert(3,"elai");
        T1.insert(5,"elai");
        T1.insert(4,"elai");
        T1.insert(6,"elai");

        //T1.insert(8, "Ido");
        //T1.insert(7, "sapir");
        //T1.insert(9, "snir");
        //T1.insert(4, "idoamit");


        AVLTree T2 = new AVLTree();
        System.out.println(T2.insert(70,""));
        AVLTree.print2D(T2.root);
        System.out.println(T2.insert(40,""));
        AVLTree.print2D(T2.root);
        System.out.println(T2.insert(10,""));
        AVLTree.print2D(T2.root);
        System.out.println(T2.insert(30,""));
        AVLTree.print2D(T2.root);
        System.out.println(T2.insert(20,""));
        AVLTree.print2D(T2.root);
        System.out.println(T2.insert(15,""));

        AVLTree.print2D(T2.root);
        System.out.println(T2.insert(17,""));


        //T2.rebalance(T2.root);
        //AVLTree.print2D(T2.root);


        AVLTree Node = new AVLTree();
        Node.insert(9,"");
        AVLTree.print2D(T1.root);
        AVLTree.print2D(T2.root);
        T2.join(Node.getRoot(), T1);
        AVLTree.print2D(T2.root);







        //rotate testing
        //AVLTree.print2DUtil(T1.root, 0);
        //T1.rotateLeft(T1.root);
        //AVLTree.print2DUtil(T1.root, 0);

        if(!(T1.max() == "snir")){
            System.out.println("Error in max() \n" +
                    "prints: " + T1.max() +" should print: snir");

        }
        if(!(T1.min() == "a")){
            System.out.println("Error in min() \n" +
                    "prints: " + T1.min() +" should print: a");
        }

        AVLTree.print2DUtil(T1.root, 0);
        System.out.println(Arrays.toString(T1.keysToArray()));
        T1.delete(2);
        System.out.println(Arrays.toString(T1.keysToArray()));
        //T1.root = T1.rotateLeft(T1.getRoot());

        System.out.println("____________");
        AVLTree.print2DUtil(T1.root, 0);

        T1.printArray(T1.keysToArray());
        if(!(T1.isEqualkeys(new int[] {1,2,4,6,7,8,9}))){
            System.out.println("Error in keysToArray");
        }
        if(!(T1.infoToArray() == new String[] {"a", "b", "idoamit", "elai", "sapir", "ido", "snir"})){
            System.out.println("Error in infosToArray");
        }

    }
}
