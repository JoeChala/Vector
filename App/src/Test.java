import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Test {
    private static final String USER_CREDENTIALS_FILE = "./users.csv";
    private static final String CALENDAR_EVENTS_FILE = "./calendar.csv";
    private static final String MESSAGES_FILE = "./messages.txt";
    private static JFrame frame;
    private static String currentUser;
    private static MessageNode head = null;

    public static void main(String[] args) {
         initializeLinkedListsFromFile();
        showLoginPage();
    }
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
    private static void showLoginPage() {
	        frame = new JFrame("Login");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(428, 850);
	        frame.setResizable(false);
	        frame.setLayout(null);
	        frame.getContentPane().setBackground(new Color(30, 31, 34));
	        ImageIcon icon = new ImageIcon("./assets/vector_icon.png"); 
	        frame.setIconImage(icon.getImage());
	        BufferedImage originalImage= null;
	try {
		originalImage = ImageIO.read(new File("./assets/vector_logo.jpg"));
	} catch (IOException e) {
		e.printStackTrace();
	}

	        int width = 150; 
	        int height = 181;
	        Image scaledImage =getHighQualityScaledImage(originalImage, width, height);

	        ImageIcon imageIcon = new ImageIcon(scaledImage);
	        JTextField invis = new JTextField();
	        invis.setBounds(0,0,1,1);
	        invis.setForeground(new Color(49,50,52));
	        invis.setBackground(new Color(49,50,52));
	        JLabel imageLabel = new JLabel(imageIcon);
	        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
	        frame.add(imageLabel, BorderLayout.CENTER);
	        imageLabel.setBounds(139,170,150,181);
	        
	        JTextField usernameField = createPlaceholderField("Username");
	        usernameField.setBounds(54, 395, 320, 60);
	        usernameField.setForeground(new Color(255,255,255));
	        usernameField.setBackground(new Color(49,50,52));

	        JPasswordField passwordField = createPlaceholderPasswordField("Password");
	        passwordField.setBounds(54, 488, 320, 60);
	        passwordField.setBackground(new Color(49,50,52));
	        passwordField.setForeground(new Color(255,255,255));

	        JButton loginButton = new JButton("Login");
	        loginButton.setBounds(144, 580, 140, 45);
	        loginButton.setForeground(new Color(0,0,0)); 
	        loginButton.setBackground(new Color(255, 255, 255));
	        loginButton.setBorder(new EmptyBorder(0, 0, 0, 0));
	        loginButton.addActionListener(e -> {
	            String username = usernameField.getText().trim();
	            String password = new String(passwordField.getPassword()).trim();
	            if (checkLoginCredentials(username, password)) {
	                currentUser = username;
	                createDashboard();
	            } else {
	                JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
	            }
	        });

	        JButton signUpButton = new JButton("Sign Up");
	        signUpButton.setBounds(144, 730, 140, 50);
	        signUpButton.setForeground(new Color(0,0,0)); 
	        signUpButton.setBackground(new Color(255, 255, 255));
	        signUpButton.setBorder(new EmptyBorder(0, 0, 0, 0));
	        signUpButton.addActionListener(e -> showSignUpPage());

	        frame.add(imageLabel, BorderLayout.CENTER);
	        frame.add(invis);
	        frame.add(usernameField);
	        frame.add(passwordField);
	        frame.add(loginButton);
	        frame.add(signUpButton);
	        
	        frame.setVisible(true);
	    }
	private static BufferedImage getHighQualityScaledImage(BufferedImage srcImage, int targetWidth, int targetHeight) {
		        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
		        Graphics2D g2d = resizedImage.createGraphics();

		        // Use advanced rendering hints for better quality
		        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		        // Draw the original image to the target size
		        g2d.drawImage(srcImage, 0, 0, targetWidth, targetHeight, null);
		        g2d.dispose();

		        return resizedImage;
	 }

    private static void showSignUpPage() {
        frame.getContentPane().removeAll();
        frame.repaint();
        frame.setTitle("Sign Up");
        ImageIcon icon = new ImageIcon("./assets/vector_icon.png"); 
        frame.setIconImage(icon.getImage());

        JLabel titleLabel = new JLabel("Sign Up");
        titleLabel.setBounds(151, 5, 126, 40);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JTextField nameField = createPlaceholderField("Name");
        nameField.setBounds(54, 130, 320, 60);
        nameField.setBackground(new Color(49,50,52));
        nameField.setForeground(new Color(255,255,255));

        JTextField ageField = createPlaceholderField("Age");
        ageField.setBounds(54, 243, 320, 60);
        ageField.setBackground(new Color(49,50,52));
        ageField.setForeground(new Color(255,255,255));

        JTextField usernameField = createPlaceholderField("Username");
        usernameField.setBounds(54, 356, 320, 60);
        usernameField.setBackground(new Color(49,50,52));
        usernameField.setForeground(new Color(255,255,255));

        JTextField emailField = createPlaceholderField("Email");
        emailField.setBounds(54, 469, 320, 60);
        emailField.setBackground(new Color(49,50,52));
        emailField.setForeground(new Color(255,255,255));

        JPasswordField passwordField = createPlaceholderPasswordField("Password");
        passwordField.setBounds(54, 582, 320, 60);
        passwordField.setBackground(new Color(49,50,52));
        passwordField.setForeground(new Color(255,255,255));

        JButton signUpButton = new JButton("Create Account");
        signUpButton.setBounds(131, 735, 165, 50);
        signUpButton.setForeground(new Color(0,0,0));
        signUpButton.setBackground(new Color(255,255,255));
        signUpButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String age = ageField.getText().trim();
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if ((name.isEmpty() || name == "Name") || (age.isEmpty() || age=="Age") || (username.isEmpty() || username=="Username" ) || (email.isEmpty() || email=="Email") || (password.isEmpty() || password=="Password")) {
                JOptionPane.showMessageDialog(frame, "All fields are required.", "Sign Up Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            saveUserCredentials(username, password);
            JOptionPane.showMessageDialog(frame, "Account created successfully!", "Sign Up Success", JOptionPane.INFORMATION_MESSAGE);
            showLoginPage();
        });

        frame.add(titleLabel);
        frame.add(nameField);
        frame.add(ageField);
        frame.add(usernameField);
        frame.add(emailField);
        frame.add(passwordField);
        frame.add(signUpButton);

        frame.revalidate();
        frame.repaint();
    }

    private static JTextField createPlaceholderField(String placeholder) {
        JTextField textField = new JTextField(placeholder);
        textField.setForeground(Color.WHITE);

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.WHITE);
                }
            }
        });

        return textField;
    }

    private static JPasswordField createPlaceholderPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField(placeholder);
        passwordField.setEchoChar((char) 0);
        passwordField.setForeground(Color.WHITE);

        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.WHITE);
                    passwordField.setEchoChar('*');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText(placeholder);
                    passwordField.setForeground(Color.WHITE);
                    passwordField.setEchoChar((char) 0);
                }
            }
        });

        return passwordField;
    }

    private static boolean checkLoginCredentials(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_CREDENTIALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String storedUsername = parts[0].trim();
                    String storedPassword = parts[1].trim();
                    if (username.equals(storedUsername) && password.equals(storedPassword)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void saveUserCredentials(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_CREDENTIALS_FILE, true))) {
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createDashboard() {
	        frame.getContentPane().removeAll();
	        frame.repaint();
	        frame.setSize(428, 880);
	        frame.setTitle("Home");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setLayout(null);
	        ImageIcon icon = new ImageIcon("./assets/vector_icon.png"); 
	        frame.setIconImage(icon.getImage());

	        JButton calendarButton = createCustomButton("Calendar", "./assets/calendar.png");
	        calendarButton.setBounds(54,177,320,148);
	        calendarButton.addActionListener(e -> showCalendar());

	        JButton meetingButton = createCustomButton("Schedule Meeting", "./assets/meeting.png");
	        meetingButton.setBounds(54,370,320,148);
	        meetingButton.addActionListener(e -> showMeetingScheduler());

	        JButton messageButton = createCustomButton("Message", "./assets./message.png");
	        messageButton.setBounds(54,563,320,148);
	        messageButton.addActionListener(e -> showMessagingPlatform());
	        frame.add(calendarButton);
	        frame.add(meetingButton);
	        frame.add(messageButton);
	        frame.setVisible(true);
	    }

	    private static JButton createCustomButton(String text, String iconPath) {
	        JButton button = new JButton(text);

	        ImageIcon icon = new ImageIcon(iconPath);
	        Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
	        if(text == "Schedule Meeting")
		        img = icon.getImage().getScaledInstance(126, 77, Image.SCALE_SMOOTH);
	        button.setIcon(new ImageIcon(img));

	        button.setFocusPainted(false);
	        button.setBackground(new Color(49, 50, 52));
	        button.setForeground(Color.WHITE); 
	        button.setFont(new Font("Arial", Font.BOLD, 28));
	  
	        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	        return button;
	    }
	    private static Calendar calendar = Calendar.getInstance();

	    private static void showCalendar() {
	        frame.getContentPane().removeAll();
	        frame.setLayout(new BorderLayout());

	        JPanel topPanel = new JPanel(new GridLayout(6, 6)); 
	        topPanel.setPreferredSize(new Dimension(438, 250));
	        topPanel.setBackground(new Color(30, 31, 34));
	        JPanel bottomPanel = new JPanel(new BorderLayout());
	        bottomPanel.setBackground(new Color(30, 31, 34));

	        JLabel eventDetails = new JLabel("<html><body style='font-size:16px;'>Event Details</body></html>", JLabel.CENTER);
	        eventDetails.setPreferredSize(new Dimension(438, 100));
	        eventDetails.setBackground(new Color(30, 31, 34));
	        eventDetails.setForeground(Color.BLACK);
	   

	        JScrollPane eventScrollPane = new JScrollPane(eventDetails);
	        eventScrollPane.setPreferredSize(new Dimension(438, 150));
	        eventScrollPane.setBackground(new Color(30, 31, 34));
	        bottomPanel.add(eventScrollPane, BorderLayout.CENTER);
	        
	        JPanel navigationPanel = new JPanel(new BorderLayout());
	        navigationPanel.setBackground(new Color(30, 31, 34));
	        JButton homeButton = new JButton("Back");
	        homeButton.setPreferredSize(new Dimension(70, 30));
	        homeButton.setBackground(Color.WHITE);
	        homeButton.setForeground(Color.BLACK);
	        homeButton.addActionListener(e -> createDashboard());
	        navigationPanel.add(homeButton, BorderLayout.WEST);

	        JPanel monthNavigationPanel = new JPanel();
	        monthNavigationPanel.setBackground(new Color(30, 31, 34));
	        JButton backButton = new JButton("<");
	        backButton.setPreferredSize(new Dimension(50, 30));
	        backButton.setBackground(new Color(30,31,34));
	        backButton.setBorder(new EmptyBorder(0,0,0, 0));
	        backButton.setForeground(Color.WHITE);
	        backButton.setFont(new Font("Arial",Font.BOLD,18));
	        backButton.addActionListener(e -> {
	            calendar.add(Calendar.MONTH, -1); 
	            showCalendar(); 
	        });

	        JLabel monthLabel = new JLabel("", JLabel.CENTER);
	        monthLabel.setPreferredSize(new Dimension(200, 30));
	        monthLabel.setForeground(Color.WHITE);

	        JButton forwardButton = new JButton(">");
	        forwardButton.setPreferredSize(new Dimension(50, 30));
	        forwardButton.setBackground(new Color(30,31,34));
	        forwardButton.setBorder(new EmptyBorder(0,0,0, 0));
	        forwardButton.setForeground(Color.WHITE);
	        forwardButton.setFont(new Font("Arial",Font.BOLD,18));
	        forwardButton.addActionListener(e -> {
	            calendar.add(Calendar.MONTH, 1); 
	            showCalendar(); 
	        });

	        monthNavigationPanel.add(backButton);
	        monthNavigationPanel.add(monthLabel);
	        monthNavigationPanel.add(forwardButton);
	        navigationPanel.add(monthNavigationPanel, BorderLayout.CENTER);

	        frame.add(navigationPanel, BorderLayout.NORTH);

	        Map<String, List<String>> events = loadCalendarEvents();

	        calendar.set(Calendar.DAY_OF_MONTH, 1);
	        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
	        int monthDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

	        String monthYear = String.format("%1$tB %1$tY", calendar);
	        monthLabel.setText(monthYear);
	        String[] days = {"S", "M", "T", "W", "T", "F", "S"};
	        for (String day : days) {
	            JLabel dayLabel = new JLabel(day, JLabel.CENTER);
	            dayLabel.setSize(62,30);
	            dayLabel.setForeground(Color.WHITE);
	            topPanel.add(dayLabel);
	        }

	        for (int i = 0; i < firstDayOfWeek; i++) {
	            topPanel.add(new JLabel(""));
	        }

	        for (int day = 1; day <= monthDays; day++) {
	            JButton dateButton = new JButton(String.valueOf(day));
	            dateButton.setSize(new Dimension(62, 30)); 
	            dateButton.setBackground(new Color(49, 50, 52));
	            dateButton.setForeground(Color.WHITE);
	            dateButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	            String date = String.format("%02d-%02d-%04d", calendar.get(Calendar.MONTH) + 1, day, calendar.get(Calendar.YEAR));

	            if (day == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && 
	                calendar.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH) &&
	                calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
	                dateButton.setBackground(Color.BLUE);
	                dateButton.setForeground(Color.WHITE);
	            }

	            if (events.containsKey(date)) {
	                dateButton.setBackground(Color.GREEN);
	                dateButton.setForeground(Color.WHITE);

	                dateButton.addActionListener(e -> {
	                    StringBuilder details = new StringBuilder("<html><body style='font-size:16px;'>");
	                    details.append("Events on ").append(date).append(":<br>");
	                    for (String event : events.get(date)) {
	                        details.append(event).append("<br>");
	                    }
	                    details.append("</body></html>");
	                    eventDetails.setText(details.toString());
	                });
	            } else {
	                dateButton.addActionListener(e -> {
	                    eventDetails.setText(String.format("<html><body style='font-size:16px;'>No events on %s</body></html>", date));
	                });
	            }

	            topPanel.add(dateButton);
	        }

	        frame.add(topPanel, BorderLayout.CENTER);
	        frame.add(bottomPanel, BorderLayout.SOUTH);
	        frame.revalidate();
	        frame.repaint();
	    }




    private static Map<String, List<String>> loadCalendarEvents() {
	    Map<String, List<String>> events = new HashMap<>();
	    try (BufferedReader reader = new BufferedReader(new FileReader(CALENDAR_EVENTS_FILE))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] parts = line.split(",");
	            String date = parts[0].trim();
	            String event = parts[1].trim();
	            events.computeIfAbsent(date, k -> new ArrayList<>()).add(event);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return events;
	}

	private static void copyToClipboard(String text) {
		    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
		}

	private static void showMeetingScheduler() {
		    frame.getContentPane().removeAll();
		    frame.setLayout(null);

		    JLabel dateLabel = new JLabel("Enter Date (DD-MM-YYYY):");
		    dateLabel.setBounds(50, 50, 200, 30);
		    dateLabel.setForeground(Color.WHITE);
		    JTextField dateField = new JTextField();
		    dateField.setBounds(250, 50, 100, 30);
		    dateField.setBorder(new EmptyBorder(0, 0, 0, 0));

		    JLabel timeLabel = new JLabel("Enter Time (HH:MM):");
		    timeLabel.setForeground(Color.WHITE);
		    timeLabel.setBounds(50, 100, 200, 30);
		    JTextField timeField = new JTextField();
		    timeField.setBounds(250, 100, 100, 30);
		    timeField.setBorder(new EmptyBorder(0, 0, 0, 0));

		    JLabel meetingLinkLabel = new JLabel("Meeting Link:");
		    meetingLinkLabel.setForeground(Color.WHITE);
		    meetingLinkLabel.setBounds(50, 200, 300, 30);

		    JButton scheduleButton = new JButton("Schedule Meeting");
		    scheduleButton.setBounds(150, 150, 150, 30);
		    scheduleButton.setBackground(Color.WHITE);
		    scheduleButton.setForeground(Color.BLACK);
		    scheduleButton.setBorder(new EmptyBorder(0, 0, 0, 0));

		    JButton copyButton = new JButton("Copy Link");
		    copyButton.setBounds(50, 250, 120, 30);
		    copyButton.setBackground(Color.WHITE);
		    copyButton.setForeground(Color.BLACK);
		    copyButton.setBorder(new EmptyBorder(0, 0, 0, 0));
		    copyButton.setEnabled(false); 

		    JButton backButton = new JButton("Back");
		    backButton.setBounds(10, 10, 80, 30);
		    backButton.setBackground(Color.WHITE);
		    backButton.setForeground(Color.BLACK);
		    backButton.setBorder(new EmptyBorder(0, 0, 0, 0));

		    frame.add(dateLabel);
		    frame.add(dateField);
		    frame.add(timeLabel);
		    frame.add(timeField);
		    frame.add(meetingLinkLabel);
		    frame.add(scheduleButton);
		    frame.add(copyButton);
		    frame.add(backButton);

		    backButton.addActionListener(e -> createDashboard());

		    scheduleButton.addActionListener(e -> {
		        String date = dateField.getText();
		        String time = timeField.getText();
		        String meetingLink = "https://meet.jit.si/" + UUID.randomUUID();

		        meetingLinkLabel.setText("Meeting Link: " + meetingLink);
		        copyButton.setEnabled(true); // Enable copy button
		        saveMeetingToCalendar(date, "Meeting", time + " - " + meetingLink);
		    });

		    copyButton.addActionListener(e -> {
		        String link = meetingLinkLabel.getText().replace("Meeting Link: ", "");
		        copyToClipboard(link);
		        JOptionPane.showMessageDialog(frame, "Meeting link copied to clipboard!", "Copied", JOptionPane.INFORMATION_MESSAGE);
		    });

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
			        try (FileWriter writer = new FileWriter(MESSAGES_FILE, true)) {
			            writer.append(message).append(" (" + date + ")\n");
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			        addMessageToLinkedList(message, date);
			    }

			    private static void loadChatLog(JTextArea chatArea) {
			        try (BufferedReader reader = new BufferedReader(new FileReader(MESSAGES_FILE))) {
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
		private static void saveMeetingToCalendar(String date, String title, String details) {
		    try (FileWriter writer = new FileWriter(CALENDAR_EVENTS_FILE, true)) {
		        writer.append(date).append(",").append(title).append(",").append(details).append("\n");
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}   
		private static void initializeLinkedListsFromFile() {
			        try (BufferedReader reader = new BufferedReader(new FileReader(MESSAGES_FILE))) {
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
