package entities;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name="User.findById", query = "SELECT u FROM User u WHERE u.id=:userId")
})
public class User {

    @Id
    private long id;
    private String username;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public User(long id, String username) {
        this.id = id;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
