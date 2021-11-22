import java.util.Arrays;

public class Tester {

    public static void main(String [] args){

        AVLTree T1 = new AVLTree();

        T1.insert(2,"a");
        T1.insert(1,"b");
        T1.insert(3,"elai");
        //T1.insert(8, "Ido");
        //T1.insert(7, "sapir");
        //T1.insert(9, "snir");
        //T1.insert(4, "idoamit");

        AVLTree T2 = new AVLTree();
        T2.insert(14,"");
        T2.insert(10,"");
        T2.insert(16,"");
        T2.insert(9,"");
        T2.insert(12,"");
        T2.insert(15,"");
        T2.insert(17,"");

        AVLTree Node = new AVLTree();
        Node.insert(4,"");
        AVLTree.print2DUtil(T1.root,0);
        AVLTree.print2DUtil(T2.root,0);
        System.out.println("___");
        T1.join(Node.getRoot(), T2);
        AVLTree.print2DUtil(T1.root,0);





        //rotate testing
        //AVLTree.print2DUtil(T1.root, 0);
        T1.rotateLeft(T1.root);
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
