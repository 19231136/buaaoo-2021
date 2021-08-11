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
                print[print.length - 1] = mainClass.type1(bookshelves,attribute,scanner);
            }
            else if (type == 2) {
                print = mainClass.judge(print,j);
                j++;
                print[print.length - 1] = mainClass.type2(bookshelves,attribute);;
            }
            else if (type == 3) {
                print = mainClass.judge(print,j);
                j++;
                print[print.length - 1] = mainClass.type3(bookshelves,attribute);
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
            else if (type == 6) {
                print = mainClass.judge(print,j);
                j++;
                int j1 = scanner.nextInt();
                String out = mainClass.type6(bookshelves,attribute,j1);
                print[print.length - 1] = out;
                bookshelves = mainClass.update6(out,bookshelves,attribute,j1);
            }
            else {
                print = mainClass.judge(print,j);
                j++;
                print[print.length - 1] = mainClass.type5(bookshelves,attribute,scanner);;
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

    public String type5(Bookshelf[] bookshelves,int attribute,Scanner scanner) {
        String name = scanner.next();
        String out = "";
        try {
            out = String.valueOf(bookshelves[attribute - 1].remove_shelf(name));
        } catch (Exception4 exception4) {
            out = "mei you wo zhen mei you.";
        }
        return out;
    }

    public String type6(Bookshelf[] bookshelves,int i,int j) {
        for (int m = 0; m < bookshelves[j - 1].get().size();m++) {
            Bookset book1 = bookshelves[j - 1].get().get(m);
            for (int n = 0; n < bookshelves[i - 1].get().size();n++) {
                Bookset book2 = bookshelves[i - 1].get().get(n);
                if (book1.getName().equals(book2.getName())) {
                    if (book1.get_type() != book2.get_type()) {
                        return "Oh, no. We fail!";
                    }
                    else if (book1.information().equals(book2.information())) {
                        continue;
                    }
                    else {
                        return "Oh, no. We fail!";
                    }
                }
            }
        }
        return String.valueOf(bookshelves.length + 1);
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

    public Bookset create(Bookset book1) {
        Bookset bookset = book1;
        String name = book1.getName();
        double price = book1.getPrice();
        long num = book1.getNum();
        if (book1.get_type() == 1) {
            bookset = new Other(name,price,num);
        }
        else if (book1.get_type() == 2) {
            bookset = new OtherA(name,price,num,book1.getAge());
        }
        else if (book1.get_type() == 3) {
            bookset = new OtherS(name,price,num,book1.getYear());
        }
        else if (book1.get_type() == 4) {
            bookset = new Novel(name,price,num,book1.getAge(),book1.isFinish());
        }
        else if (book1.get_type() == 5) {
            bookset = new Poetry(name,price,num,book1.getAge(),book1.getAuthor());
        }
        else if (book1.get_type() == 6) {
            bookset = new Math(name,price,num,book1.getYear(),book1.getGrade());
        }
        else {
            bookset = new Computer(name,price,num,book1.getYear(),book1.getSpecial());
        }
        return bookset;

    }

    public void set(Bookshelf bookshelf) {
        for (int k = 0; k < bookshelf.get().size(); k++) {
            Bookset book1 = bookshelf.get().get(k);
            if (book1.get_type() == 1) {
                bookshelf.set1();
            }
            else if (book1.get_type() == 2) {
                bookshelf.set2();
            }
            else if (book1.get_type() == 3) {
                bookshelf.set3();
            }
            else if (book1.get_type() == 4) {
                bookshelf.set4();
            }
            else if (book1.get_type() == 5) {
                bookshelf.set5();
            }
            else if (book1.get_type() == 6) {
                bookshelf.set6();
            }
            else {
                bookshelf.set7();
            }
        }
    }

    public Bookshelf[] update6(String out,Bookshelf[] bookshelves,int i,int j) {
        MainClass mainClass = new MainClass();
        if (out.equals("Oh, no. We fail!")) {
            return bookshelves;
        }
        Bookshelf bookshelf = new Bookshelf();
        int l = bookshelves[i - 1].get().size();
        for (int m = 0; m < bookshelves[i - 1].get().size();m++) {
            try {
                Bookset book1 = bookshelves[i - 1].get().get(m);
                Bookset bookset = mainClass.create(book1);
                bookshelf.add_shelf(bookset);
            } catch (Exception3 e) {
                continue;
            }
        }
        for (int m = 0; m < bookshelves[j - 1].get().size();m++) {
            Bookset book1 = bookshelves[j - 1].get().get(m);
            int n;
            for (n = 0; n < l;n++) {
                Bookset book2 = bookshelf.get().get(n);
                if (book1.getName().equals(book2.getName())) {
                    if (book1.information().equals(book2.information())) {
                        bookshelf.get().get(n).setNum(book1.getNum() + book2.getNum());
                        break;
                    }
                }
            }
            if (n == l) {
                try {
                    Bookset bookset = mainClass.create(book1);
                    bookshelf.add_shelf(bookset);
                } catch (Exception e) {
                    continue;
                }
            }
        }
        Bookshelf[] bookshelves1 = Arrays.copyOf(bookshelves,bookshelves.length + 1);
        mainClass.set(bookshelf);
        bookshelves1[bookshelves1.length - 1] = bookshelf;
        return bookshelves1;
    }

    public String[] update4(String result,String[] print,int j,String name) {
        MainClass mainClass = new MainClass();
        if (result.equals("")) {
            return print;
        }
        else {
            String[] print1 = mainClass.judge(print,j);
            print1[print1.length - 1] = "Oh, no! The " + name + " exist.";
            return print1;
        }
    }

}
