

public class Course {
	private final String id;
	private final String name;

	public Course(String id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public int hashCode() {
		int result = 2;
		result = 31 * result + (id == null ? 0 : id.hashCode());
		result = 31 * result + (name == null ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Course) {
			Course course = (Course) obj;
			return course.id.equals(this.id) && course.name.equals(this.name);
		}
		else {
			return false;
		}
	}

	@Override
	public String toString() {
		return name;
	}
}
