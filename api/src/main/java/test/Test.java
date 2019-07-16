package test;

public class Test {
    public static class Parent{
        protected boolean flage;
    }

    public static class Children1 extends Parent{
        public boolean flage = true;
    }

    public static class Children2 extends Parent{
    }

    public static void main(String[] args) {
        Parent parent1 = new Children1();
        Parent parent2 = new Children2();
        System.out.println(parent1.flage);
        System.out.println(parent2.flage);

        Children1 children1 = new Children1();
        Children2 children2 = new Children2();
        System.out.println(children1.flage);
        System.out.println(children2.flage);

    }
}
