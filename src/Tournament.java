import java.net.MalformedURLException;

public class Tournament {
    private final String EVENT_URL = "https://events.euw.leagueoflegends.com/events/";
    private String bracket = DscString.toItalic("Bracket:") + " ";
    private String overview = DscString.toItalic("Overview:") + " ";
    private String event = DscString.toItalic("Eventpage:") + " ";

    private String tName;
    private String name;
    private BattlefyURL overviewURL;
    private BattlefyURL bracketURL;
    private String eventURL;
    private String status;
    private String eventID;
    private String team;

    public Tournament(String sURL, String eventID, String status) throws MalformedURLException{
        this.bracketURL = new BattlefyURL(sURL);
        this.tName = this.bracketURL.getTName();
        this.name = this.bracketURL.getSplitPath().get(1);
        if(eventID.length() == 6){
            this.eventID = eventID;
            this.eventURL = EVENT_URL + eventID;
        }
        else{
            this.eventURL = eventID;
            this.eventID = eventID.substring(eventID.length() - 6);
        }
        this.status = status;
    }

    public Tournament(String sUrl, String eventID) throws MalformedURLException {
        this(sUrl, eventID, "Playing");
    }

    public void setStatus(String status) {
        if(DscString.placements.containsKey(status)) this.status = status;
    }

    public void setName(String name){
        this.tName = name;
    }

    public String getName(){
        return tName;
    }

    public String getEventID(){
        return eventID;
    }

    public String getStatus(){return status;}

    public String getBracketURL() {
        return bracketURL.toString();
    }

    public String toString(){
        return DscString.toUnderlined(DscString.toBold(this.tName)) + ": " +  DscString.placements.get(status) + "\n" + ((bracketURL.isBracketURL()) ?  bracket : overview )
                + "<" + bracketURL.toString() + ">" + "\n" + event + "<" + eventURL + ">";
    }
}