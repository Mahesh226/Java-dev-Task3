import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyDesignUI extends JFrame {
    private JPanel mainPanel;
    private JButton addQuestionButton;
    private JButton saveSurveyButton;
    private JButton startSurveyButton;
    private JButton generateReportButton;
    private List<JPanel> questionPanels;
    private List<String> questions;
    private Map<String, List<String>> responses;

    public SurveyDesignUI(String title) {
        super(title);
        questionPanels = new ArrayList<>();
        questions = new ArrayList<>();
        responses = new HashMap<>();
        mainPanel = new JPanel(new GridLayout(0, 1));
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        addQuestionButton = new JButton("Add Question");
        saveSurveyButton = new JButton("Save Survey");
        startSurveyButton = new JButton("Start Survey");
        generateReportButton = new JButton("Generate Report");
        buttonPanel.add(addQuestionButton);
        buttonPanel.add(saveSurveyButton);
        buttonPanel.add(startSurveyButton);
        buttonPanel.add(generateReportButton);
        this.add(buttonPanel, BorderLayout.SOUTH);

        addQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addQuestionPanel();
            }
        });

        saveSurveyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSurvey();
            }
        });

        startSurveyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSurvey();
            }
        });

        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
    }

    private void addQuestionPanel() {
        JPanel questionPanel = new JPanel(new FlowLayout());
        JLabel questionLabel = new JLabel("Question:");
        JTextField questionField = new JTextField(20);

        questionPanel.add(questionLabel);
        questionPanel.add(questionField);

        questionPanels.add(questionPanel);
        mainPanel.add(questionPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
        pack();
    }

    private void saveSurvey() {
        questions.clear();
        for (JPanel questionPanel : questionPanels) {
            JTextField questionField = (JTextField) questionPanel.getComponent(1);
            questions.add(questionField.getText());
        }
        JOptionPane.showMessageDialog(this, "Survey Saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void startSurvey() {
        for (String question : questions) {
            collectResponse(question);
        }
    }

    private void collectResponse(String question) {
        JPanel panel = new JPanel();
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton yesButton = new JRadioButton("Yes");
        JRadioButton noButton = new JRadioButton("No");
        buttonGroup.add(yesButton);
        buttonGroup.add(noButton);
        panel.add(new JLabel(question));
        panel.add(yesButton);
        panel.add(noButton);

        int result = JOptionPane.showConfirmDialog(null, panel, "Choose Response", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String response = "";
            if (yesButton.isSelected()) {
                response = "Yes";
            } else if (noButton.isSelected()) {
                response = "No";
            } else {
                response = "No Response";
            }
            if (responses.containsKey(question)) {
                responses.get(question).add(response);
            } else {
                List<String> responseList = new ArrayList<>();
                responseList.add(response);
                responses.put(question, responseList);
            }
        }
    }

    private void generateReport() {
        StringBuilder report = new StringBuilder("Survey Report:\n");
        for (Map.Entry<String, List<String>> entry : responses.entrySet()) {
            String question = entry.getKey();
            List<String> responseList = entry.getValue();
            report.append("\nQuestion: ").append(question);
            report.append("\nResponses: ");
            for (String response : responseList) {
                report.append(response).append(", ");
            }
            report.delete(report.length() - 2, report.length()); // Remove trailing comma and space
            report.append("\n");
        }
        JOptionPane.showMessageDialog(this, report.toString(), "Survey Report", JOptionPane.PLAIN_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new SurveyDesignUI("Survey Design");
            frame.setSize(400, 300);
            frame.setVisible(true);
        });
    }
}
