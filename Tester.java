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
        T1.insert(7,"elai");
        T1.insert(8,"elai");
        T1.insert(9,"elai");
        T1.insert(10,"elai");
        T1.insert(11,"elai");
        T1.insert(12,"elai");
        T1.insert(13,"elai");

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
