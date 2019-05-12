package entities;

import facades.UserFacade;
import org.hibernate.annotations.CreationTimestamp;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "LAEvaluation.findAll", query = "select e from LAEvaluation e"),
        @NamedQuery(name = "LAEvaluation.findById", query = "select e from LAEvaluation e where e.id = :id"),
        @NamedQuery(name = "LAEvaluation.findByStar", query = "select e from LAEvaluation e where e.star.epic = :epic order by e.creationTimeStamp desc"),
        @NamedQuery(name = "LAEvaluation.findByUserIdAndStar", query = "select e from LAEvaluation e where e.star.epic=:epic and e.user.id=:userId")
})
public class LAEvaluation {
    private static final String JSON_ID = "id";
    private static final String JSON_STAR = "star";
    private static final String JSON_CANDIDATES = "candidates";
    private static final String JSON_USER = "user";
    private static final String JSON_USERID = "userId";
    private static final String JSON_CREATIONTIMESTAMP= "creationTimeStamp";
    private static final String JSON_CLASSIFICATIONRESULT= "classificationResult";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Double classificationResult;
    LocalDateTime creationTimeStamp;
    @ManyToOne
    User user;
    @ManyToOne
    Star star;
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "evaluation")
    List<LACandidate> candidates;

    public LAEvaluation() {
    }

    public LAEvaluation(Star star, List<LACandidate> candidates) {
        this.star = star;
        this.candidates = candidates;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Star getStar() {
        return star;
    }

    public void setStar(Star star) {
        this.star = star;
    }

    public List<LACandidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<LACandidate> candidates) {
        this.candidates = candidates;
    }

    public Double getClassificationResult() {
        return classificationResult;
    }

    public void setClassificationResult(Double classificationResult) {
        this.classificationResult = classificationResult;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreationTimeStamp() {
        return creationTimeStamp;
    }

    public void setCreationTimeStamp(LocalDateTime creationTimeStamp) {
        this.creationTimeStamp = creationTimeStamp;
    }

    public JSONObject parseToJson() {
        JSONObject json = new JSONObject();
        if(star != null)
            json.put(JSON_STAR, star.parseToJson());

        JSONArray arr = new JSONArray();
        for (LACandidate c : candidates) {
            arr.put(c.parseToJson());
        }
        json.put(JSON_ID, id);
        json.put(JSON_CANDIDATES, arr);
        json.put(JSON_USERID, user.getId());
        json.put("userName", user.getUsername());
        json.put(JSON_CLASSIFICATIONRESULT, classificationResult);
        json.put(JSON_CREATIONTIMESTAMP, creationTimeStamp.format(dtf));

        return json;
    }

    public static LAEvaluation convertFromJson(JSONObject json, User user) {

        Star star = Star.createFromJson(json.getJSONObject(JSON_STAR));
        List<LACandidate> candidates = new LinkedList<>();
        Long id = null;
        Double classificationResult = null;

        JSONArray arr = json.getJSONArray(JSON_CANDIDATES);
        for(int i = 0; i < arr.length(); i++) {
            LACandidate candidate = LACandidate.createFromJson(arr.getJSONObject(i), star);
            candidates.add(candidate);
        }
        if (json.has(JSON_ID)) {
            id = json.getLong(JSON_ID);
        }
        if(json.has(JSON_CLASSIFICATIONRESULT)) {
            classificationResult = json.getDouble(JSON_CLASSIFICATIONRESULT);
        }

        LAEvaluation evaluation = new LAEvaluation(star, candidates);
        evaluation.setId(id);
        evaluation.setUser(user);
        evaluation.setClassificationResult(classificationResult);
        if(json.has(JSON_CREATIONTIMESTAMP)) {
            evaluation.setCreationTimeStamp(LocalDateTime.parse(json.getString(JSON_CREATIONTIMESTAMP), dtf));
        } else {
            String curStr = LocalDateTime.now().format(dtf);
            evaluation.setCreationTimeStamp(LocalDateTime.parse(curStr, dtf));
        }

        return evaluation;
    }
}
