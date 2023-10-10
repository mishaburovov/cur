import java.util.ArrayList;
import java.util.List;

public class Main {
    private static double A = 0;
    private static double B = 4 * Math.PI;
    private static final double H = 1e-5;

    public static void main(String[] args) {
        List<Double> areas = new ArrayList<>();

        for (int num = 1; num < 11; num++) {
            long startTime = System.nanoTime();
            double peace = (B - A) / num;
            double peaceA = A;
            double peaceB = peaceA + peace;
            List<Thread> threads = new ArrayList<>();

            for (int i = 0; i < num; i++) {
                final double threadA = peaceA;
                final double threadB = peaceB;

                Thread thread = new Thread(() -> {
                    double area = traparea(threadA, threadB);
                    synchronized (areas) {
                        areas.add(area);
                    }
                });
                threads.add(thread);
                thread.start();

                peaceA = peaceB;
                peaceB += peace;
            }

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            double sum = 0;
            for (double area : areas) {
                sum += area;
            }
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
            System.out.println("Количество потоков: " + num + "  Сумма площадей: " + sum + "    Время: " + timeElapsed);

        }
    }

    public static double traparea(double a, double b) {
        int steps = (int) ((b - a) / H);

        double area = 0;

        for (int i = 0; i < steps; i++) {
            double x0 = a + i * H;
            double x1 = a + (i + 1) * H;

            double y0 = 2 * Math.sin(x0);
            double y1 = 2 * Math.sin(x1);

            double trap = (y0 + y1) * H / 2;
            area += trap;
        }

        return area;
    }
}