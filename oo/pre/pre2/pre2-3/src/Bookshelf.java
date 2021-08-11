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

    //1.查询属性
    public String search(String name) {
        for (int i = 0;i < booksetArrayList.size();i++) {
            if (booksetArrayList.get(i).getName().equals(name)) {
                String information = booksetArrayList.get(i).get_information();
                return information;
            }
        }
        return "";
    }

    //2.查询种类
    public int num() {
        return have1 + have2 + have3 + have4 + have5 + have6 + have7;
    }

    //3.查询总数
    public int sum() {
        int sum = 0;
        for (int i = 0;i < booksetArrayList.size();i++) {
            sum += booksetArrayList.get(i).getNum();
        }
        return sum;
    }

    //4.添加书籍
    public void add_shelf(Bookset bookset) {
        booksetArrayList.add(bookset);
    }

    //5.移出书架
    public int remove_shelf(String name) {
        for (int i = 0;i < booksetArrayList.size();i++) {
            if (booksetArrayList.get(i).getName().equals(name)) {
                int type = booksetArrayList.get(i).get_type();
                int j = 0;
                booksetArrayList.remove(i);
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
        }
        int sum = 0;
        for (int i = 0;i < booksetArrayList.size();i++) {
            sum += booksetArrayList.get(i).getNum();
        }
        return sum;
    }
}

