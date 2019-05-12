package entities;

import org.json.JSONArray;
import org.json.JSONObject;
import enums.CandidateDisposition;

import java.util.LinkedList;
import java.util.List;

public class K2Candidate {
    public static final String JSON_EPIC_NAME = "epic_candname";
    public static final String JSON_PL_NAME = "pl_name";
    public static final String JSON_CAND_NAME = "epic_candname";
    public static final String JSON_PL_DISPOSITION = "k2c_disp";
    public static final String JSON_PL_ORBITAL_PERIOD = "pl_orbper";
    public static final String JSON_PL_RAD_J = "pl_radj";
    public static final String JSON_PL_EQT = "pl_eqt";
    public static final String JSON_PL_FPP_PROB = "pl_fppprob";

    String epicName;
    String plName;
    String candName;
    CandidateDisposition plDisposition;
    Double plOrbitalPeriod;
    Double plRadj;
    Double plEqt;
    Double plFppProp;

    public K2Candidate(String epicName, String plName, String candName, CandidateDisposition disposition, Double plOrbitalPeriod, Double plRadj, Double plEqt, Double plFppProp) {
        this.epicName = epicName;
        this.plName = plName;
        this.candName = candName;
        this.plDisposition = disposition;
        this.plOrbitalPeriod = plOrbitalPeriod;
        this.plRadj = plRadj;
        this.plEqt = plEqt;
        this.plFppProp = plFppProp;
    }

    public String getEpicName() {
        return epicName;
    }

    public void setEpicName(String epicName) {
        this.epicName = epicName;
    }

    public String getPlName() {
        return plName;
    }

    public void setPlName(String plName) {
        this.plName = plName;
    }

    public String getCandName() {
        return candName;
    }

    public void setCandName(String candName) {
        this.candName = candName;
    }

    public CandidateDisposition getPlDisposition() {
        return plDisposition;
    }

    public void setPlDisposition(CandidateDisposition plDisposition) {
        this.plDisposition = plDisposition;
    }

    public Double getPlOrbitalPeriod() {
        return plOrbitalPeriod;
    }

    public void setPlOrbitalPeriod(Double plOrbitalPeriod) {
        this.plOrbitalPeriod = plOrbitalPeriod;
    }

    public Double getPlRadj() {
        return plRadj;
    }

    public void setPlRadj(Double plRadj) {
        this.plRadj = plRadj;
    }

    public Double getPlEqt() {
        return plEqt;
    }

    public void setPlEqt(Double plEqt) {
        this.plEqt = plEqt;
    }

    public Double getPlFppProp() {
        return plFppProp;
    }

    public void setPlFppProp(Double plFppProp) {
        this.plFppProp = plFppProp;
    }

    public static K2Candidate parseFromJson(JSONObject json) {
        String epicName = null;
        String plName = null;
        String candName = null;
        CandidateDisposition plDisposition = null;
        Double plOrbitalPeriod = null;
        Double plRadj = null;
        Double plEqt = null;
        Double plFppProp = null;

        if (!json.isNull(JSON_EPIC_NAME)) {
            epicName = json.getString(JSON_EPIC_NAME);
        }
        if (!json.isNull(JSON_PL_NAME)) {
            plName = json.getString(JSON_PL_NAME);
        }
        if (!json.isNull(JSON_CAND_NAME)) {
            candName = json.getString(JSON_CAND_NAME);
        }
        if (!json.isNull(JSON_PL_DISPOSITION)) {
            if (json.getString(JSON_PL_DISPOSITION).compareTo("CONFIRMED") == 0) {
                plDisposition = CandidateDisposition.CONFIRMED;
            }else if (json.getString(JSON_PL_DISPOSITION).compareTo("CANDIDATE") == 0) {
                plDisposition = CandidateDisposition.CANDIDATE;
            }else if (json.getString(JSON_PL_DISPOSITION).compareTo("FALSE POSITIVE") == 0) {
                plDisposition = CandidateDisposition.FALSE_POSITIVE;
            }
        }
        if (!json.isNull(JSON_PL_ORBITAL_PERIOD)) {
            plOrbitalPeriod = json.getDouble(JSON_PL_ORBITAL_PERIOD);
        }
        if (!json.isNull(JSON_PL_RAD_J)) {
            plRadj = json.getDouble(JSON_PL_RAD_J);
        }
        if (!json.isNull(JSON_PL_EQT)) {
            plEqt = json.getDouble(JSON_PL_EQT);
        }
        if (!json.isNull(JSON_PL_FPP_PROB)) {
            plFppProp = json.getDouble(JSON_PL_FPP_PROB);
        }

        K2Candidate c = new K2Candidate(epicName, plName, candName, plDisposition, plOrbitalPeriod, plRadj, plEqt, plFppProp);

        return c;
    }

    public static List<K2Candidate> parseFromJson(JSONArray array) {
        List<K2Candidate> list = new LinkedList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(parseFromJson(array.getJSONObject(i)));
        }
        return list;
    }
}
