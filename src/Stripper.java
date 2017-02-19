import java.util.ArrayList;

public class Stripper {

    private ArrayList<String> results = new ArrayList<>();

    public Stripper(ArrayList<String> page) {
        for (String html : page)
            matchHTML(html);
    }

    public Stripper(ArrayList<String> page, boolean httpOnly, String filter) {
        this(page);

        // Filter through the ArrayList
        if (httpOnly) {
            ArrayList<String> newLinks = new ArrayList<>();
            for (String link : results)
                if (link.contains("http") || link.contains("https")) newLinks.add(link);

            results = newLinks;
        }

        if (filter != null && !filter.isEmpty() && !filter.equals("KEYWORD")) {
            ArrayList<String> newLinks = new ArrayList<>();
            for (String link : results)
                if (link.contains(filter)) newLinks.add(link);

            results = newLinks;
        }
    }

    public ArrayList<String> getResults() { return results; }

    private void matchHTML(String html) {
        // Go through string, we match "href="
        for (int i = 0; i + 5 < html.length(); i++) {
            if (html.substring(i, i + 5).equals("href=")) {
                // Get start of link and set end to start
                int start = i + 6;
                if (html.charAt(start) == '\'' || html.charAt(start) == '\"')
                    start = i + 7;
                int end = start;

                // Find where the string ends and add to ArrayList
                while (end < html.length() && html.charAt(end) != '\'' && html.charAt(end) != '\"')
                    end++;
                results.add(html.substring(start, end));

                // Match rest of the string for more links
                matchHTML(html.substring(end + 1, html.length()));
                break;
            }
        }
    }
}
