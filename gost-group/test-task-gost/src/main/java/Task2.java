import java.math.BigInteger;
import java.util.HashMap;

public class Task2 {
    // В ТЗ не описаны границы чисел, т.к.
    // тестовое берём вычисление факториала из статьи хабра FactorialUtil.
    // В идеале надо написать тест для функции calcF, если бы она была публичной,
    // и т.к. тестовое задание распечатываются значения

    public static void main(String[] args) {

        try {
            System.out.println(calcF(2, 2));
            System.out.println(calcF(3, 1));
            System.out.println(calcF(100, 98));
            System.out.println(calcF(1, 1));
            System.out.println(calcF(1, 3));
            System.out.println(calcF(9, 100));
        } catch (BizLogicException e) {
            e.printStackTrace();
        }
    }

    private static BigInteger calcF(int m, int r) throws BizLogicException {
        if (r > m)
            throw new BizLogicException("Invalid parameters r > m");
        return FactorialUtil.factorial(m)
                .divide(
                        FactorialUtil.factorial(r)
                                .multiply(FactorialUtil.factorial(m - r))
                );
    }

}
