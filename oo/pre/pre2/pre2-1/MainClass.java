import java.util.Arrays;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] argv) {
        String[] print = new String[1];
        Scanner scanner = new Scanner(System.in);
        String name = scanner.next();
        double price = scanner.nextDouble();
        int num = scanner.nextInt();
        Bookset book = new Bookset();
        book.setName(name);
        book.setPrice(price);
        book.setNum(num);
        int m = scanner.nextInt();
        int j = 1;
        for (int i = 0;i < m;i++) {
            int n = scanner.nextInt();
            if (n == 1) {
                if (print.length < j) {
                    String[] copy = Arrays.copyOf(print,print.length + 1);
                    print = Arrays.copyOf(copy,copy.length);
                }
                j++;
                print[print.length - 1] = book.getName();
            }
            else if (n == 2) {
                if (print.length < j) {
                    String[] copy = Arrays.copyOf(print,print.length + 1);
                    print = Arrays.copyOf(copy,copy.length);
                }
                j++;
                print[print.length - 1] = String.valueOf(book.getPrice());
            }
            else if (n == 3) {
                if (print.length < j) {
                    String[] copy = Arrays.copyOf(print,print.length + 1);
                    print = Arrays.copyOf(copy,copy.length);
                }
                j++;
                print[print.length - 1] = String.valueOf(book.getNum());
            }
            else if (n == 4) {
                book.setName(scanner.next());
            }
            else if (n == 5) {
                book.setPrice(scanner.nextDouble());
            }
            else if (n == 6) {
                book.setNum(scanner.nextInt());
            }
            else {
                if (print.length < j) {
                    String[] copy = Arrays.copyOf(print,print.length + 1);
                    print = Arrays.copyOf(copy,copy.length);
                }
                j++;
                print[print.length - 1] = String.valueOf(book.sum());
            }
        }
        for (int i = 0;i < print.length;i++) {
            System.out.println(print[i]);
        }
    }
}
