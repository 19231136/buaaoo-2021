import java.util.Arrays;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] argv) {
        MainClass mainClass = new MainClass();
        Scanner scanner =  new Scanner(System.in);
        int n = scanner.nextInt();
        Bookshelf[] bookshelves = new Bookshelf[n];
        for (int i = 0; i < n; i++) {
            bookshelves[i] = new Bookshelf();
        }
        int m = scanner.nextInt();
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
                String out = String.valueOf(bookshelves[attribute - 1].search(scanner.next()));
                print[print.length - 1] = out;
            }
            else if (type == 2) {
                if (print.length < j) {
                    String[] copy = Arrays.copyOf(print, print.length + 1);
                    print = Arrays.copyOf(copy, copy.length);
                }
                j++;
                print[print.length - 1] = String.valueOf(bookshelves[attribute - 1].num());
            }
            else if (type == 3) {
                if (print.length < j) {
                    String[] copy = Arrays.copyOf(print, print.length + 1);
                    print = Arrays.copyOf(copy, copy.length);
                }
                j++;
                print[print.length - 1] = String.valueOf(bookshelves[attribute - 1].sum());
            }
            else if (type == 4) {
                mainClass.add(attribute,bookshelves,scanner);
            }
            else {
                if (print.length < j) {
                    String[] copy = Arrays.copyOf(print, print.length + 1);
                    print = Arrays.copyOf(copy, copy.length);
                }
                j++;
                String name = scanner.next();
                String out = String.valueOf(bookshelves[attribute - 1].remove_shelf(name));
                print[print.length - 1] = out;
            }
        }
        for (int i = 0;i < print.length;i++) {
            System.out.println(print[i]);
        }
    }

    public void add(int attribute,Bookshelf[] bookshelves,Scanner scanner) {
        String type = scanner.next();
        if (type.equals("Other")) {
            Bookset bookset = new Bookset(scanner.next(), scanner.nextDouble(), scanner.nextInt());
            bookshelves[attribute - 1].add_shelf(bookset);
            bookshelves[attribute - 1].set1();
        }
        else if (type.equals("OtherA")) {
            String name = scanner.next();
            double price = scanner.nextDouble();
            Bookset bookset = new OtherA(name, price, scanner.nextInt(),scanner.nextInt());
            bookshelves[attribute - 1].add_shelf(bookset);
            bookshelves[attribute - 1].set2();
        }
        else if (type.equals("OtherS")) {
            String name = scanner.next();
            double price = scanner.nextDouble();
            Bookset bookset = new OtherS(name, price, scanner.nextInt(),scanner.nextInt());
            bookshelves[attribute - 1].add_shelf(bookset);
            bookshelves[attribute - 1].set3();
        }
        else if (type.equals("Novel")) {
            String name = scanner.next();
            double price = scanner.nextDouble();
            int num = scanner.nextInt();
            Bookset bookset = new Novel(name, price, num,scanner.nextInt(),scanner.nextBoolean());
            bookshelves[attribute - 1].add_shelf(bookset);
            bookshelves[attribute - 1].set4();
        }
        else if (type.equals("Poetry")) {
            String name = scanner.next();
            double price = scanner.nextDouble();
            int num = scanner.nextInt();
            Bookset bookset = new Poetry(name, price, num,scanner.nextInt(),scanner.next());
            bookshelves[attribute - 1].add_shelf(bookset);
            bookshelves[attribute - 1].set5();
        }
        else if (type.equals("Math")) {
            String name = scanner.next();
            double price = scanner.nextDouble();
            int num = scanner.nextInt();
            Bookset bookset = new Math(name, price, num,scanner.nextInt(),scanner.nextInt());
            bookshelves[attribute - 1].add_shelf(bookset);
            bookshelves[attribute - 1].set6();
        }
        else {
            String name = scanner.next();
            double price = scanner.nextDouble();
            int num = scanner.nextInt();
            Bookset bookset = new Computer(name, price, num,scanner.nextInt(),scanner.next());
            bookshelves[attribute - 1].add_shelf(bookset);
            bookshelves[attribute - 1].set7();
        }
    }
}
