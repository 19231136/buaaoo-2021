import java.math.BigDecimal;
import java.util.ArrayList;

public class Bookshelf {
    private ArrayList<Bookset> booksetArrayList = new ArrayList<>();

    public void add_shelf(Bookset bookset) {
        booksetArrayList.add(bookset);
    }

    public BigDecimal high_price() {
        BigDecimal max = booksetArrayList.get(0).getPrice();
        for (int i = 1;i < booksetArrayList.size();i++) {
            if (booksetArrayList.get(i).getPrice().compareTo(max) == 1) {
                max = booksetArrayList.get(i).getPrice();
            }
        }
        return max;
    }

    public BigDecimal sum() {
        BigDecimal sum = new BigDecimal(0);
        for (int i = 0;i < booksetArrayList.size();i++) {
            sum = sum.add(booksetArrayList.get(i).sum());
        }
        return sum;
    }

    public int remove_shelf(String name) {
        for (int i = 0;i < booksetArrayList.size();i++) {
            if (booksetArrayList.get(i).getName().equals(name)) {
                booksetArrayList.remove(i);
            }
        }
        int sum = 0;
        for (int i = 0;i < booksetArrayList.size();i++) {
            sum += booksetArrayList.get(i).getNum();
        }
        return sum;
    }
}
