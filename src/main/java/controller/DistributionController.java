package controller;

import background.EpicLoader;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DistributionController {

    public static final int CACHE_SIZE = 100;
    public static final int[] CAMPAIGN_BLACKLIST = new int[]{91, 111};

    private static DistributionController instance;

    List<String> cache;
    int[] campaigns;

    int currentCampaign = 0;
    int currentCampaignIndex = 0;
    int currentCacheIndex = 0;

    public static DistributionController getInstance() {
        if (instance == null) {
            instance = new DistributionController();
            instance.readCursorInformation();
        }
        return instance;
    }

    private DistributionController() {
        if (cache == null) {
            cache = Collections.synchronizedList(new ArrayList());
        }
    }

    //Returns a value from the cache
    public String getValue() {
        String val = cache.get(0);
        cache.remove(0);
        if (cache.size() == 20) { //cache getting small => load new entries
            startThread();
        }
        return val;
    }

    //Starts the Thread responsible for refilling the cache
    public void startThread() {
        Thread t = new Thread(new EpicLoader(this, currentCampaignIndex, currentCampaign, CACHE_SIZE));
        t.setDaemon(true);
        t.start();
    }

    public List<String> getCache() {
        return cache;
    }

    public void setCache(List<String> cache) {
        this.cache = cache;
    }

    public int[] getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(int[] campaigns) {
        this.campaigns = campaigns;
    }

    public int getCurrentCampaign() {
        return currentCampaign;
    }

    public void setCurrentCampaign(int currentCampaign) {
        this.currentCampaign = currentCampaign;
        saveCursorInformation();
    }

    public int getCurrentCampaignIndex() {
        return currentCampaignIndex;
    }

    public void setCurrentCampaignIndex(int currentCampaignIndex) {
        this.currentCampaignIndex = currentCampaignIndex;
        saveCursorInformation();
    }

    public int getCurrentCacheIndex() {
        return currentCacheIndex;
    }

    public void setCurrentCacheIndex(int currentCacheIndex) {
        this.currentCacheIndex = currentCacheIndex;
    }

    private void saveCursorInformation() {
        try {
            List<String> lines = Arrays.asList("");

            JsonObject object = new JsonObject();
            object.addProperty("currentCampaign", currentCampaign);
            object.addProperty("currentCampaignIndex", currentCampaignIndex);

            Path file = Paths.get("cursor.txt");
            Files.write(file, Arrays.asList(object.toString()), Charset.forName("UTF-8"));
        } catch (Exception e) {
            System.out.println(e + " Error while saving cursor information");
        }
    }

    private void readCursorInformation() {
        try {
            Path file = Paths.get("cursor.txt");

            if (Files.exists(file)) {
                String jsonStr = Files.readAllLines(file).get(0);
                JsonObject jsonObject = new Gson().fromJson(jsonStr, JsonObject.class);
                currentCampaign = jsonObject.get("currentCampaign").getAsInt();
                currentCampaignIndex = jsonObject.get("currentCampaignIndex").getAsInt();
            } else {
                currentCampaign = 0;
                currentCampaignIndex = 0;
            }
        } catch (Exception e) {
            System.out.println(e + " Error while reading cursor information");
        }
    }
}
