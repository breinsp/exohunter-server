package facades;

import controller.K2DataController;
import entities.CnnVersion;
import entities.TrainData;
import entities.UserReport;
import org.json.JSONArray;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

@Stateless
public class UserReportFacade {
    private static final long NUM_OF_USER_REPORTS_FOR_CLASSIFICATION = 2;
    private static final int NUM_OF_MIN_CNN_UPGRADE_USER_REPORTS = 1;
    private static final double ADD_OFFSET = 0.01;

    @PersistenceContext
    EntityManager em;
    @Inject
    CnnVersionFacade cnnVersionFacade;
    @Inject
    TrainDataFacade trainDataFacade;

    public List<UserReport> findAll() {
        return em.createNamedQuery("UserReport.findAll").getResultList();
    }

    public UserReport findById(Long id) {
        List<UserReport> ur = em.createNamedQuery("UserReport.findById").setParameter("id", id).getResultList();

        if(ur.size() > 0) {
            return ur.get(0);
        } else {
            return null;
        }
    }

    public List<UserReport> findByStar(int epic) {
        return em.createNamedQuery("UserReport.findByStar").setParameter("epic", epic).getResultList();
    }

    public List<UserReport> findByStarUnused(int epic) {
        return em.createNamedQuery("UserReport.findByStarUnused").setParameter("epic", epic).getResultList();
    }

    public int getCntOfStarUserReports(int epic) {
        Long cnt = (Long)em.createNamedQuery("UserReport.getCntOfStarUserReports").setParameter("epic", epic).getSingleResult();
        return cnt.intValue();
    }

    public void save(UserReport ur) {
        em.persist(ur);
    }

    public void merge(UserReport ur) { em.merge(ur); }

    public JSONArray listOfObjectToJSON(List<UserReport> list) {
        JSONArray arr = new JSONArray();
        for (UserReport ur : list) {
            arr.put(ur.parseToJson());
        }
        return arr;
    }

    public void checkForCnnUpdate() {
        List<Object[]> columns = em.createNamedQuery("UserReport.findUpgradeableData").setParameter("cnt", NUM_OF_USER_REPORTS_FOR_CLASSIFICATION).getResultList();
        if(columns.size() >= NUM_OF_MIN_CNN_UPGRADE_USER_REPORTS) {
            for(Object[] colRow: columns) {
                int epic = (int)colRow[0];
                Long cnt = (Long)colRow[1];
                List<UserReport> unusedUserReports = findByStarUnused(epic);
                double resClassification = 0;
                for(UserReport ur : unusedUserReports) {
                    ur.setUsed(true);
                    merge(ur);
                    resClassification += ur.getUserClassification();
                }
                resClassification /= unusedUserReports.size();
                resClassification /= 100;
                int result = (int)Math.round(resClassification);

                TrainData td = trainDataFacade.findByStar(epic);

                if(td == null) {
                    List<Float> list = getSeqFromEpic(epic);
                    TrainData newTd = new TrainData(epic, result, list);
                    trainDataFacade.save(newTd);
                } else {
                    td.setClassification(result);
                    trainDataFacade.merge(td);
                }
            }
            trainCnn();
        }
    }

    public List<Float> getSeqFromEpic(int epic) {
        List<Float> list = new LinkedList<>();
        try {
            int campaign = K2DataController.getCampaignForEpicNr(epic);
            String strUrl = K2DataController.getLightCurveURL(epic, campaign);
            try {
                URL url = new URL(strUrl);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                StringBuffer response = new StringBuffer();

                while ((line = in.readLine()) != null) {
                    response.append(line + "\n");
                }
                in.close();

                String[] lines = response.toString().split("\n");
                int i = 0;
                for(String curLine: lines) {
                    String[] parts = curLine.split("[,]");
                    if(i > 0) {
                        list.add(Float.parseFloat(parts[1]));
                    }
                    i++;
                }
                return list;
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

    public void trainCnn() {
        CnnVersion cv = cnnVersionFacade.findLatestVersion();

        double oldNom = Double.parseDouble(cv.getVersionText().split("[-]")[0]);
        String newVersionText = (oldNom + ADD_OFFSET) + "-beta";

        // TODO: train network
    }
}
