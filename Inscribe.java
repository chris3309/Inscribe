import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
//import java.time.LocalDateTime;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Inscribe extends JFrame {
    private List<Flashcard> flashcards;
    private int currentIndex = 0;
    private JLabel cardLabel;
    private JPanel buttonPanel;
    private JPanel topPanel;
    // Buttons for flipping the card, moving to the next card, and rating the difficulty.
    private JButton flipButton;
    private JButton againButton;
    private JButton hardButton;
    private JButton goodButton;
    private JButton easyButton;
    // "revealed" indicates whether the answer should be shown below the question.
    private boolean revealed = false;
    // File in which flashcards are stored
    private static final String FILE_NAME = "flashcards.txt";

    public Inscribe() {
        setTitle("Inscribe");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeFlashcards();
        initializeUI();
    }

    private void initializeFlashcards() {
        flashcards = new ArrayList<>();
        //open file and read flashcards
        try{
            File fcardsVerses = new File(FILE_NAME);
            if(fcardsVerses.exists()){
                Scanner reader = new Scanner(fcardsVerses);
                while(reader.hasNextLine()){
                    String verse = reader.nextLine();
                    flashcards.add(new Flashcard(verse));
                }
                reader.close();
            }
            else{
                System.out.println("Flashcards file not found");
                System.out.println("Creating new file...");
                try{
                    File newFile = new File(FILE_NAME);
                    if(newFile.createNewFile()){
                        System.out.println("File created: " + newFile.getName());
                    }
                    else{
                        System.out.println("File already exists.");
                    }
                } catch(Exception e){
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }
            
        }
        catch(Exception e){
            System.out.println("Error reading flashcards from file: " + e.getMessage());
            e.printStackTrace();
        }



        flashcards.add(new Flashcard("John 3:16"));
        flashcards.add(new Flashcard("Revelation 1:17-18"));
        flashcards.add(new Flashcard("Philippians 4:13"));
        //System.out.println(flashcards.get(0).getVerse("John 3:16"));
    }

    private void initializeUI() {

        

        // Create a panel to represent the flashcard with a border
        JPanel cardPanel = new JPanel();
        cardPanel.setBorder(new LineBorder(Color.BLACK, 1, true));
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
        topPanel = new JPanel();
        topPanel.setBackground(Color.LIGHT_GRAY);
        topPanel.setBorder(new LineBorder(Color.BLACK, 1, true));
        //topPanel.setLayout(new BorderLayout());
        JButton addButton = new JButton("Add Flashcard");
        
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle adding a new flashcard here
                addNewCard();
            }
        });
        topPanel.add(addButton);
        getContentPane().add(topPanel, BorderLayout.NORTH);

    }

    private void updateCardLabel() {
        Flashcard card = flashcards.get(currentIndex);
        if (revealed) {
            // Both question and answer are shown.
            cardLabel.setText("<html><div style='text-align: center; padding: 20px;'>"
                              + card.getVerse() + "<br><br>" + card.getReference() 
                              + "</div></html>");
        } else {
            // Only the question is shown.
            cardLabel.setText("<html><div style='text-align: center; padding: 20px;'>"
                              + card.getVerse() + "</div></html>");
        }
    }
    
    private void addNewCard(){
        String newReference = JOptionPane.showInputDialog(
            this,
            "Enter a Bible Verse",
            "Add Flashcard",
            JOptionPane.PLAIN_MESSAGE
        );
        if(newReference != null && !newReference.trim().isEmpty()){
            flashcards.add(new Flashcard(newReference.trim()));
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))){
                writer.write(newReference.trim());
                writer.newLine();
            } catch(Exception e){
                System.out.println("Error writing to file: " + e.getMessage());
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(
                this,
                "Flashcard added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Inscribe().setVisible(true);
            }
        });
    }
}
