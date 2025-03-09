import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarGUI {

    private static final String CHAT_LOG_FILE = "messages.txt";
    private static JFrame frame;
    private static String currentUser = "User";

    private static class DateNode {
        String date;
        MessageNode messages;
        DateNode nextDate;

        public DateNode(String date) {
            this.date = date;
            this.messages = null;
            this.nextDate = null;
        }
    }

    private static class MessageNode {
        String message;
        MessageNode nextMessage;

        public MessageNode(String message) {
            this.message = message;
            this.nextMessage = null;
        }
    }

    private static DateNode dateHead = null;

    public static void main(String[] args) {
        frame = new JFrame("Messaging Platform");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        initializeLinkedListsFromFile();
        createDashboard();
        frame.setVisible(true);
    }

    private static void createDashboard() {
        frame.getContentPane().removeAll();
        JButton messagingButton = new JButton("Open Messaging Platform");
        messagingButton.addActionListener(e -> showMessagingPlatform());
        frame.add(messagingButton);
        frame.revalidate();
        frame.repaint();
    }

    private static void showMessagingPlatform() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setBackground(new Color(30, 31, 34));
        chatArea.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        JPanel inputPanel = new JPanel(new BorderLayout());
        JTextField messageField = new JTextField();
        messageField.setForeground(Color.WHITE);
        messageField.setBackground(new Color(49, 50, 52));
        JButton sendButton = new JButton("Send");
        sendButton.setForeground(Color.BLACK);
        sendButton.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(70, 30));
        backButton.setForeground(Color.BLACK);
        backButton.setBackground(Color.WHITE);

        JTextField dateField = new JTextField();
        dateField.setForeground(Color.WHITE);
        dateField.setBackground(new Color(49, 50, 52));
        dateField.setPreferredSize(new Dimension(150, 30));
        JButton searchButton = new JButton("Search");
        searchButton.setForeground(Color.BLACK);
        searchButton.setBackground(Color.WHITE);

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(dateField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        loadChatLog(chatArea);

        backButton.addActionListener(e -> createDashboard());

        sendButton.addActionListener(e -> {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                appendToChatLog(currentUser + " : " + message, date);
                loadChatLog(chatArea);
                messageField.setText("");
            }
        });

        searchButton.addActionListener(e -> {
            String date = dateField.getText().trim();
            if (!date.isEmpty()) {
                loadMessagesByDate(chatArea, date);
            }
        });

        frame.revalidate();
        frame.repaint();
    }

    private static void appendToChatLog(String message, String date) {
        try (FileWriter writer = new FileWriter(CHAT_LOG_FILE, true)) {
            writer.append(message).append(" (" + date + ")\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        addMessageToLinkedList(message, date);
    }

    private static void loadChatLog(JTextArea chatArea) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CHAT_LOG_FILE))) {
            StringBuilder chatContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(" (")) {
                    line = line.substring(0, line.indexOf(" ("));
                }
                chatContent.append(line).append("\n");
            }
            chatArea.setText(chatContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadMessagesByDate(JTextArea chatArea, String date) {
        DateNode current = dateHead;
        while (current != null) {
            if (current.date.equals(date)) {
                StringBuilder messages = new StringBuilder();
                MessageNode messageNode = current.messages;
                while (messageNode != null) {
                    messages.append(messageNode.message).append("\n");
                    messageNode = messageNode.nextMessage;
                }
                chatArea.setText(messages.toString());
                return;
            }
            current = current.nextDate;
        }
        chatArea.setText("No messages found for the date: " + date);
    }

    private static void addMessageToLinkedList(String message, String date) {
        if (dateHead == null) {
            dateHead = new DateNode(date);
            dateHead.messages = new MessageNode(message);
            return;
        }

        DateNode current = dateHead;
        while (current != null) {
            if (current.date.equals(date)) {
                MessageNode newMessage = new MessageNode(message);
                MessageNode temp = current.messages;
                while (temp.nextMessage != null) {
                    temp = temp.nextMessage;
                }
                temp.nextMessage = newMessage;
                return;
            }
            if (current.nextDate == null) break;
            current = current.nextDate;
        }

        DateNode newDateNode = new DateNode(date);
        newDateNode.messages = new MessageNode(message);
        current.nextDate = newDateNode;
    }

    private static void initializeLinkedListsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CHAT_LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(" (")) {
                    int dateStartIndex = line.lastIndexOf(" (");
                    String message = line.substring(0, dateStartIndex).trim();
                    String date = line.substring(dateStartIndex + 2, line.length() - 1).trim();
                    addMessageToLinkedList(message, date);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
