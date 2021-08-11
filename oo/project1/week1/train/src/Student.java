

public class Student {
	private final String id;
	private final String name;

	public Student(String id, String name) {
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
		if (obj instanceof Student) {
			Student student = (Student) obj;
			return student.id.equals(this.id) && student.name.equals(this.name);
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
