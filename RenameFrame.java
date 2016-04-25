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

public class RenameFrame extends JFrame {
    private static RenameFrame instance;
    private String name;

    public static void create(String fileName)
    {
        System.out.println(fileName);

        if (instance == null) {
            instance = new RenameFrame(fileName);
        } else {
            if(instance.getState() != Frame.NORMAL) {
                instance.setState(Frame.NORMAL);
            }

            instance.sendFileName(fileName);

            instance.setLocationRelativeTo(null);
            instance.setVisible(true);
            instance.toFront();
            instance.requestFocus();
        }
    }

    private String cut(String fileName)
    {
        int indexOfSlash = fileName.indexOf('/');
        if (indexOfSlash != -1) {
            return fileName.substring(0, indexOfSlash);
        }

        return fileName;
    }

    private RenameFrame(String fileName)
    {
        super("Rename");

        sendFileName(fileName);

        Border paddingBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);

        JPanel page_start = new JPanel();
        page_start.setBorder(paddingBorder);

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(paddingBorder);

        JPanel page_end = new JPanel();
        page_end.setBorder(paddingBorder);

        JLabel label = new JLabel("Enter new name", JLabel.CENTER);
        page_start.add(label);
        getContentPane().add(page_start, BorderLayout.PAGE_START);

        final JTextField oldTextField = new JTextField(cut(name));
        oldTextField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5), oldTextField.getBorder()));
        oldTextField.setEditable(false);
        center.add(oldTextField, BorderLayout.PAGE_START);

        final JTextField textField = new JTextField();
        textField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5), textField.getBorder()));
        center.add(textField, BorderLayout.PAGE_END);

        getContentPane().add(center, BorderLayout.CENTER);

        JButton button = new JButton("Rename");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (textField.getText()
                             .length() == 0) {
                    // @TODO show error
                } else {
                    Matcher matcher = Pattern.compile("^[\\w_][\\w\\s_-]*$")
                                             .matcher(textField.getText());
                    if (matcher.find()) {
                        jBookLibrary.rename(oldTextField.getText(), textField.getText());

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

    private void sendFileName(String fileName)
    {
        if (name != fileName) {
            if (getContentPane().getComponentCount() == 3) {
                JPanel panel = (JPanel) getContentPane().getComponent(1);

                JTextField oldTextField = (JTextField) panel.getComponent(0);
                oldTextField.setText(cut(fileName));

                JTextField textField = (JTextField) panel.getComponent(1);
                textField.setText(null);

                /* Set frame size */
                pack();
            }
        }

        name = fileName;
    }
};
