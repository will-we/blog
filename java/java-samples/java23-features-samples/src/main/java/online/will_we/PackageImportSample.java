package online.will_we;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("DuplicatedCode")
public class PackageImportSample {
    void main() {
        List<Integer> list = List.of(1,2,3,4,5,6,7,8,9,10);
        list.forEach(System.out::println);
        Map<String, Integer> map = new HashMap<>();
        map = Map.of("a", 1, "b", 2, "c", 3);
        map.forEach((k, v) -> System.out.println(k + " " + v));
    }
}
