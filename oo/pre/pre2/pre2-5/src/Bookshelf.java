import java.util.ArrayList;

public class Bookshelf {
    private ArrayList<Bookset> booksetArrayList = new ArrayList<>();
    private int have1;
    private int have2;
    private int have3;
    private int have4;
    private int have5;
    private int have6;
    private int have7;

    public void set1() {
        have1 = 1;
    }

    public void set2() {
        have2 = 1;
    }

    public void set3() {
        have3 = 1;
    }

    public void set4() {
        have4 = 1;
    }

    public void set5() {
        have5 = 1;
    }

    public void set6() {
        have6 = 1;
    }

    public void set7() {
        have7 = 1;
    }

    public ArrayList<Bookset> get() {
        return booksetArrayList;
    }

    //1.查询属性
    public String search(String name)throws Exception1,Exception2 {
        int i = 0;
        if (booksetArrayList.isEmpty()) {
            throw new Exception2();
        }
        for (i = 0;i < booksetArrayList.size();i++) {
            if (booksetArrayList.get(i).getName().equals(name)) {
                String information = booksetArrayList.get(i).get_information();
                return information;
            }
        }
        if (i == booksetArrayList.size()) {
            throw new Exception1();
        }
        return "";
    }

    //2.查询种类
    public int num()throws Exception2 {
        if (booksetArrayList.isEmpty()) {
            throw new Exception2();
        }
        return have1 + have2 + have3 + have4 + have5 + have6 + have7;
    }

    //3.查询总数
    public long sum()throws Exception2 {
        long sum = 0;
        if (booksetArrayList.isEmpty()) {
            throw new Exception2();
        }
        for (int i = 0;i < booksetArrayList.size();i++) {
            sum += booksetArrayList.get(i).getNum();
        }
        return sum;
    }

    //4.添加书籍
    public void add_shelf(Bookset bookset)throws Exception3 {
        for (int i = 0;i < booksetArrayList.size();i++) {
            if (booksetArrayList.get(i).getName().equals(bookset.getName())) {
                throw new Exception3();
            }
        }
        booksetArrayList.add(bookset);
    }

    //5.移出书架
    public long remove_shelf(String name)throws Exception4 {
        int i = 0;
        int type = 0;
        int l = booksetArrayList.size();
        for (i = 0;i < booksetArrayList.size();i++) {
            if (booksetArrayList.get(i).getName().equals(name)) {
                type = booksetArrayList.get(i).get_type();
                booksetArrayList.remove(i);
                break;
            }
        }
        if (i == l) {
            throw new Exception4();
        }
        else {
            int j = 0;
            for (j = 0;j < booksetArrayList.size();j++) {
                if (booksetArrayList.get(j).get_type() == type) {
                    break;
                }
            }
            if (j == booksetArrayList.size()) {
                if (type == 1) {
                    have1 = 0;
                }
                else if (type == 2) {
                    have2 = 0;
                }
                else if (type == 3) {
                    have3 = 0;
                }
                else if (type == 4) {
                    have4 = 0;
                }
                else if (type == 5) {
                    have5 = 0;
                }
                else if (type == 6) {
                    have6 = 0;
                }
                else {
                    have7 = 0;
                }
            }
        }
        long sum = 0;
        for (i = 0;i < booksetArrayList.size();i++) {
            sum += booksetArrayList.get(i).getNum();
        }
        return sum;
    }

}

