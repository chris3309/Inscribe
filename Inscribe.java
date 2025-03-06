import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Inscribe extends JFrame {
    private List<Flashcard> flashcards;
    private int currentIndex = 0;
    private JLabel cardLabel;
    private JPanel buttonPanel;
    // Buttons for flipping the card, moving to the next card, and rating the difficulty.
    private JButton flipButton;
    private JButton againButton;
    private JButton hardButton;
    private JButton goodButton;
    private JButton easyButton;
    // "revealed" indicates whether the answer should be shown below the question.
    private boolean revealed = false;

    public Inscribe() {
        setTitle("Inscribe");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeFlashcards();
        initializeUI();
    }

    private void initializeFlashcards() {
        flashcards = new ArrayList<>();
        flashcards.add(new Flashcard("What is the capital of France?", "Paris"));
        flashcards.add(new Flashcard("What is the largest planet in our solar system?", "Jupiter"));
        flashcards.add(new Flashcard("What is the smallest prime number?", "2"));
    }

    private void initializeUI() {
        // Create a panel to represent the flashcard with a border
        JPanel cardPanel = new JPanel();
        cardPanel.setBorder(new LineBorder(Color.BLACK, 2, true));
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBackground(Color.WHITE);

        // Create a label for the card content
        cardLabel = new JLabel("", SwingConstants.CENTER);
        cardLabel.setFont(new Font("Arial", Font.BOLD, 20));
        updateCardLabel();
        cardPanel.add(cardLabel, BorderLayout.CENTER);

        buttonPanel = new JPanel();

        // Create buttons for interaction
        flipButton = new JButton("Flip");
        flipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                revealAnswer();
            }
        });
        buttonPanel.add(flipButton);
        
        // Create buttons for rating the difficulty
        againButton = new JButton("Again");
        hardButton = new JButton("Hard");
        goodButton = new JButton("Good");
        easyButton = new JButton("Easy");

        ActionListener ratingListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle the rating logic here
                nextCard();
            }
        };

        againButton.addActionListener(ratingListener);
        hardButton.addActionListener(ratingListener);
        goodButton.addActionListener(ratingListener);
        easyButton.addActionListener(ratingListener);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(cardPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateCardLabel() {
        Flashcard card = flashcards.get(currentIndex);
        if (revealed) {
            // Both question and answer are shown.
            cardLabel.setText("<html><div style='text-align: center; padding: 20px;'>"
                              + card.getQuestion() + "<br><br>" + card.getAnswer() 
                              + "</div></html>");
        } else {
            // Only the question is shown.
            cardLabel.setText("<html><div style='text-align: center; padding: 20px;'>"
                              + card.getQuestion() + "</div></html>");
        }
    }

    // Reveals the answer below the question.
    private void revealAnswer() {
        if (!revealed) {
            revealed = true;    
            updateCardLabel();
            buttonPanel.removeAll();
            buttonPanel.add(againButton);
            buttonPanel.add(hardButton);
            buttonPanel.add(goodButton);
            buttonPanel.add(easyButton);
            buttonPanel.revalidate();
            buttonPanel.repaint();
        }
    }

    // Moves to the next flashcard and resets the view.
    private void nextCard() {
        currentIndex = (currentIndex + 1) % flashcards.size();
        revealed = false;
        updateCardLabel();
        buttonPanel.removeAll();
        buttonPanel.add(flipButton);
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Inscribe().setVisible(true);
            }
        });
    }
}
