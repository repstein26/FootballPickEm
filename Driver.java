import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JButton;

class Driver {
	public static final String frameTitle = "NFL Pick 'Em";
	public static final String borderTitle = "ENTER WIN PROBABILITY FOR EACH TEAM";
	final static int MM = 80;
	final static String TEAM_FILE = "matchups.txt";

	private static Random rng = new Random();

	private static List<Game> gameList = new ArrayList<>();
	private static Set<String> output = new HashSet<>();

	public static void main(String[] args) {
		  
		readTeamFile();
		final MatchupGUI gui = new MatchupGUI(frameTitle,borderTitle, gameList.size());

		ActionListener saveToFile = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						System.out.println("Saving to file");
					     writeToFile(gui);
					     gameList.clear();
					     readTeamFile();
					     for (int i = 0; i < MM; i++) {
							compute();
						}

						// write info to file
						//Get the file reference
						Path path = Paths.get("pickTix.txt");
						try (BufferedWriter writer = Files.newBufferedWriter(path)){
							int counter = 1;
							for (String o : output) {
								writer.write(counter + ": " + o);
								writer.newLine();
								counter++;
							}
							output.clear();
							File file = new File("pickTix.txt");
							Desktop.getDesktop().open(file);
						
						} catch (IOException ioe){
							System.out.println(ioe);
						}
					     //System.exit(0);
					}
				});
			}
		};

		ActionListener clearAllFields = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println("Clearing all text fields");
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						gui.clearAllTextFields();
					}
				});
			}
		};

		gui.showGUI();
		gui.addSaveButtonListener(saveToFile);
		gui.addClearAllButtonListener(clearAllFields);

		addTextToFields(gui);

	}

	private static void addTextToFields(MatchupGUI gui){
		for (int i = 0; i < gameList.size(); i++){
			gui.setAwayLabel(i, gameList.get(i).getAway().getName());
			gui.setHomeLabel(i, gameList.get(i).getHome().getName());
			gui.setHomeText(i, gameList.get(i).getHome().getPercentage());
			gui.setAwayText(i, gameList.get(i).getAway().getPercentage());
		}
	
	}

	private static void writeToFile(MatchupGUI gui){
		String textForFile = gui.toString();
		System.out.println(textForFile);
		//Get the file reference
		Path path = Paths.get(TEAM_FILE);
		try (BufferedWriter writer = Files.newBufferedWriter(path))
		{
			writer.write(textForFile);
		} catch (IOException ioe){
			System.out.println(ioe);
		}
	}

	private static void readTeamFile() {
		// read teams.txt file

		try{
			BufferedReader reader = new BufferedReader( new FileReader(TEAM_FILE));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] teams = line.split("\\|");
				Team home = null;
				Team away = null;
				String[] awayInfo = teams[0].split(" ");
				String[] homeInfo = teams[1].split(" ");
				away = new Team(awayInfo[0], awayInfo[1]);
				home = new Team(homeInfo[0], homeInfo[1]);
				gameList.add(new Game(away, home));
			}
		} catch (IOException ioe){
			System.out.println(ioe);
		}
	}

	private static void compute() {

		StringBuilder builder = new StringBuilder();
		for (Game currentGame : gameList) {
			int rand = rng.nextInt(100)+1; // 1 - 100
			Team home = currentGame.getHome();
			Team away = currentGame.getAway();

			builder.append(rand <= Integer.valueOf(home.getPercentage()) ? home.getName() : away.getName()).append(",");
		}

		output.add(builder.toString().substring(0, builder.length()-1));
	}
}