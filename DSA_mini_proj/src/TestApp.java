import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

class MessageNode {
    String username;
    String message;
    String date;
    String time;
    MessageNode next;

    public MessageNode(String username, String message, String date, String time) {
        this.username = username;
        this.message = message;
        this.date = date;
        this.time = time;
        this.next = null;
    }
}

public class TestApp {

    private static final String MESSAGES_FILE = "messages.txt";
    private static String currentUser;
    private static MessageNode head = null;

    public static void showMessagingPlatform(String username) {
        currentUser = username;

        JFrame frame = new JFrame("Messaging Platform");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JTextArea messageArea = new JTextArea();
        messageArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(messageArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        JTextField messageField = new JTextField();
        JButton sendButton = new JButton("Send");

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText().trim();
                if (!message.isEmpty()) {
                    saveMessage(currentUser, message);
                    messageField.setText("");
                    loadRecentMessages(messageArea);
                }
            }
        });

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        frame.add(inputPanel, BorderLayout.SOUTH);

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());

        JTextField searchField = new JTextField("Enter date (dd-MM-yyyy)");
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = searchField.getText().trim();
                if (!date.isEmpty()) {
                    loadMessagesByDate(messageArea, date);
                }
            }
        });

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        frame.add(searchPanel, BorderLayout.NORTH);

        // Load recent messages initially
        loadRecentMessages(messageArea);

        frame.setVisible(true);
    }

    private static void saveMessage(String username, String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MESSAGES_FILE, true))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            Date now = new Date();
            String date = dateFormat.format(now);
            String time = timeFormat.format(now);
            writer.write(username + " : " + message + " -" + date + "-" + time);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadRecentMessages(JTextArea messageArea) {
        loadMessagesIntoLinkedList();

        StringBuilder recentMessages = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -3);
        Date threeDaysAgo = calendar.getTime();

        MessageNode current = head;
        while (current != null) {
            try {
                Date messageDate = dateFormat.parse(current.date);
                if (!messageDate.before(threeDaysAgo)) {
                    recentMessages.append(current.username)
                            .append(" : ")
                            .append(current.message)
                            .append(" (" + current.date + " " + current.time + ")\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            current = current.next;
        }

        messageArea.setText(recentMessages.toString());
    }

    private static void loadMessagesIntoLinkedList() {
        head = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(MESSAGES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" : | -|-");
                if (parts.length == 4) {
                    String username = parts[0].trim();
                    String message = parts[1].trim();
                    String date = parts[2].trim();
                    String time = parts[3].trim();

                    MessageNode newNode = new MessageNode(username, message, date, time);
                    if (head == null) {
                        head = newNode;
                    } else {
                        MessageNode current = head;
                        while (current.next != null) {
                            current = current.next;
                        }
                        current.next = newNode;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadMessagesByDate(JTextArea messageArea, String date) {
        loadMessagesIntoLinkedList();

        StringBuilder messagesByDate = new StringBuilder();
        MessageNode current = head;
        while (current != null) {
            if (current.date.equals(date)) {
                messagesByDate.append(current.username)
                        .append(" : ")
                        .append(current.message)
                        .append(" (" + current.date + " " + current.time + ")\n");
            }
            current = current.next;
        }

        messageArea.setText(messagesByDate.toString());
    }
}
