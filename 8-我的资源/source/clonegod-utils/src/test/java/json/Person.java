package json;
import java.util.Date;
import java.util.List;

public class Person {
	
	private long id;
	private String name;
	private int age;
	private Date birth = new Date();
	private List<Address> addresses;
	
	
	public Person() {
		super();
	}
	public Person(long id, String name, int age) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public List<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", age=" + age + ", birth=" + birth + ", addresses=" + addresses
				+ "]";
	}
	
}
