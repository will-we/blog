package online.will_we;

public class CaseSample {
    void main() {
        Object obj = 55;
        printValue(obj);
    }

    private static void printValue(Object obj) {
        switch (obj) {
            case int i when i > 40 && i < 60 -> System.out.println("i is between 40 and 60");
            case int i -> System.out.println("i = " + i);
            case String s -> System.out.println("s = " + s);
            default -> System.out.println("obj is neither an Integer nor a String");
        }
    }
}
