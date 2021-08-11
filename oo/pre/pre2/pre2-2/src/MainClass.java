import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] argv) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        Bookshelf[] bookshelves = new Bookshelf[n];
        for (int i = 0; i < n; i++) {
            bookshelves[i] = new Bookshelf();
        }
        String[] print = new String[1];
        int j = 1;
        for (int i = 0; i < m; i++) {
            int type = scanner.nextInt();
            int attribute = scanner.nextInt();
            if (type == 1) {
                if (print.length < j) {
                    String[] copy = Arrays.copyOf(print, print.length + 1);
                    print = Arrays.copyOf(copy, copy.length);
                }
                j++;
                print[print.length - 1] = String.valueOf(bookshelves[attribute - 1].high_price());
            } else if (type == 2) {
                if (print.length < j) {
                    String[] copy = Arrays.copyOf(print, print.length + 1);
                    print = Arrays.copyOf(copy, copy.length);
                }
                j++;
                print[print.length - 1] = String.valueOf(bookshelves[attribute - 1].sum());
            } else if (type == 3) {
                String name = scanner.next();
                BigDecimal price = BigDecimal.valueOf(scanner.nextDouble());
                int num = scanner.nextInt();
                Bookset bookset = new Bookset(name, price, num);
                bookshelves[attribute - 1].add_shelf(bookset);
            } else {
                String name = scanner.next();
                if (print.length < j) {
                    String[] copy = Arrays.copyOf(print, print.length + 1);
                    print = Arrays.copyOf(copy, copy.length);
                }
                j++;
                int num = bookshelves[attribute - 1].remove_shelf(name);
                print[print.length - 1] = String.valueOf(num);
            }
        }
        for (int i = 0;i < print.length;i++) {
            System.out.println(print[i]);
        }
    }
}

