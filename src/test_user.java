import java.io.Serializable;


public class test_user implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private int age;
    private String gender;

    test_user() {
    };

    test_user(String name, int age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Name:" + name + "\nAge: " + age + "\nGender: " + gender;
    }
}