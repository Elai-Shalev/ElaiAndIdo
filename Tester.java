public class Tester {

    public static void main(String [] args){

        AVLTree T1 = new AVLTree();
        T1.insert(1,"a");
        T1.insert(2,"b");
        T1.insert(6,"elai");
        T1.insert(8, "Ido");
        T1.insert(7, "sapir");
        T1.insert(9, "snir");
        T1.insert(4, "idoamit");

        System.out.println(T1.max());
        System.out.println(T1.min());
        System.out.println(T1.keysToArray());
        System.out.println(T1.infoToArray());






    }
}
