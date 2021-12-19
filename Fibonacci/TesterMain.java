package Fibonacci;

import java.util.ArrayList;
import java.util.Arrays;

public class TesterMain {
    public static void main(String[] args){
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
    }
}
