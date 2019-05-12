package entities;

import org.json.JSONObject;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@NamedQueries({
        @NamedQuery(name="CnnVersion.findAll", query = "select cv from CnnVersion cv"),
        @NamedQuery(name="CnnVersion.findById", query = "select cv from CnnVersion cv where cv.id=:id"),
        @NamedQuery(name="CnnVersion.findLatestVersion", query = "select cv from CnnVersion cv where cv.timeStamp=(select max(o.timeStamp) from CnnVersion o)")
})
public class CnnVersion {
    private static final String JSON_ID = "id";
    private static final String JSON_VERSION_TEXT = "versionText";
    private static final String JSON_PATH_TO_FILE = "pathToFile";
    private static final String JSON_TIMESTAMP = "timeStamp";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String versionText;
    String pathToFile;
    LocalDateTime timeStamp;

    public CnnVersion() {
    }

    public CnnVersion(String versionText, String pathToFile, LocalDateTime timeStamp) {
        this.versionText = versionText;
        this.pathToFile = pathToFile;
        this.timeStamp = timeStamp;
    }

    public CnnVersion(String versionText, String pathToFile, String timeStampStr) {
        this.versionText = versionText;
        this.pathToFile = pathToFile;
        this.timeStamp = LocalDateTime.parse(timeStampStr, dtf);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersionText() {
        return versionText;
    }

    public void setVersionText(String versionText) {
        this.versionText = versionText;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public JSONObject parseToJson() {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, id);
        json.put(JSON_VERSION_TEXT, versionText);
        json.put(JSON_PATH_TO_FILE, pathToFile);
        json.put(JSON_TIMESTAMP, timeStamp.format(dtf));

        return json;
    }
}
