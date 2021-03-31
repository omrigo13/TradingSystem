import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println("hello world");
        ConcurrentLinkedQueue<Integer> l1=new ConcurrentLinkedQueue<>();
        l1.add(2);
        l1.add(3);
        l1.add(7);
        ConcurrentLinkedQueue<Integer> l2=new ConcurrentLinkedQueue<>();
        l2.add(2);
        l2.add(7);
        l2.add(4);
        ConcurrentLinkedQueue<Integer> l3=new ConcurrentLinkedQueue<>();
        l3.add(2);
        l3.add(3);
        l3.add(7);
        l1.retainAll(l2);
        l1.retainAll(l3);

        System.out.println(l1);
    }
}
