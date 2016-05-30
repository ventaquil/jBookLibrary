import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class BookFrame extends JFrame {
    private static BookFrame instance;
    private JLabel locationLabel;

    private BookFrame()
    {
        super("jBookLibrary");

        JPanel page_start = new JPanel(new FlowLayout(FlowLayout.LEFT));
        page_start.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        /* Set frame icon */
        try {
            setIconImage(ImageIO.read(BookFrame.class.getResourceAsStream("/resources/Icon.png")));
        } catch (IOException e) { }

        /* Set default operation on close */
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* Add content */
        locationLabel = new JLabel("<html><b>Current location:</b> " + getDirectoryText() + "</html>");
        locationLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        page_start.add(locationLabel);
        getContentPane().add(page_start, BorderLayout.PAGE_START);

        /* Set frame size */
        pack();

        /* Set frame minimum size */
        setMinimumSize(new Dimension(300, 150));

        /* Set frame on center of the screen */
        setLocationRelativeTo(null);

        /* Set frame as visible */
        setVisible(true);
    }

    public static BookFrame create()
    {
        instance = new BookFrame();

        return instance;
    }

    private String getDirectoryText()
    {
        return jBookLibrary.getDirectory()
                           .replaceAll(jBookLibrary.getBaseDirectory(), "Your Books");
    }

    public Integer getLabelHeight()
    {
        return locationLabel.getHeight();
    }

    public static BookFrame instance()
    {
        return instance;
    }

    private void refresh()
    {
        /* Refresh frame content */
        invalidate();
        validate();
        repaint();
    }

    public void refreshBooks(String[] books)
    {
        Container pane = getContentPane();
        pane.remove(1);
        pane.remove(1);

        showBooks(books);
    }

    public void showBooks(String[] books)
    {
        final JPopupMenu contextMenu = new JPopupMenu();

        /* Create books list */
        final JList<String> list = new JList<String>(books);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setVisibleRowCount(4);

        /* Add list event listener */
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                clearSelection(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                clearSelection(e);
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
                /* Check if click is on left or right mouse button */
                if (SwingUtilities.isRightMouseButton(e)) {
                    Point point = e.getPoint();
                    int index = list.locationToIndex(point);
                    Rectangle rectangle = list.getCellBounds(index, index);

                    if ((rectangle == null) || !rectangle.contains(point)){
                        list.clearSelection();
                    } else {
                        list.setSelectedIndex(list.locationToIndex(e.getPoint()));

                        String selectedValue = list.getSelectedValue();

                        if (!selectedValue.equals("../")) {
                            contextMenu.show(list, e.getX(), e.getY());
                        }
                    }
                } else {
                    if (e.getClickCount() == 2) {
                        try {
                            String selectedValue = list.getSelectedValue();

                            if (selectedValue != null) {
                                if (selectedValue.substring(selectedValue.length() - 1)
                                                 .equals("/")) {
                                    jBookLibrary.moveTo(selectedValue);
                                    jBookLibrary.refreshFrame();
                                } else {
                                    Desktop.getDesktop()
                                           .open(BookNames.getFile(selectedValue));
                                }
                            }
                        } catch (IOException ex) {}
                    }
                }
            }

            public void clearSelection (MouseEvent e){
                Point point = e.getPoint();
                int index = list.locationToIndex(point);
                Rectangle rectangle = list.getCellBounds(index, index);

                if ((rectangle == null) || !rectangle.contains(point)){
                    list.clearSelection();
                }
            }
        });

        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (list.getSelectedIndex() != -1) {
                        System.out.println(list.getSelectedValue());
                    }
                }
            }
        });

        /* Add menu items */
        JMenuItem menuItem;

        menuItem = new JMenuItem("Rename");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                RenameFrame.create(list.getSelectedValue());
            }
        });
        contextMenu.add(menuItem);

        contextMenu.addSeparator();

        menuItem = new JMenuItem("Delete");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try {
                    Directory.remove(BookNames.getFile(list.getSelectedValue()));
                } catch (IOException ex) { }

                jBookLibrary.refreshFrame();
            }
        });
        contextMenu.add(menuItem);

        JScrollPane scroll = new JScrollPane(list);

        /* Set scroll border */
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        border = BorderFactory.createTitledBorder(border, "eBooks");
        scroll.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5), border));

        /* Create button instance */
        JButton button = new JButton("Add Directory");
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mousePressed(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseClicked(MouseEvent e) {
                AddDirectoryFrame.create();
            }
        });

        getContentPane().add(scroll, BorderLayout.CENTER);

        JPanel page_end = new JPanel();
        page_end.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        page_end.add(button);
        getContentPane().add(page_end, BorderLayout.PAGE_END);

        refresh();
    }

    public void updateDirectory()
    {
        String directoryText = getDirectoryText();
        locationLabel.setText("<html><b>Current location:</b> " + directoryText + "</html>");

        refresh();

        Integer labelWidth = (locationLabel.getWidth() + 10),
                frameWidth = getWidth();
        if (labelWidth > frameWidth) {
            Integer percentage = (labelWidth - frameWidth + (100 * labelWidth / frameWidth)) * 100 / labelWidth,
                    length = directoryText.length();
            locationLabel.setText("<html><b>Current location:</b> ..." + directoryText.substring(percentage * length / 100, length) + "</html>");

            refresh();
        }
    }
};
