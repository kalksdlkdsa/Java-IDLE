import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.RTextScrollPane;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class code_editor {
    private static RSyntaxTextArea textArea;
    private static JFrame frame;
    private static boolean isModified = false;
    private static File currentFile = null;

    private static void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                FileReader reader = new FileReader(fileChooser.getSelectedFile());
                BufferedReader br = new BufferedReader(reader);
                textArea.read(br, null);
                br.close();
                textArea.requestFocus();
                frame.setTitle(fileChooser.getSelectedFile().getName());
                isModified = false;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    private static void saveFile() {
        if (currentFile == null) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile(); // Lưu file hiện tại
            } else {
                return; // Người dùng đã hủy việc lưu file
            }
        }
        try (FileWriter writer = new FileWriter(currentFile)) {
            textArea.write(writer);
            frame.setTitle(currentFile.getName());
            isModified = false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private static void newFile() {
        textArea.setText("");
        frame.setTitle("New File");
        isModified = false;
        currentFile = null;
    }

    public static int __init__() {
        frame = new JFrame("Java IDLE preclassic 100283928");
        textArea = new RSyntaxTextArea();
        textArea.setSyntaxEditingStyle("text/java");
        RTextScrollPane sp = new RTextScrollPane(textArea);
        frame.add(sp);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (isModified) {
                    int option = JOptionPane.showConfirmDialog(frame,
                            "Bạn có muốn lưu thay đổi không?",
                            "Xác nhận",
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        saveFile();
                    } else if (option == JOptionPane.CANCEL_OPTION) {
                        frame.setDefaultCloseOperation(0);
                    } else {
                        frame.setDefaultCloseOperation(3);
                    }
                } else {
                    frame.setDefaultCloseOperation(3);
                }
            }
        });
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                markAsModified();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                markAsModified();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                markAsModified();
            }

            private void markAsModified() {
                if (!frame.getTitle().startsWith("*")) {
                    frame.setTitle("*" + frame.getTitle());
                }
                isModified = true;
            }
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        newMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newFile();
            }
        });

        openMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });

        saveMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        frame.setSize(800,600);
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);
        return 0;
    }

    public static void main(String[] args) {
        __init__();
    }
}