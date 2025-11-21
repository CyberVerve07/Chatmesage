import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

 class TwoWayColorfulChat extends JFrame {

    private JTextArea chatAreaLeft, chatAreaRight;
    private JTextArea messageAreaLeft, messageAreaRight;
    private JButton sendButtonLeft, sendButtonRight;

    public TwoWayColorfulChat() {
        setTitle("Two-Way Colorful Chat Messenger");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Root panel with gradient background
        JPanel root = new GradientBackgroundPanel();
        root.setLayout(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(root);

        // ==== LEFT CHAT PANEL (User 1) ====
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(
                BorderFactory.createTitledBorder("User 1")
        );

        chatAreaLeft = createChatArea(new Color(240, 255, 255));  // Light Cyan
        messageAreaLeft = createMessageArea();
        sendButtonLeft = createSendButton(new Color(70, 130, 180)); // Steel Blue

        JScrollPane scrollPaneLeft = new JScrollPane(chatAreaLeft);
        JScrollPane messageScrollLeft = new JScrollPane(messageAreaLeft);

        JPanel inputPanelLeft = new JPanel(new BorderLayout(5, 5));
        inputPanelLeft.setOpaque(false);
        inputPanelLeft.add(messageScrollLeft, BorderLayout.CENTER);
        inputPanelLeft.add(sendButtonLeft, BorderLayout.EAST);

        leftPanel.add(scrollPaneLeft, BorderLayout.CENTER);
        leftPanel.add(inputPanelLeft, BorderLayout.SOUTH);

        // ==== RIGHT CHAT PANEL (User 2) ====
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(
                BorderFactory.createTitledBorder("User 2")
        );

        chatAreaRight = createChatArea(new Color(255, 240, 245)); // Lavender Blush
        messageAreaRight = createMessageArea();
        sendButtonRight = createSendButton(new Color(255, 105, 180)); // Hot Pink

        JScrollPane scrollPaneRight = new JScrollPane(chatAreaRight);
        JScrollPane messageScrollRight = new JScrollPane(messageAreaRight);

        JPanel inputPanelRight = new JPanel(new BorderLayout(5, 5));
        inputPanelRight.setOpaque(false);
        inputPanelRight.add(messageScrollRight, BorderLayout.CENTER);
        inputPanelRight.add(sendButtonRight, BorderLayout.EAST);

        rightPanel.add(scrollPaneRight, BorderLayout.CENTER);
        rightPanel.add(inputPanelRight, BorderLayout.SOUTH);

        // ==== SPLIT PANE ====
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(450);
        splitPane.setResizeWeight(0.5);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);

        root.add(splitPane, BorderLayout.CENTER);

        // ==== ACTIONS ====

        // Left send button
        sendButtonLeft.addActionListener(e ->
                sendMessage(messageAreaLeft, chatAreaLeft, chatAreaRight, "User 1")
        );

        // Right send button
        sendButtonRight.addActionListener(e ->
                sendMessage(messageAreaRight, chatAreaRight, chatAreaLeft, "User 2")
        );

        // Enter key (no Shift) to send - LEFT
        messageAreaLeft.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isShiftDown()) {
                    e.consume();
                    sendButtonLeft.doClick();
                }
            }
        });

        // Enter key (no Shift) to send - RIGHT
        messageAreaRight.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isShiftDown()) {
                    e.consume();
                    sendButtonRight.doClick();
                }
            }
        });
    }

    // ==== HELPERS ====

    // Chat display area (read-only)
    private JTextArea createChatArea(Color bgColor) {
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ta.setBackground(bgColor);
        ta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return ta;
    }

    // Message input area
    private JTextArea createMessageArea() {
        JTextArea ta = new JTextArea(3, 20);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));
        return ta;
    }

    // Send buttons
    private JButton createSendButton(Color bgColor) {
        JButton btn = new JButton("Send");
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Core send logic
    private void sendMessage(JTextArea inputArea,
                             JTextArea senderChat,
                             JTextArea receiverChat,
                             String user) {

        String message = inputArea.getText().trim();
        if (message.isEmpty()) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        String formattedMessage = user + ": " + message + "\n";

        senderChat.append(formattedMessage);
        receiverChat.append(formattedMessage);

        inputArea.setText("");

        senderChat.setCaretPosition(senderChat.getDocument().getLength());
        receiverChat.setCaretPosition(receiverChat.getDocument().getLength());
    }

    // ==== BACKGROUND PANEL WITH CLEAN GRADIENT + SHAPES ====

    private static class GradientBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();
            int w = getWidth();
            int h = getHeight();

            // Smooth edges
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Gradient background
            GradientPaint gp = new GradientPaint(
                    0, 0, new Color(255, 228, 240),
                    w, h, new Color(220, 240, 255)
            );
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);

            // Soft circles (top-left to center)
            g2d.setColor(new Color(255, 105, 180, 60));
            for (int i = 0; i < 4; i++) {
                int size = 140 + i * 40;
                int x = 20 + i * 40;
                int y = 10 + i * 30;
                g2d.fillOval(x, y, size, size);
            }

            // Soft rectangles (right side)
            g2d.setColor(new Color(30, 144, 255, 50));
            for (int i = 0; i < 4; i++) {
                int rectWidth = 180;
                int rectHeight = 70;
                int x = w - rectWidth - 40 - (i * 20);
                int y = 40 + i * 80;
                g2d.fillRoundRect(x, y, rectWidth, rectHeight, 30, 30);
            }

            g2d.dispose();
        }
    }

    // ==== MAIN ====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TwoWayColorfulChat chat = new TwoWayColorfulChat();
            chat.setVisible(true);
        });
    }
}
