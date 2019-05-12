import controller.CandidateCheckController;
import controller.DistributionController;
import controller.K2DataController;
import entities.CnnVersion;
import entities.ConfirmedExoStat;
import entities.LAEvaluation;
import entities.User;
import facades.*;
import org.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.*;
import java.util.List;

@Startup
@Singleton
public class InitBean {
    //"C:\Program Files\Java\jdk1.8.0_40\db\bin\"startNetworkServer.bat -noSecurityManager
    @Inject
    UserFacade userFacade;
    @Inject
    CnnVersionFacade cnnVersionFacade;
    @Inject
    LAEvaluationFacade evaluationFacade;
    @Inject
    ExoplanetStatFacade exoplanetStatFacade;
    @Inject
    ConfirmedExoStatFacade confirmedExoStatFacade;
    @Inject
    CandidateCheckController candidateCheckController;

    @PostConstruct
    public void init() {
        System.out.println("  I N I T  ");

        createTestData();

        DistributionController dataController = DistributionController.getInstance();
        dataController.setCampaigns(K2DataController.getAllCampaigns());
        dataController.startThread();
        System.out.println();
    }

    public void createTestData() {

        exoplanetStatFacade.findLatestExoplanetStatAndUpdate();

        // User data
        User testUser = new User();
        testUser.setId(1);
        testUser.setUsername("Bob");
        userFacade.save(testUser);

        testUser = new User();
        testUser.setId(2);
        testUser.setUsername("Tom");
        userFacade.save(testUser);

        testUser = new User();
        testUser.setId(3);
        testUser.setUsername("Bern");
        userFacade.save(testUser);

        //User Report
        // TODO

        // CnnVersion data
        CnnVersion cv = new CnnVersion("1.0-beta", "1.0-beta.h5", "01.01.2018 12:00:00");
        cnnVersionFacade.save(cv);
        cv = new CnnVersion("2.0-beta", "2.0-beta.h5", "02.01.2018 12:00:00");
        cnnVersionFacade.save(cv);
        cv = new CnnVersion("3.0-beta", "3.0-beta.h5", "03.01.2018 12:00:00");
        cnnVersionFacade.save(cv);

        // Evaluation data
        readEvaluationJsonFile("k2-10.json", false);
        readEvaluationJsonFile("k2-30.json", false);
        readEvaluationJsonFile("k2-45.json", false);

        readEvaluationJsonFile("k2-10-add1.json", false);

        List<ConfirmedExoStat> confList = ConfirmedExoStatFacade.loadAndPersistData();
        confirmedExoStatFacade.saveRange(confList);
    }

    public void readEvaluationJsonFile(String fileName, boolean addAdditional) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("testdata/" + fileName).getFile());

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            JSONObject json = new JSONObject(sb.toString());
            if(!addAdditional) {
                evaluationFacade.addSingleComputed(json);
            } else {
                evaluationFacade.addSingleCustom(json);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
