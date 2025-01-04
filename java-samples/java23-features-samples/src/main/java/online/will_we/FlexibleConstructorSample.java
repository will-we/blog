package online.will_we;

public class FlexibleConstructorSample {
    void main() {
        Dog dog = new Dog(3);
    }

    class Animal {
        String name;

        public Animal(String name) {
            this.name = name;
        }
    }

    class Dog extends Animal {
        int age;

        public Dog(int age) {
            // 在Java23以前的版本中，调用父类的构造器（super()）必须是构造函数的第一个语句，否则编译器会报错。
            this.age = age;
            // 甚至可以加上一些必要的参数校验
            if(age < 0) {
                throw new IllegalArgumentException("Age cannot be negative.");
            }
            super("Dog");
        }
    }
}
