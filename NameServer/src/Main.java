

public class Main {

    public static void main(String[] args) {

        System.out.println(getHash("map branchh"));
        System.out.println(getHash("YorickDeBock"));


    }

    public static int getHash(String s){
        return Math.abs(s.hashCode() % 32768);
    }
}
