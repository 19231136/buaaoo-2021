

import java.util.Objects;

public class CourseSelectionRecord {
	private final Student stu;
	private final Course c;

	public CourseSelectionRecord(Student stu, Course c) {
		this.stu = stu;
		this.c = c;
	}

	@Override
	public int hashCode() {
		int result = 2;
		result = 31 * result + (stu == null ? 0 : stu.hashCode());
		result = 31 * result + (c == null ? 0 : c.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CourseSelectionRecord) {
			CourseSelectionRecord co = (CourseSelectionRecord) obj;
			return co.c.equals(this.c) && co.stu.equals(this.stu);
		}
		else {
			return false;
		}
	}

	@Override
	public String toString() {
		return stu.toString() + " selects " + c.toString();
	}
}
