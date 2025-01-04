package online.will_we;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorSpecies;

import java.util.Arrays;


public class VectorAddition {
    void main() {
        // 创建向量规范，指定向量类型和长度
        VectorSpecies<Integer> SPECIES = IntVector.SPECIES_PREFERRED;

        // 初始化数组
        int[] a = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] b = {8, 7, 6, 5, 4, 3, 2, 1};
        int[] c = new int[a.length];

        // 使用向量 API 进行加法运算
        for (int i = 0; i < a.length; i += SPECIES.length()) {
            // 将数组加载到向量
            var va = IntVector.fromArray(SPECIES, a, i);
            var vb = IntVector.fromArray(SPECIES, b, i);
            // 向量加法
            var vc = va.add(vb);
            // 将结果存储回数组
            vc.intoArray(c, i);
        }

        // 输出结果
        System.out.println(Arrays.toString(c));
    }
}