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
                print = mainClass.judge(print,j);
                j++;
                String out = mainClass.type1(bookshelves,attribute,scanner);
                print[print.length - 1] = out;
            }
            else if (type == 2) {
                print = mainClass.judge(print,j);
                j++;
                String out = mainClass.type2(bookshelves,attribute);
                print[print.length - 1] = out;
            }
            else if (type == 3) {
                print = mainClass.judge(print,j);
                j++;
                String out = mainClass.type3(bookshelves,attribute);
                print[print.length - 1] = out;
            }
            else if (type == 4) {
                String type1 = scanner.next();
                String name = scanner.next();
                if (mainClass.add(attribute,bookshelves,scanner,type1,name).equals("")) {
                    continue;
                }
                else {
                    print = mainClass.judge(print,j);
                    j++;
                    print[print.length - 1] = "Oh, no! The " + name + " exist.";
                }
            }
            else {
                print = mainClass.judge(print,j);
                j++;
                String name = scanner.next();
                String out = mainClass.type5(bookshelves,attribute,name);
                print[print.length - 1] = out;
            }
            scanner.nextLine();
        }
        for (int i = 0;i < print.length;i++) {
            System.out.println(print[i]);
        }
    }

    public String add(int attribute,Bookshelf[] bookshelves,Scanner scanner,String type,String n) {
        String out = "";
        MainClass mainClass = new MainClass();
        if (type.equals("Other")) {
            Bookset bookset = new Bookset(n, scanner.nextDouble(), scanner.nextLong());
            out = mainClass.add_1(bookshelves,attribute,bookset,n);
        }
        else if (type.equals("OtherA")) {
            double price = scanner.nextDouble();
            Bookset bookset = new OtherA(n, price, scanner.nextLong(),scanner.nextLong());
            out = mainClass.add_2(bookshelves,attribute,bookset,n);
        }
        else if (type.equals("OtherS")) {
            double price = scanner.nextDouble();
            Bookset bookset = new OtherS(n, price, scanner.nextLong(),scanner.nextLong());
            out = mainClass.add_3(bookshelves,attribute,bookset,n);
        }
        else if (type.equals("Novel")) {
            double price = scanner.nextDouble();
            long num = scanner.nextLong();
            Bookset bookset = new Novel(n, price, num,scanner.nextLong(),scanner.nextBoolean());
            out = mainClass.add_4(bookshelves,attribute,bookset,n);
        }
        else if (type.equals("Poetry")) {
            double price = scanner.nextDouble();
            long num = scanner.nextLong();
            Bookset bookset = new Poetry(n, price, num,scanner.nextLong(),scanner.next());
            try {
                bookshelves[attribute - 1].add_shelf(bookset);
                bookshelves[attribute - 1].set5();
            } catch (Exception3 exception3) {
                out = "Oh, no! The " + n + " exist.";
            }
        }
        else if (type.equals("Math")) {
            double price = scanner.nextDouble();
            long num = scanner.nextLong();
            Bookset bookset = new Math(n, price, num,scanner.nextLong(),scanner.nextLong());
            try {
                bookshelves[attribute - 1].add_shelf(bookset);
                bookshelves[attribute - 1].set6();
            } catch (Exception3 exception3) {
                out = "Oh, no! The " + n + " exist.";
            }
        }
        else {
            double price = scanner.nextDouble();
            long num = scanner.nextLong();
            Bookset bookset = new Computer(n, price, num,scanner.nextLong(),scanner.next());
            try {
                bookshelves[attribute - 1].add_shelf(bookset);
                bookshelves[attribute - 1].set7();
            } catch (Exception3 exception3) {
                out = "Oh, no! The " + n + " exist.";
            }
        }
        return out;
    }

    public String[] judge(String[] print,int j) {
        String[] out;
        if (print.length < j) {
            String[] copy = Arrays.copyOf(print, print.length + 1);
            out = Arrays.copyOf(copy, copy.length);
            return out;
        }
        return print;
    }

    public String type1(Bookshelf[] bookshelves,int attribute,Scanner scanner) {
        String out = "";
        String name = scanner.next();
        try {
            out = String.valueOf(bookshelves[attribute - 1].search(name));
        } catch (Exception2 exception2) {
            out = "Oh, no! This is empty.";
        } catch (Exception1 exception1) {
            out = "Oh, no! We don't have " + name + ".";
        }
        return out;
    }

    public String type2(Bookshelf[] bookshelves,int attribute) {
        String out = "";
        try {
            out = String.valueOf(bookshelves[attribute - 1].num());
        } catch (Exception2 exception2) {
            out = "Oh, no! This is empty.";
        }
        return out;
    }

    public String type3(Bookshelf[] bookshelves,int attribute) {
        String out = "";
        try {
            out = String.valueOf(bookshelves[attribute - 1].sum());
        } catch (Exception2 exception2) {
            out = "Oh, no! This is empty.";
        }
        return out;
    }

    public String type5(Bookshelf[] bookshelves,int attribute,String name) {
        String out = "";
        try {
            out = String.valueOf(bookshelves[attribute - 1].remove_shelf(name));
        } catch (Exception4 exception4) {
            out = "mei you wo zhen mei you.";
        }
        return out;
    }

    public String add_1(Bookshelf[] bookshelves,int attribute,Bookset bookset,String n) {
        try {
            bookshelves[attribute - 1].add_shelf(bookset);
            bookshelves[attribute - 1].set1();
            return "";
        } catch (Exception3 exception3) {
            return "Oh, no! The " + n + " exist.";
        }
    }

    public String add_2(Bookshelf[] bookshelves,int attribute,Bookset bookset,String n) {
        try {
            bookshelves[attribute - 1].add_shelf(bookset);
            bookshelves[attribute - 1].set2();
            return "";
        } catch (Exception3 exception3) {
            return "Oh, no! The " + n + " exist.";
        }
    }

    public String add_3(Bookshelf[] bookshelves,int attribute,Bookset bookset,String n) {
        try {
            bookshelves[attribute - 1].add_shelf(bookset);
            bookshelves[attribute - 1].set3();
            return "";
        } catch (Exception3 exception3) {
            return "Oh, no! The " + n + " exist.";
        }
    }

    public String add_4(Bookshelf[] bookshelves,int attribute,Bookset bookset,String n) {
        try {
            bookshelves[attribute - 1].add_shelf(bookset);
            bookshelves[attribute - 1].set4();
            return "";
        } catch (Exception3 exception3) {
            return "Oh, no! The " + n + " exist.";
        }
    }

}
