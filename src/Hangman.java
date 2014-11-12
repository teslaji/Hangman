import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Hangman {

	// This describes the word length intervals
	private static final int[] difficulties = { 2, 4, 7, 15 };

	// Some global variables
	private static String word;
	private static String wordCopy;
	private static List<String> wordList;
	private static char[] solution;
	private static boolean solved;
	private static Scanner scan;
	private static int wrongGuesses;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);

		// The layout manager handle the layout
		// of the widgets in the container
		shell.setLayout(new FillLayout());

		// Shell can be used as container
		Label label = new Label(shell, SWT.BORDER);
		label.setText("This is a label:");
		label.setToolTipText("This is the tooltip of this label");

		Text text = new Text(shell, SWT.NONE);
		text.setText("This is the text in the text widget");
		text.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		text.setForeground(display.getSystemColor(SWT.COLOR_WHITE));

		Button button = new Button(shell, SWT.PUSH);

		// register listener for the selection event
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Called!");
			}
		});

		// set widgets size to their preferred size
		text.pack();
		label.pack();

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void init() {
		System.out.println("Welcome to Hangman!");
		scan = new Scanner(System.in);

		word = "";
		while (word.isEmpty()) {
			System.out.print("Enter a difficulty (1-3): ");
			int difficulty = scan.nextInt();

			if (difficulty > 0 && difficulty < 4) {
				playGame(difficulty);
			} else {
				System.out.println("That's not an option");
			}
		}
		scan.close();
	}

	private static void playGame(int difficulty) {
		word = getWord(difficulty).toLowerCase();
		wordCopy = word;
		solution = new char[word.length()];
		solved = false;
		wrongGuesses = 0;

		printMan(wrongGuesses);
		for (int i = 0; i < solution.length; i++) {
			solution[i] = '_';
			System.out.print("_ ");
		}

		while (!solved && wrongGuesses < 7) {
			System.out.println("\n\nEnter a letter or guess the word: ");
			String guess = scan.next();

			if (guess.length() > 1) {
				if (guess.equalsIgnoreCase(word)) {
					solved = true;
				} else {
					wrongGuesses++;
					checkSolution();
				}
			} else {
				char choice = guess.charAt(0);
				int occurrences = wordCopy.length()
						- wordCopy.replace(String.valueOf(choice), "").length();

				if (occurrences == 0) {
					wrongGuesses++;
				}
				for (int i = 0; i < occurrences; i++) {
					int index = wordCopy.indexOf(choice);
					solution[index] = choice;

					wordCopy = wordCopy.replaceFirst(String.valueOf(choice),
							"_");
				}

				checkSolution();
			}

			if (solved) {
				System.out.println("\n\nYou win!! Play again? (y/n)");
				if (scan.next().toLowerCase().charAt(0) == 'y') {
					playGame(difficulty);
				}
			} else if (wrongGuesses == 7) {
				System.out.println("\n\nYou lose. Play again? (y/n)");
				if (scan.next().toLowerCase().charAt(0) == 'y') {
					playGame(difficulty);
				}
			}
		}
	}

	private static void checkSolution() {
		printMan(wrongGuesses);

		if (wrongGuesses == 7) {
			for (int i = 0; i < word.length(); i++) {
				System.out.print(word.charAt(i) + " ");
			}
		} else {
			boolean containsUnderscore = false;
			solved = true;

			for (int i = 0; i < solution.length; i++) {
				if (!containsUnderscore && solution[i] == '_') {
					containsUnderscore = false;
					solved = false;
				}
				System.out.print(solution[i] + " ");
			}
		}
	}

	private static String getWord(int difficulty) {
		String word = "";
		int wordIndex = -1;
		readInWordList();

		while (true) {
			wordIndex = (int) Math.ceil(Math.random() * 1000) % wordList.size();
			word = wordList.get(wordIndex);
			if (word.length() > difficulties[difficulty - 1]
					&& word.length() <= difficulties[difficulty]) {
				return word;
			}
		}
	}

	private static void readInWordList() {
		String fileName = "wordList.txt";

		try {
			wordList = Files.readAllLines(Paths.get(fileName),
					Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void printMan(int pieces) {
		switch (pieces) {
		case 0:
			System.out.println(" ______");
			System.out.println(" |    |");
			System.out.println(" |");
			System.out.println(" |");
			System.out.println(" |");
			System.out.println(" |");
			System.out.println(" |");
			System.out.print(" |________    ");
			break;
		case 1:
			System.out.println(" ______");
			System.out.println(" |    |");
			System.out.println(" |    O");
			System.out.println(" |");
			System.out.println(" |");
			System.out.println(" |");
			System.out.println(" |");
			System.out.print(" |________    ");
			break;
		case 2:
			System.out.println(" ______");
			System.out.println(" |    |");
			System.out.println(" |    O");
			System.out.println(" |    |");
			System.out.println(" |");
			System.out.println(" |");
			System.out.println(" |");
			System.out.print(" |________    ");
			break;
		case 3:
			System.out.println(" ______");
			System.out.println(" |    |");
			System.out.println(" |    O");
			System.out.println(" |   /|");
			System.out.println(" |");
			System.out.println(" |");
			System.out.println(" |");
			System.out.print(" |________    ");
			break;
		case 4:
			System.out.println(" ______");
			System.out.println(" |    |");
			System.out.println(" |    O");
			System.out.println(" |   /|\\");
			System.out.println(" |");
			System.out.println(" |");
			System.out.println(" |");
			System.out.print(" |________    ");
			break;
		case 5:
			System.out.println(" ______");
			System.out.println(" |    |");
			System.out.println(" |    O");
			System.out.println(" |   /|\\");
			System.out.println(" |    |");
			System.out.println(" |");
			System.out.println(" |");
			System.out.print(" |________    ");
			break;
		case 6:
			System.out.println(" ______");
			System.out.println(" |    |");
			System.out.println(" |    O");
			System.out.println(" |   /|\\");
			System.out.println(" |    |");
			System.out.println(" |   /");
			System.out.println(" |");
			System.out.print(" |________    ");
			break;
		case 7:
			System.out.println(" ______   ________________________");
			System.out.println(" |    |  /I'm dead now. Thanks.../");
			System.out
					.println(" |    O  \u0305 \u0305 \u0305 \u0305 \u0305 \u0305 \u0305 \u0305 \u0305 \u0305 \u0305 "
							+ "\u0305 \u0305 \u0305 \u0305 \u0305 \u0305 \u0305 \u0305 \u0305 \u0305 \u0305 \u0305 \u0305 ");
			System.out.println(" |   /|\\");
			System.out.println(" |    |");
			System.out.println(" |   / \\");
			System.out.println(" |");
			System.out.print(" |________    ");
			break;
		}
	}
}