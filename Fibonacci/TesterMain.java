package Fibonacci;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TesterMain {

    static void mytest22(FibonacciHeap fibonacciHeap, ArrayList<FibonacciHeap.HeapNode> nodes) {
        int treeSize = 32768;
        int sizeToDelete = 1000;
        boolean noCascading = true;
        int iterationCuts;
        int totalCuts = FibonacciHeap.totalCuts();
        int links = FibonacciHeap.totalLinks();

        for (int i = 0; i < treeSize; i++) {
            iterationCuts = FibonacciHeap.totalCuts();

            try{
                fibonacciHeap.decreaseKey(nodes.get(i), nodes.get(i).getKey() - (treeSize - i));
            }
            catch (Exception e){
                return;
            }

            if (FibonacciHeap.totalCuts() - iterationCuts > 1)
                noCascading = false;
        }

        if (fibonacciHeap.potential() != treeSize ||
                FibonacciHeap.totalCuts() - totalCuts != treeSize - 1 ||
                FibonacciHeap.totalLinks() - links != 0 ||
                fibonacciHeap.countersRep()[0] != treeSize ||
                noCascading)
            System.out.println("bugFound(test)");
    }

    static void mytest22Start() {
        String test = "test22";
        FibonacciHeap fibonacciHeap = new FibonacciHeap();

        int treeSize = 32768;
        int sizeToDelete = 1000;

        ArrayList<FibonacciHeap.HeapNode> nodes = new ArrayList<>();

        for (int i = treeSize; i < treeSize * 2; i++) {
            nodes.add(fibonacciHeap.insert(i));
        }

        for (int i = 0; i < sizeToDelete; i++) {
            fibonacciHeap.insert(i);
        }

        for (int i = 0; i < sizeToDelete; i++) {
            fibonacciHeap.deleteMin();
        }


        if (fibonacciHeap.potential() != 1)
            System.out.println("bugFound(test)");

        int totalCuts = FibonacciHeap.totalCuts();
        int links = FibonacciHeap.totalLinks();


        Collections.shuffle(nodes);

        mytest22(fibonacciHeap, nodes);
        //mytest22(fibonacciHeap, nodes);
        //mytest22(fibonacciHeap, nodes);
        //mytest22(fibonacciHeap, nodes);
        //mytest22(fibonacciHeap, nodes);
    }

    static void Question1(){
        for (int j=0; j <=0; j++){
            int power = 20;
            System.out.println("\n");
            System.out.println("Power = " + power);
            int m = (int)Math.pow(2, power);
            FibonacciHeap.HeapNode[] nodes = new FibonacciHeap.HeapNode[m+1];
            FibonacciHeap fHeap = new FibonacciHeap();

            long start = System.currentTimeMillis();

            for (int k = m-1; k >= -1; k--){
                nodes[k+1] = fHeap.insert(k);
            }

            fHeap.deleteMin();
            //HeapPrinter.print(fHeap, false);

            for (int i = FibonacciHeap.log2(m); i >= 1; i--){
                fHeap.decreaseKey(nodes[m-(int)Math.pow(2, i) + 2], m+1);
            }

            fHeap.decreaseKey(nodes[m-2+1], m+1);

            //HeapPrinter.print(fHeap, false);

            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;

            System.out.println("Elapsed Time = " + timeElapsed);
            System.out.println("Total Links = " + FibonacciHeap.totalLinks());
            System.out.println("Total Cuts = " + FibonacciHeap.totalCuts());
            System.out.println("Potential = " + fHeap.potential());
        }
    }

    static void Question2(){
        int i = 6;
        int m = (int)Math.pow(3, i) - 1;
        FibonacciHeap fHeap = new FibonacciHeap();

        long start = System.currentTimeMillis();

        for (int k = m; k >= 0; k--){
            fHeap.insert(k);
        }

        for (int k = 1; k <= 3*m/4; k++){
            fHeap.deleteMin();
            System.out.println("Delete " + k + " Total Links = " + FibonacciHeap.totalLinks());
        }

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;


        System.out.println("m = " + m);
        System.out.println("size = " + fHeap.size);
        System.out.println("Elapsed Time = " + timeElapsed);
        System.out.println("Total Links = " + FibonacciHeap.totalLinks());
        System.out.println("Total Cuts = " + FibonacciHeap.totalCuts());
        System.out.println("Potential = " + fHeap.potential());
    }

    public static void main(String[] args){
        Question2();



        /*mytest22Start();
        FibonacciHeap m = new FibonacciHeap();
        ArrayList<FibonacciHeap.HeapNode> nodes = new ArrayList<>();

        //FibonacciHeap.kMin(m, 4);
        for (int i = 0; i < 17; i++){
            nodes.add(m.insert(i));
        }
        System.out.println("potential " + m.potential());
        System.out.println("Min " + m.findMin().getKey());
        System.out.println("CountersRep " + Arrays.toString(m.countersRep()));
        System.out.println("Size " + m.size());
        System.out.println("NumTrees " + m.totalTrees);
        System.out.println("");

        m.deleteMin();
        System.out.println("potential " + m.potential());
        System.out.println("Min " + m.findMin().getKey());
        System.out.println("CountersRep " + Arrays.toString(m.countersRep()));
        System.out.println("Size " + m.size());
        System.out.println("NumTrees " + m.totalTrees);
        System.out.println("Kmin " + Arrays.toString(FibonacciHeap.kMin(m, 10)));
        System.out.println("");

        m.decreaseKey(nodes.get(15), 100);
        m.decreaseKey(nodes.get(11), 100);
        m.decreaseKey(nodes.get(14), 100);
        m.deleteMin();
        System.out.println("potential " + m.potential());
        System.out.println("Min " + m.findMin().getKey());
        System.out.println("CountersRep " + Arrays.toString(m.countersRep()));
        System.out.println("Size " + m.size());
        System.out.println("NumTrees " + m.totalTrees);
        System.out.println("");

        m.deleteMin();
        System.out.println("potential " + m.potential());
        System.out.println("Min " + m.findMin().getKey());
        System.out.println("CountersRep " + Arrays.toString(m.countersRep()));
        System.out.println("Size " + m.size());
        System.out.println("NumTrees " + m.totalTrees);
        System.out.println("");

        m.deleteMin();
        System.out.println("potential " + m.potential());
        System.out.println("Min " + m.findMin().getKey());
        System.out.println("CountersRep " + Arrays.toString(m.countersRep()));
        System.out.println("Size " + m.size());
        System.out.println("NumTrees " + m.totalTrees);
        System.out.println("");

        m.insert(1);
        m.insert(2);
        System.out.println("potential " + m.potential());
        System.out.println("Min " + m.findMin().getKey());
        System.out.println("CountersRep " + Arrays.toString(m.countersRep()));
        System.out.println("Size " + m.size());
        System.out.println("NumTrees " + m.totalTrees);
        System.out.println("");

        m.deleteMin();
        System.out.println("potential " + m.potential());
        System.out.println("Min " + m.findMin().getKey());
        System.out.println("CountersRep " + Arrays.toString(m.countersRep()));
        System.out.println("Size " + m.size());
        System.out.println("NumTrees " + m.totalTrees);
        System.out.println("");

        FibonacciHeap m2 = new FibonacciHeap();
        ArrayList<FibonacciHeap.HeapNode> nodes2 = new ArrayList<>();

        HeapPrinter.print(m, false);
        //FibonacciHeap.kMin(m, 4);
        for (int i = 18; i < 24; i++){
            nodes2.add(m2.insert(i));
        }
        HeapPrinter.print(m2, false);

        m2.meld(m);
        HeapPrinter.print(m2, false);*/
    }
}
