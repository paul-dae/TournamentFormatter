import java.util.HashMap;
import java.util.Map;

public class DscString {

    private static final String ITAILC = "*";
    private static final String BOLD = "**";
    private static final String UNDERLINED = "__";
    private static final String STRIKETHROUGH = "~~";

    protected static Map<String, String> placements = new HashMap<String, String>();
    static{
        placements.put("Playing", ":play_pause: _**Playing**_");
        placements.put("First Place", ":first_place::one:**. Place**");
        placements.put("Second Place", ":second_place::two:**. Place**");
        placements.put("Third Place", ":third_place::three:**. Place**");
        placements.put("Fourth Place", ":four:**. Place**");
        placements.put("XPBoost", ":regional_indicator_x::regional_indicator_p:  **Boost**");
        placements.put("Canceled", ":x:");
        placements.put("Delayed", ":construction: _**Delayed**_");
    }

    protected static Map<String, String> reverseplacements = new HashMap<String, String>();
    static{
        reverseplacements.put(":play_pause: _**Playing**_", "Playing");
        reverseplacements.put(":first_place::one:**. Place**", "First Place");
        reverseplacements.put(":second_place::two:**. Place**", "Second Place");
        reverseplacements.put(":third_place::three:**. Place**", "Third Place");
        reverseplacements.put(":four:**. Place**", "Fourth Place");
        reverseplacements.put(":regional_indicator_x::regional_indicator_p:  **Boost**", "XPBoost");
        reverseplacements.put(":x:", "Canceled");
        reverseplacements.put(":construction: _**Delayed**_", "Delayed");
    }

    protected static String[] placementsKeys = {"Playing" , "First Place", "Second Place", "Third Place", "Fourth Place", "XPBoost", "Canceled", "Delayed"};

    public static String wrap(String s, String wrapper){
        return wrapper + s + wrapper;
    }

    public static String toItalic(String s){
        return wrap(s, ITAILC);
    }

    public static String toBold(String s){
        return wrap(s, BOLD);
    }

    public static String toUnderlined(String s){
        return wrap(s, UNDERLINED);
    }

    public static String toStrikethrough(String s){
        return wrap(s, STRIKETHROUGH);
    }

    public static String unwrap(String s, String wrapper){return s.substring(wrapper.length(), s.length() - wrapper.length());}
}