import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BattlefyURL {
    private final String BATTLEFY_HOST = "battlefy.com";

    private URL url;
    private List<String> splitPath = new ArrayList<String>();
    private String tName;
    private boolean bracketURL = false;
    private boolean overviewURL = false;

    BattlefyURL(String sURL) throws MalformedURLException {
        this(new URL(sURL));
    }

    BattlefyURL(URL uURL){
        this.url = uURL;
        if(this.isBattlefyURL()){
            String[] s = uURL.getPath().split("/");
            for(int i = 0; i < s.length ; i++){
                if(!(s[i] == null) && !s[i].isEmpty()) {
                    this.splitPath.add(s[i]);
                }
            }
            this.tName = splitPath.get(0);
            this.tName = this.tName.substring(0, 1).toUpperCase() + this.tName.substring(1);

            if(splitPath.get(splitPath.size() - 1).contains("info")) this.overviewURL = true;
            else if(splitPath.get(splitPath.size() - 1).equals("bracket") || splitPath.get(splitPath.size() - 2).equals("bracket")) this.bracketURL = true;
            else System.err.println("URL can't be processed: " + uURL.toString());
        }
    }

    public URL getHideComplete(){
        try {
            return new URL(getUrl().toString() + "/?hidecomplete=true");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BattlefyURL getOverviewURL(){
        try {
            String overviewPath = url.getHost() + "/" +  splitPath.get(0) + "/" + "/" +  splitPath.get(1) + "/" + "/" +  splitPath.get(2) + "/info?infoTab=details";
            if(isBracketURL()) return new BattlefyURL(new URL(overviewPath));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isBattlefyURL(){
        return url.getHost().equals(BATTLEFY_HOST);
    }

    public String getTName(){
        return tName;
    }

    public boolean isBracketURL() {
        return bracketURL;
    }

    public boolean isOverviewURL() {
        return overviewURL;
    }

    public URL getUrl(){
        return url;
    }

    public String getHost() {
        return url.getHost();
    }

    public String getPath(){
        return url.getPath();
    }

    public List<String> getSplitPath(){
        return splitPath;
    }

    @Override
    public String toString(){
        return url.toString();
    }
}