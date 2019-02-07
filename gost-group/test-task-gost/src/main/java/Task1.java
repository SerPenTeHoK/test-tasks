public class Task1 {
    public static void main(String[] args) {
        // В задаче не указаны натуральные числа или вещественные, т.к.
        // это тестовое задание берём натуральные числа

        for (int i = 1; i <= 100; i++) {
            if (i % 7 == 0 && i % 2 == 0) {
                System.out.println("TwoSeven");
            } else if (i % 2 == 0) {
                System.out.println("Two");
            } else if (i % 7 == 0) {
                System.out.println("Seven");
            } else {
                System.out.println(i);
            }
        }
    }
}
