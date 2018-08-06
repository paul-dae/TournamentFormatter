import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class Session {
    private final String FRAME = "=========================================================================";
    private final String TEAMNAME = "__**Teamname:**__\n";
    private String notes = "-";

    private Team team;
    protected List<Tournament> tournaments = new ArrayList<>();

    public Session(){

    }

    public void addTournamentFromURL(String url, String eventID) throws MalformedURLException {
        this.tournaments.add(new Tournament(url, eventID));
    }

    public void addTournament(Tournament t){
        String name = t.getName();
        int i = 2;
        for(Tournament tm : tournaments){
            System.out.println(name);
            System.out.println(tm.getName());
            if(tm.getName().contains(name)){
                System.out.println("found");
                name = t.getName() + " " + Integer.toString(i);
                i++;
            }
        }

        t.setName(name);
        this.tournaments.add(t);
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void addNotes(String notes){
        this.notes = notes;
    }

    public Team getTeam(){
        return team;
    }

    public String toString(){
        String out = FRAME + "\n";
        for(Tournament t : tournaments){
            out += t.toString() + "\n\n";
        }
        out += TEAMNAME + team.toString() + "\n" + FRAME + "\n" +  DscString.toBold("NOTES: " + notes);
        return out;
    }
}