package entities;

import enums.CandidateDisposition;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "K2LACandidateMatchingResult.findAll", query = "select c from K2LACandidateMatchingResult c"),
        @NamedQuery(name = "K2LACandidateMatchingResult.findById", query = "select c from K2LACandidateMatchingResult c where c.id=:id"),
        @NamedQuery(name = "K2LACandidateMatchingResult.findByCandidateName", query = "select c from K2LACandidateMatchingResult c where c.candidateName=:candidateName"),
        @NamedQuery(name = "K2LACandidateMatchingResult.findByStar", query = "select c from K2LACandidateMatchingResult c where c.match.star.epic=:epic"),
})
public class K2LACandidateMatchingResult {
    public static final String JSON_ID = "id";
    public static final String JSON_CANDIDATE_NAME = "candidateName";
    public static final String JSON_PLANET_NAME = "planetName";
    public static final String JSON_DISPOSITION = "disposition";
    public static final String JSON_ORBITAL_MATCH_PERCENTAGE = "orbitalMatchPercentage";
    public static final String JSON_RADIUS_JUPITER_MATCH_PERCENTAGE = "radiusJupiterMatchPercentage";
    public static final String JSON_EQT_MATCH_PERCENTAGE = "eqtMatchPercentage";
    public static final String JSON_MATCH = "match";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String candidateName;
    String planetName;
    CandidateDisposition disposition;
    Double orbitalMatchPercentage;
    Double radiusJupiterMatchPercentage;
    Double eqtMatchPercentage;
    @ManyToOne
    LACandidate match;

    @Transient
    public static final Comparator<K2LACandidateMatchingResult> PERCENTAGE_COMPERATOR = new Comparator<K2LACandidateMatchingResult>() {
        @Override
        public int compare(K2LACandidateMatchingResult o1, K2LACandidateMatchingResult o2) {
            return Double.compare(o1.getFullPercentageError(), o2.getFullPercentageError());
        }
    };

    public K2LACandidateMatchingResult() {
        this.orbitalMatchPercentage = null;
        this.radiusJupiterMatchPercentage = null;
        this.eqtMatchPercentage = null;
    }

    public K2LACandidateMatchingResult(Double orbitalMatchPercentage, Double radiusJupiterMatchPercentage, Double eqtMatchPercentage, LACandidate match) {
        this.orbitalMatchPercentage = orbitalMatchPercentage;
        this.radiusJupiterMatchPercentage = radiusJupiterMatchPercentage;
        this.eqtMatchPercentage = eqtMatchPercentage;
        this.match = match;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }

    public CandidateDisposition getDisposition() {
        return disposition;
    }

    public void setDisposition(CandidateDisposition disposition) {
        this.disposition = disposition;
    }

    public Double getOrbitalMatchPercentage() {
        return orbitalMatchPercentage;
    }

    public void setOrbitalMatchPercentage(Double orbitalMatchPercentage) {
        this.orbitalMatchPercentage = orbitalMatchPercentage;
    }

    public Double getRadiusJupiterMatchPercentage() {
        return radiusJupiterMatchPercentage;
    }

    public void setRadiusJupiterMatchPercentage(Double radiusJupiterMatchPercentage) {
        this.radiusJupiterMatchPercentage = radiusJupiterMatchPercentage;
    }

    public Double getEqtMatchPercentage() {
        return eqtMatchPercentage;
    }

    public void setEqtMatchPercentage(Double eqtMatchPercentage) {
        this.eqtMatchPercentage = eqtMatchPercentage;
    }

    public LACandidate getMatch() {
        return match;
    }

    public void setMatch(LACandidate match) {
        this.match = match;
    }

    public double getFullPercentageError() {
        double error = 0;
        error += (getOrbitalMatchPercentage() != null) ? getOrbitalMatchPercentage() : 0;
        error += (getRadiusJupiterMatchPercentage() != null) ? getRadiusJupiterMatchPercentage() : 0;
        error += (getEqtMatchPercentage() != null) ? getEqtMatchPercentage() : 0;
        return error;
    }

    public JSONObject parseToJson() {
        JSONObject json = new JSONObject();

        json.put(JSON_ID, id);
        json.put(JSON_CANDIDATE_NAME, candidateName);
        json.put(JSON_PLANET_NAME, planetName);
        json.put(JSON_DISPOSITION, disposition);
        json.put(JSON_ORBITAL_MATCH_PERCENTAGE, orbitalMatchPercentage);
        json.put(JSON_RADIUS_JUPITER_MATCH_PERCENTAGE, radiusJupiterMatchPercentage);
        json.put(JSON_EQT_MATCH_PERCENTAGE, eqtMatchPercentage);
        //json.put(JSON_MATCH, match.parseToJson());

        return json;
    }
}
