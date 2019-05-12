package entities;

import org.json.JSONObject;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@NamedQueries({
        @NamedQuery(name = "UserReport.findAll", query = "select ur from UserReport ur"),
        @NamedQuery(name = "UserReport.findById", query = "select ur from UserReport ur where ur.id=:id"),
        @NamedQuery(name = "UserReport.findByStar", query = "select ur from UserReport ur where ur.epic=:epic"),
        @NamedQuery(name = "UserReport.findByStarUnused", query = "select ur from UserReport ur where ur.epic=:epic and ur.used=false"),
        @NamedQuery(name = "UserReport.getCntOfStarUserReports", query = "select COUNT(ur) from UserReport ur where ur.epic=:epic"),
        @NamedQuery(name = "UserReport.findUpgradeableData", query = "select ur.epic, count(ur) from UserReport ur where ur.used=false group by ur.epic having count(ur) >=:cnt")
})
public class UserReport {
    private static final String JSON_ID = "id";
    private static final String JSON_EPIC = "epic";
    private static final String JSON_TIMESTAMP = "timeStamp";
    private static final String JSON_OLD_CLASSIFICATION = "oldClassification";
    private static final String JSON_USER_CLASSIFICATION = "userClassification";
    private static final String JSON_USED = "used";
    private static final String JSON_USER = "user";
    private static final String JSON_USER_ID = "userId";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    int epic;
    LocalDateTime timeStamp;
    double oldClassification;
    double userClassification;
    boolean used;
    @OneToOne(fetch = FetchType.EAGER)
    User user;

    public UserReport() {
    }

    public UserReport(int epic, LocalDateTime timeStamp, double oldClassification, double userClassification) {
        this.epic = epic;
        this.timeStamp = timeStamp;
        this.oldClassification = oldClassification;
        this.userClassification = userClassification;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getEpic() {
        return epic;
    }

    public void setEpic(int epic) {
        this.epic = epic;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getOldClassification() {
        return oldClassification;
    }

    public void setOldClassification(double oldClassification) {
        this.oldClassification = oldClassification;
    }

    public double getUserClassification() {
        return userClassification;
    }

    public void setUserClassification(double userClassification) {
        this.userClassification = userClassification;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public JSONObject parseToJson() {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, id);
        json.put(JSON_EPIC, epic);
        json.put(JSON_TIMESTAMP, timeStamp.format(dtf));
        json.put(JSON_OLD_CLASSIFICATION, oldClassification);
        json.put(JSON_USER_CLASSIFICATION, userClassification);
        json.put(JSON_USED, used);
        json.put(JSON_USER_ID, user.getId());

        return json;
    }

    public static UserReport parseJsonToObject(JSONObject json, User user) {
        Long id = null;
        int epic = -1;
        LocalDateTime timeStamp = LocalDateTime.parse(LocalDateTime.now().format(dtf), dtf);
        double oldClassification = -1;
        double userClassification = -1;

        if(json.has(JSON_ID)) {
            id = json.getLong(JSON_ID);
        }
        if(json.has(JSON_EPIC)) {
            epic = json.getInt(JSON_EPIC);
        }
        if(json.has(JSON_TIMESTAMP)) {
            timeStamp = LocalDateTime.parse(json.getString(JSON_TIMESTAMP), dtf);
        }
        if(json.has(JSON_OLD_CLASSIFICATION)) {
            oldClassification = json.getDouble(JSON_OLD_CLASSIFICATION);
        }
        if(json.has(JSON_USER_CLASSIFICATION)) {
            userClassification = json.getDouble(JSON_USER_CLASSIFICATION);
        }

        UserReport ur = new UserReport(epic, timeStamp, oldClassification, userClassification);
        ur.setId(id);
        ur.setUser(user);

        return ur;
    }
}
