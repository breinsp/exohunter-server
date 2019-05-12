package background;


import controller.DistributionController;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

public class EpicLoader implements Runnable {

    DistributionController root;
    int startIndex = 0;
    int campaign = 0;
    int length = 0;

    public EpicLoader(DistributionController dc, int startIndex, int campaign, int length) {
        root = dc;
        this.campaign = campaign;
        this.startIndex = startIndex;
        this.length = length;
    }

    //Loads a given amount of ids into the cache in a background thread
    @Override
    public void run() {
        List<String> total = new LinkedList<>();

        List<String> resultSet1 = getEpicIds(getCampaignLink(campaign), startIndex, length);
        total.addAll(resultSet1);

        if (resultSet1.size() < length) { //Not enough data in current campaign => load next campaign
            //get the next campaign and fill up with remaining ids
            int newCampaign = incrementCampaign();
            //Only load the remaining data to reach cache size
            List<String> resultSet2 = getEpicIds(getCampaignLink(newCampaign), 0, length - total.size());
            total.addAll(resultSet2);
            root.setCurrentCampaign(newCampaign);
            root.setCurrentCampaignIndex(resultSet2.size());
        } else {
            root.setCurrentCampaignIndex(root.getCurrentCampaignIndex() + length);
        }

        System.out.println("Loaded " + total.size() + " Entries into cache");
        //add 100 entries to cache
        root.getCache().addAll(total);
    }

    //Crafts the campaign link from a given campaign number
    public String getCampaignLink(int c) {
        return "https://www.cfa.harvard.edu/~avanderb/allk2c" + c + "obs.html";
    }

    //Returns the subsequent entry for a given campaign number, if last entry is reached, the campaigns get cycled through
    public int incrementCampaign() {
        int[] campaigns = root.getCampaigns();
        for (int i = 0; i < campaigns.length; i++) {
            int c = campaigns[i];

            if (c == campaign) {
                if (i == campaigns.length - 1) {
                    return campaigns[0];
                }

                return campaigns[i + 1];
            }
        }
        return 0;
    }

    //Loads Ids from a campaign from a given start point and length
    public List<String> getEpicIds(String path, int start, int len) {
        try {
            List<String> out = new LinkedList<>();

            //Load all ids from a campaign-page into a list via Jsoup
            Elements elements = Jsoup.connect(path).get().getElementsByTag("a");

            int end = start + len;

            //Filter out the needed elements
            for (int i = start; i < elements.size() && i < end; i++) {
                Element a = elements.get(i);
                out.add(a.html().replaceAll("\\D+", ""));
            }
            return out;
        } catch (Exception e) {
            System.out.println(e);
            return new LinkedList<>();
        }
    }
}
