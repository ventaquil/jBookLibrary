import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Frame;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddDirectoryFrame extends JFrame {
    private static AddDirectoryFrame instance;

    private AddDirectoryFrame()
    {
        super("Add new");

        Border paddingBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);

        JPanel page_start = new JPanel();
        page_start.setBorder(paddingBorder);

        JPanel page_end = new JPanel();
        page_end.setBorder(paddingBorder);

        JLabel label = new JLabel("Enter directory name", JLabel.CENTER);
        page_start.add(label);
        getContentPane().add(page_start, BorderLayout.PAGE_START);

        final JTextField textField = new JTextField();
        textField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5), textField.getBorder()));
        getContentPane().add(textField, BorderLayout.CENTER);

        JButton button = new JButton("Add");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (textField.getText()
                             .length() == 0) {
                    // @TODO show error
                } else {
                    Matcher matcher = Pattern.compile("^[\\w_][\\w\\s_-]*$")
                                             .matcher(textField.getText());
                    if (matcher.find()) {
                        jBookLibrary.addDirectory(textField.getText());

                        /* Hide frame */
                        setVisible(false);

                        /* Clear text field */
                        textField.setText(null);
                    } else {
                        // @TODO error
                    }
                }
            }
        });
        page_end.add(button);
        getContentPane().add(page_end, BorderLayout.PAGE_END);

        /* Set frame size */
        pack();

        /* Disable frame resize action */
        setResizable(false);

        /* Set frame on center of the screen */
        setLocationRelativeTo(null);

        /* Set frame as visible */
        setVisible(true);
    }

    public static void create()
    {
        if (instance == null) {
            instance = new AddDirectoryFrame();
        } else {
            if(instance.getState() != Frame.NORMAL) {
                instance.setState(Frame.NORMAL);
            }

            instance.setLocationRelativeTo(null);
            instance.setVisible(true);
            instance.toFront();
            instance.requestFocus();
        }
    }
};
