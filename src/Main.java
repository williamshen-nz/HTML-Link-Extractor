import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static ArrayList<String> results = new ArrayList<>();
    private static boolean httpOnly = true;
    private static String keyword = "";

    public static void main(String[] args) {
        // We get http or https only unless specified
        if (args.length != 0 && args[0].equals("-a"))
            httpOnly = false;

        // Keyword
        if (args.length >= 2)
            keyword = args[1];

        // We read the HTML into an ArrayList line by line
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> page = new ArrayList<>();
        while(scanner.hasNext()) {
            page.add(scanner.nextLine());
        }

        // Loop through each line of HTML in the page
        for (String html : page)
            //matchHTML(html);

        // Filter through the ArrayList
        if (httpOnly) {
            ArrayList<String> newLinks = new ArrayList<>();
            for (String link : results)
                if (link.contains("http") || link.contains("https")) newLinks.add(link);

            results = newLinks;
        }

        if (!keyword.isEmpty()) {
            ArrayList<String> newLinks = new ArrayList<>();
            for (String link : results)
                if (link.contains(keyword)) newLinks.add(link);

            results = newLinks;
        }

        results.forEach(System.out::println);
    }
        /*
    private static void matchHTML(String html) {
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
    */
}
