import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.TreeMap;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.regex.Matcher;
import java.util.Map;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Word Occurrence Program 
 * 
 * A program that reads the poem The Raven from a specified webpage, stores each word
 * in an array, and counts the occurrence of each word. Program will output each word and
 * it's number of occurrences
 *
 * @author Sharlton Shepeluk
 *
 */



public class main {

	

	public static void main(String[] args) {

		// Reading URL and writing to file ==================================================================================

		URL url;
		try {
			url = new URL("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm"); // Create object for URL.
			BufferedReader readr = new BufferedReader(new InputStreamReader(url.openStream())); // Open a reader to
																								// stream the file
			BufferedWriter writer = new BufferedWriter(new FileWriter("poem.txt")); // Create file to store the
																					// downloaded info.
			String line;

			int start = 77; // line right before poem starts. Seen by viewing webpage source code
			int end = 244; // line right after poem starts. Seen by viewing webpage source code

			// Writes poem to file starting and ending at the predetermined lines above
			for (int ln = 0; (line = readr.readLine()) != null && ln <= end; ln++) {
				if (ln >= start) {
					writer.write(line);
				}
			}

			readr.close();
			writer.close();
		} catch (IOException e) {
			System.out.println("Error");
			e.printStackTrace();
		}

		File f = new File("poem.txt");
		
		try {
			
			Document doc = Jsoup.parse(f, "UTF-8"); // Eliminates html tags and symbols

			String body = doc.body().text(); // Save the parsed words to a string.
			BufferedWriter writer = new BufferedWriter(new FileWriter("poem.txt")); // rewrite the file without the
																					// tags/symbols.
			writer.write(body);
			writer.close();
		} catch (IOException e) {
			System.out.println("Error");
			e.printStackTrace();
		}

		// Word Count ===========================================================================================================

		Path path = Paths.get("C:\\Users\\sharl\\Desktop\\2022HW\\SoftwareDev\\Mod9SDLCPart2\\poem.txt");

		
		
        try {
        
        //Converting file from ANSI to UTF-8	
        ByteBuffer bb = ByteBuffer.wrap(Files.readAllBytes(path));
        CharBuffer cb = Charset.forName("windows-1252").decode(bb);
        bb = Charset.forName("UTF-8").encode(cb);
        Files.write(path, bb.array());
		
        } catch (IOException e1) {
			System.out.println("Error");
			e1.printStackTrace();
		}

		
		
		// HashMap ==============================================================================
		LinkedHashMap<String, Integer> sortedFreq = new LinkedHashMap<>();

		try {

			String poem = Files.readString(path);

			poem = poem.toLowerCase();
			Pattern p = Pattern.compile("[a-z]+"); // creates pattern that ignores symbols
			Matcher m = p.matcher(poem);

			TreeMap<String, Integer> freq = new TreeMap<>();

			while (m.find()) {
				String word = m.group();

				if (freq.containsKey(word)) {
					freq.computeIfPresent(word, (w, c) -> Integer.valueOf(c.intValue() + 1));
				} else {
					freq.computeIfAbsent(word, (w) -> Integer.valueOf(1));
				}
			}

			// Sorts treemap into descending order with use of a linked hashmap

			freq.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
					.forEachOrdered(x -> sortedFreq.put(x.getKey(), x.getValue()));

			// Outputs treemap
			for (String word : sortedFreq.keySet()) {
				String key = word.toString();
				String value = sortedFreq.get(word).toString();
				System.out.println(key + ", " + value);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println();
		
		// GUI ============================================================================================================

		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		frame.setSize(350, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);

		panel.setLayout(null);
		JLabel label = new JLabel("Search for word:");
		label.setFont(new Font("Serif", Font.PLAIN, 12));
		label.setBounds(10, 20, 80, 25);
		panel.add(label);

		JTextField searchWord = new JTextField("Enter word", 20);

		searchWord.setBounds(100, 20, 165, 25);
		panel.add(searchWord);

		JButton button = new JButton("Search");
		button.setBounds(10, 80, 80, 25);
		button.addActionListener(new ActionListener() {

			// Button searches for inputed word and outputs # of occurrences
			public void actionPerformed(java.awt.event.ActionEvent e) {

				String userEntry = searchWord.getText().toLowerCase();

				System.out.println("" + userEntry);

				if (sortedFreq.containsKey(userEntry)) {
					System.out.println(
							"The word " + userEntry + " appears in poem " + sortedFreq.get(userEntry) + " times(s)");

				} else {
					System.out.println("Poem does not contain this word");
				}

			}

		});

		panel.add(button);

		frame.setVisible(true);
	}// end main

}
