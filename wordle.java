package wordlecsa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;

public class wordle extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String[] WORDS = {"apple", "banana", "orange", "grape", "pear", "kiwi", "strawberry", "pineapple"};
    private static final int MAX_ATTEMPTS = 5;

    private String targetWord;
    private char[] guess;
    private boolean[] guessedLetters;
    private int attempts;

    private JLabel wordLabel;
    private JTextField guessField;
    private JButton guessButton;
    private JLabel attemptsLabel;
    private JLabel progressLabel;

    public wordle() {
        setTitle("Wordle");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        wordLabel = new JLabel();
        add(wordLabel);

        progressLabel = new JLabel();
        add(progressLabel);

        guessField = new JTextField();
        add(guessField);

        guessButton = new JButton("Guess");
        guessButton.addActionListener(new GuessButtonListener());
        add(guessButton);

        attemptsLabel = new JLabel();
        add(attemptsLabel);

        startGame();
    }

    private void startGame() {
        Random random = new Random();
        targetWord = WORDS[random.nextInt(WORDS.length)];
        guess = new char[targetWord.length()];
        guessedLetters = new boolean[26];
        attempts = 0;

        Arrays.fill(guess, '_');

        wordLabel.setText("Guess the word: " + String.valueOf(guess));
        progressLabel.setText("");
        attemptsLabel.setText("Attempts left: " + (MAX_ATTEMPTS - attempts));
    }

    private void updateProgress() {
        StringBuilder progress = new StringBuilder();
        for (char c : guess) {
            progress.append(c).append(" ");
        }
        progressLabel.setText("Progress: " + progress);
    }

    private void checkGuess(String input) {
        char letter = input.toLowerCase().charAt(0);

        if (guessedLetters[letter - 'a']) {
            JOptionPane.showMessageDialog(this, "You already guessed this letter. Try again.");
            return;
        }

        guessedLetters[letter - 'a'] = true;

        boolean found = false;
        for (int i = 0; i < targetWord.length(); i++) {
            if (targetWord.charAt(i) == letter) {
                guess[i] = letter;
                found = true;
            }
        }

        if (!found) {
            attempts++;
            attemptsLabel.setText("Attempts left: " + (MAX_ATTEMPTS - attempts));
        }

        updateProgress();

        if (Arrays.equals(guess, targetWord.toCharArray())) {
            JOptionPane.showMessageDialog(this, "Congratulations! You guessed the word: " + targetWord);
            startGame();
        } else if (attempts >= MAX_ATTEMPTS) {
            JOptionPane.showMessageDialog(this, "Sorry, you're out of attempts. The word was: " + targetWord);
            startGame();
        }
    }

    private class GuessButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String guess = guessField.getText();
            if (guess.length() != 1 || !Character.isLetter(guess.charAt(0))) {
                JOptionPane.showMessageDialog(wordle.this, "Please enter a single letter.");
                return;
            }
            checkGuess(guess);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            wordle game = new wordle();
            game.setVisible(true);
        });
    }
}

