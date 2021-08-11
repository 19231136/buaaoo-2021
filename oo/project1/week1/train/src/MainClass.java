
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class MainClass {
	public static void main(String[] args) throws Exception {
		Set<CourseSelectionRecord> recordSet = new HashSet<>();
		try (Scanner scan = new Scanner(System.in)) {

			while (scan.hasNext()) {
				String command = scan.nextLine();
				String[] strs = command.split(" ");
				if (strs.length != 5) {
					throw new Exception("command format wrong!");
				}
				String cmd = strs[0];
				String stuId = strs[1];
				String stuName = strs[2];
				String cId = strs[3];
				String cName = strs[4];
				if ("select".equals(cmd)) {
					recordSet.add(new CourseSelectionRecord(new Student(stuId, stuName), new Course(cId, cName)));
				} else if ("unselect".equals(cmd)) {
					recordSet.remove(new CourseSelectionRecord(new Student(stuId, stuName), new Course(cId, cName)));
				} else {
					throw new Exception("command format wrong!");
				}
			}
		}
		System.out.println(recordSet);
	}
}
