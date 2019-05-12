package entities;

import org.json.JSONObject;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "TrainData.findByEpic", query = "select td from TrainData td where td.epic=:epic")
})
public class TrainData {
    private static final String JSON_ID = "id";
    private static final String JSON_EPIC = "epic";
    private static final String JSON_CLASSIFICATION = "classification";
    private static final String JSON_SEQ = "seq";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    int epic;
    int classification;
    @ElementCollection(fetch = FetchType.EAGER)
    List<Float> seq;

    public TrainData() {
    }

    public TrainData(int epic, int classification, List<Float> seq) {
        this.epic = epic;
        this.classification = classification;
        this.seq = seq;
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

    public int getClassification() {
        return classification;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }

    public List<Float> getSeq() {
        return seq;
    }

    public void setSeq(List<Float> seq) {
        this.seq = seq;
    }

    public JSONObject parseToJson() {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, id);
        json.put(JSON_EPIC, epic);
        json.put(JSON_CLASSIFICATION, classification);
        json.put(JSON_SEQ, seq);

        return json;
    }

    public static TrainData csvLineToObject(String line) {
        TrainData td = new TrainData();
        String[] parts = line.split("[;]");

        td.setEpic(Integer.parseInt(parts[0]));
        td.setClassification(Integer.parseInt(parts[1]));

        String[] arrParts = parts[2].split("[,]");
        List<Float> list = new LinkedList<>();
        for(String val: arrParts) {
            list.add(Float.parseFloat(val));
        }
        td.setSeq(list);

        return td;
    }
}
