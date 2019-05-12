package controller;

import entities.LCData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class K2DataController {

    //Returns meta data for a given epic number
    public static void fetchK2Data(int epicNr) throws IOException {
        String select = "select=" +
                "epic_number," +
                "k2_mass," +
                "k2_rad," +
                "k2_campaign";


        String url = "https://exoplanetarchive.ipac.caltech.edu/cgi-bin/nstedAPI/nph-nstedAPI?table=k2targets&format=csv&" + select + "&where=epic_number=" + epicNr;
        Document document = Jsoup.connect(url).get();
        Element body = document.getElementsByTag("body").first();
        String output = body.html().split("\n")[0].trim().split(" ")[1];

        String[] result = output.split(",");
        double mass = Double.parseDouble(result[1]);
        double radius = Double.parseDouble(result[2]);
        int campaign = getCampaignForEpicNr(epicNr);
    }

    /*
    public static LCData getLightCurveForStar(int epicNr, int campaign) throws IOException {
        String href = getLightCurveURL(epicNr, campaign);

        URL txt = new URL(href);
        Scanner scanner = new Scanner(txt.openStream());

        List<Double> xAxis = new LinkedList<>();
        List<Double> yAxis = new LinkedList<>();

        double xmin = Double.MAX_VALUE;
        double xmax = 0.0;
        double ymin = Double.MAX_VALUE;
        double ymax = 0.0;

        boolean headline = true;
        while (scanner.hasNext()) {
            try {
                String line = scanner.nextLine();

                if (headline) {
                    headline = false;
                } else {
                    String[] split = line.split(",");
                    double x = Double.parseDouble(split[0]);
                    double y = Double.parseDouble(split[1]);
                    xAxis.add(x);
                    yAxis.add(y);

                    xmax = (x > xmax) ? x : xmax;
                    xmin = (x < xmin) ? x : xmin;
                    ymax = (y > ymax) ? y : ymax;
                    ymin = (y < ymin) ? y : ymin;
                }
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }
        return new LCData(epicNr, xmin, xmax, ymin, ymax, xAxis, yAxis);
    }*/

    //Returns the data url for the light curve data from epic number and campaign number
    public static String getLightCurveURL(int epicNr, int campaign) throws IOException {
        String url = "https://www.cfa.harvard.edu/~avanderb/k2c" + campaign + "/ep" + epicNr + ".html";
        Element a = Jsoup.connect(url).get().getElementsByTag("a").first();
        String href = a.attr("href");

        return href;
    }

    //Returns the campaign number for a given epic number
    public static int getCampaignForEpicNr(int epicNr) throws IOException {
        String url = "https://archive.stsci.edu/k2/data_search/search.php?ktc_k2_id=" + epicNr + "&outputformat=CSV&action=Search&selectedColumnsCsv=sci_campaign";
        Document document = Jsoup.connect(url).get();
        Element element = document.getElementsByTag("body").first();
        String[] split = element.html().split(" ");

        int out = 0;

        if (split.length == 4) {
            out = Integer.parseInt(split[3]);
        } else {
            out = Integer.parseInt(split[2]);
        }
        for (int j = 0; j < DistributionController.CAMPAIGN_BLACKLIST.length; j++) {
            int bl = DistributionController.CAMPAIGN_BLACKLIST[j];

            if (out == bl) {
                out++;
                break;
            }
        }
        return out;
    }

    //Returns all campaign numbers except the black listed ones
    public static int[] getAllCampaigns() {
        try {
            List<Integer> ints = new LinkedList<>();
            Document doc = Jsoup.connect("https://www.cfa.harvard.edu/~avanderb/k2.html").get();
            Elements elements = doc.getElementsByTag("a");
            for (int i = 0; i < elements.size(); i++) {
                Element a = elements.get(i);
                String href = a.attr("href");
                if (href.contains("asciiwget.sh")) {
                    href = href.substring(1).replace("asciiwget.sh", "");

                    int campaign = Integer.parseInt(href);
                    boolean valid = true;

                    for (int j = 0; j < DistributionController.CAMPAIGN_BLACKLIST.length; j++) {
                        int bl = DistributionController.CAMPAIGN_BLACKLIST[j];

                        if (campaign == bl) {
                            valid = false;
                            break;
                        }
                    }
                    if (valid) {
                        ints.add(campaign);
                    }
                }
            }
            int[] out = new int[ints.size()];
            for (int i = 0; i < ints.size(); i++) {
                out[i] = ints.get(i).intValue();
            }
            return out;
        } catch (Exception e) {
            System.out.println(e);
        }
        return new int[]{};
    }

    //Returns the epic nr for a k2 alias name
    public static String getEpicFromAlias(String alias) {
        try {
            URL url = new URL("https://exoplanetarchive.ipac.caltech.edu/cgi-bin/nstedAPI/nph-nstedAPI?table=k2names&format=csv&select=epic_host&where=k2_name=" + alias);

            System.out.println(url.toString());

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();

            while ((line = in.readLine()) != null) {
                response.append(line + "\n");
            }
            in.close();

            return response.toString().split("\n")[1].replaceAll("\\D+", "");
        } catch (Exception e) {
            System.out.println(e.toString());
            return e.toString();
        }
    }
}
