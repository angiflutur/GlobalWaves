package app.entities;

/**
 * JAVADOC
 */
public class User {
    private String username;
    private Integer age;
    private String city;

    /**
     * JAVADOC
     */
    public User() {
    }

    /**
     * JAVADOC
     */
    public User(final String username,
                final Integer age,
                final String city) {
        this.username = username;
        this.age = age;
        this.city = city;
    }

    /**
     * JAVADOC
     */
    public String getUsername() {
        return username;
    }

    /**
     * JAVADOC
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * JAVADOC
     */
    public Integer getAge() {
        return age;
    }

    /**
     * JAVADOC
     */
    public void setAge(final Integer age) {
        this.age = age;
    }

    /**
     * JAVADOC
     */
    public String getCity() {
        return city;
    }

    /**
     * JAVADOC
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * JAVADOC
     */
    @Override
    public String toString() {
        return "User{"
                + "username='" + username + '\''
                + ", age=" + age
                + ", city='" + city + '\''
                + '}';
    }
}
