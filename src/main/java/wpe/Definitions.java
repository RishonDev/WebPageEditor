package wpe;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;

public class Definitions {
    static FileWriter fileWriter;
    static UndoManager undoManager = new UndoManager();
    static File currentFile;
    static JFileChooser chooser = new JFileChooser();
    static JFrame frame = new JFrame();
    static JTextPane textPane = new JTextPane();
    static JScrollPane scrollPane = new JScrollPane(textPane);
    static JTextPane htmlViewer = new JTextPane();
    static JScrollPane htmlViewerScrollPane = new JScrollPane(htmlViewer);
    //Menu Bar
    static JMenuBar menuBar = new JMenuBar();
    //Menu
    static JMenu fileMenu = new JMenu("File");
    static JMenu editMenu = new JMenu("Edit");
    static JMenu viewMenu = new JMenu("View");
    static JMenu helpMenu = new JMenu("Help");
    static JMenu formatMenu = new JMenu("Format");
    static JMenu windowMenu = new JMenu("Window");
    //File Menu Items
    static JMenuItem newFile = new JMenuItem("New File..");
    static JMenuItem open = new JMenuItem("Open");
    static JMenuItem openRecent = new JMenuItem("Open Recent");
    static JMenuItem save = new JMenuItem("Save");
    static JMenuItem saveAs = new JMenuItem("Save As");
    static JMenuItem saveAll = new JMenuItem("Save All");
    static JMenuItem close = new JMenuItem("Close Document");
    static JMenuItem exit = new JMenuItem("Exit");
    //View Menu Items
    static JMenuItem zoomIn = new JMenuItem("Zoom In");
    static JMenuItem zoomOut = new JMenuItem("Zoom Out");
    //Edit Menu Items
    static JMenuItem undo = new JMenuItem("Undo");
    static JMenuItem redo = new JMenuItem("Redo");
    static JMenuItem cut = new JMenuItem("Cut");
    static JMenuItem copy = new JMenuItem("Copy");
    static JMenuItem paste = new JMenuItem("Paste");
    static JMenuItem clearLine = new JMenuItem();
    //Help Menu Items
    //Format Menu Items
    static JMenuItem bold = new JMenuItem("Bold");
    static JMenuItem italic = new JMenuItem("Italic");
    static JMenuItem underline = new JMenuItem("Underline");
    static JMenuItem strikeThrough = new JMenuItem("Strike-Through");
    static JMenuItem superscript = new JMenuItem("Superscript");
    static JMenuItem subscript = new JMenuItem("Subscript");
    static JMenuItem font = new JMenuItem("Font");
    static JMenuItem highlight = new JMenuItem("Highlight");
    static JMenuItem color = new JMenuItem("Color");
    //Window Menu Items
    static JCheckBoxMenuItem minimize = new JCheckBoxMenuItem("Minimize");
    static JMenuItem zoom = new JMenuItem("Zoom");
    static JCheckBoxMenuItem hide = new JCheckBoxMenuItem("Hide");
    static JMenuItem moveWindowToLeft = new JMenuItem("Move Window To Left Side Of The Screen");
    static JMenuItem moveWindowToRight = new JMenuItem("Move Window To Right Side Of The Screen");
    static JMenuItem moveWindowToTopRight = new JMenuItem("Move Window To Top Right Of The Screen");
    static JMenuItem moveWindowToTopLeft = new JMenuItem("Move Window To Top Left Of The Screen");
    static JMenuItem moveWindowToBottomLeft = new JMenuItem("Move Window To Bottom Left Of The Screen");
    static JMenuItem moveWindowToBottomRight = new JMenuItem("Move Window To Bottom Right Of The Screen");
    static final Toolkit tk = Toolkit.getDefaultToolkit();
    //Help Menu Items
    static JMenuItem about = new JMenuItem("About...");
    static JMenuItem checkForUpdates = new JMenuItem("Check For Updates");
    static JMenuItem bugReport = new JMenuItem("Bug Report");
    static JMenuItem askAQuestion = new JMenuItem("Ask A Question");
    static JMenuItem docs = new JMenuItem("Documentation");
    static JMenuItem preferences = new JMenuItem("Preferences");
    static JTextArea lines = new JTextArea("1");
    static Dimension screenSize = tk.getScreenSize();
    static Font initialFont = null;
}
