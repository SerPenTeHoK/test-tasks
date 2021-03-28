public class Task1 {
    public static void main(String[] args) {
        // В задаче не указаны натуральные числа или вещественные, т.к.
        // это тестовое задание берём натуральные числа

        for (int i = 1; i <= 100; i++) {
            boolean is7 = i % 7 == 0;
            boolean is2 = i % 2 == 0;

            if (is7 && is2) {
                System.out.println("TwoSeven");
            } else if (is2) {
                System.out.println("Two");
            } else if (is7) {
                System.out.println("Seven");
            } else {
                System.out.println(i);
            }
        }
    }
}
