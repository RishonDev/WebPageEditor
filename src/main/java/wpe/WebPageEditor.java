package wpe;

import wpe.core.SoftwareInfo;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;

public class WebPageEditor extends Definitions{
    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
        htmlViewer.setEditable(false);
        textPane.setText("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1 style=\"background-color: red;\">Hello World!</h1>\n" +
                "<p>This is a paragraph.</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n");
        frame.addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                textPane.requestFocusInWindow();
            }
        });
        lines.setBackground(new Color(232,232,232));
        lines.setEditable(false);
        scrollPane.setRowHeaderView(lines);

        //Menu Bar properties on macOS

        if(SoftwareInfo.getPlatform().equals("Mac OS X")){
            Desktop desktop = Desktop.getDesktop();
            desktop.setAboutHandler(e -> JOptionPane.showMessageDialog(frame, "WaveTextEditor Version " + SoftwareInfo.getVersion() + "\n" + SoftwareInfo.getAbout()));
            desktop.setPreferencesHandler(e -> JOptionPane.showMessageDialog(null, "Preferences dialog"));
            desktop.setQuitHandler((e, r) -> {
                int response = JOptionPane.showConfirmDialog(frame, "Are you sure you want to quit?", "Are you sure?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(response == JOptionPane.YES_OPTION) {
                    try {
                        quit();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            Taskbar taskbar = Taskbar.getTaskbar();
            if (SoftwareInfo.getOSVersion().charAt(1) != '0')
                taskbar.setIconImage(tk.getImage("./src/main/resources/images/app_icons/big_sur/icon_256x256@2x.png"));
            else
                taskbar.setIconImage(tk.getImage("./src/main/resources/images/app_icons/catalina/icon_256x256@2x.png"));
            textPane.getActionMap().put("del2EoL", new Delete2EOL());
            textPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, tk.getMenuShortcutKeyMaskEx()), "del2EoL");
            System.setProperty( "apple.laf.useScreenMenuBar", "true" );
            System.setProperty( "apple.awt.application.name", "WaveTextEditor" );
            System.setProperty( "apple.awt.application.appearance", "system" );

                frame.getRootPane().putClientProperty("apple.awt.transparentTitleBar", true);

            frame.getRootPane().putClientProperty( "apple.awt.fullscreenable", true );
        }
        //If it's not macOS, sets to Windows/Linux look with toolbar within the application
        else {
            frame.setJMenuBar(menuBar);
            frame.setIconImage(tk.getImage("./src/main/resources/images/app_icons/other/icon_256x256@2x.png"));
        }
        frame.setTitle("Untitled");
        addAccelerators();
        addActionListeners();
        addComponentsToFrame();
    }

    public static void addActionListeners(){
        hide.addActionListener(e -> {
            frame.setVisible(!hide.isSelected());
        });
        subscript.addActionListener(e -> {
            textPane.replaceSelection("<sub>" + textPane.getSelectedText() + "</sub>");

        });
        superscript.addActionListener(e ->{
            textPane.replaceSelection("<sup>" + textPane.getSelectedText() + "</sup>");
        });
        strikeThrough.addActionListener(e ->{
            textPane.replaceSelection("<s>" + textPane.getSelectedText() + "</s>");
        });
        underline.addActionListener(e -> {
            textPane.replaceSelection("<u>" + textPane.getSelectedText() + "</u>");
        });
        bold.addActionListener(e -> {
           textPane.replaceSelection("<b>" + textPane.getSelectedText() + "</b>");
        });
        italic.addActionListener(e -> {
            textPane.replaceSelection("<i>" + textPane.getSelectedText() + "</i>");
        });
        zoom.addActionListener(e -> {
           if(initialFont == null){
               textPane.getFont();
           }
        });
        textPane.getDocument().addDocumentListener(new DocumentListener() {
            public String getText() {
                int caretPosition = textPane.getDocument().getLength();
                Element root = textPane.getDocument().getDefaultRootElement();
                String text = "1" + System.getProperty("line.separator");
                for(int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
                    text += i + System.getProperty("line.separator");
                }
                return text;
            }
            @Override
            public void changedUpdate(DocumentEvent de) {
                lines.setText(getText());
                htmlViewer.setText(textPane.getText());
            }
            @Override
            public void insertUpdate(DocumentEvent de) {
                lines.setText(getText());
                htmlViewer.setText(textPane.getText());
            }
            @Override
            public void removeUpdate(DocumentEvent de) {
                lines.setText(getText());
                htmlViewer.setText(textPane.getText());
            }
        });
        moveWindowToLeft.addActionListener(e ->{
            frame.setLocation(0, frame.getY());
        });
        moveWindowToRight.addActionListener(e ->{
           frame.setLocation(screenSize.width-frame.getWidth(), frame.getY());
        });
        moveWindowToTopLeft.addActionListener(e ->{
            frame.setLocation(0, 0);
        });
        moveWindowToTopRight.addActionListener(e ->{
           frame.setLocation(screenSize.width-frame.getWidth(),0);
        });
        moveWindowToBottomLeft.addActionListener(e ->{
           frame.setLocation(0, screenSize.height-frame.getHeight());
        });
        moveWindowToBottomRight.addActionListener(e ->{
            frame.setLocation(screenSize.width-frame.getWidth(), screenSize.height-frame.getHeight());
        });
        minimize.addActionListener(e ->{
            if(minimize.isSelected())
                frame.setState(Frame.ICONIFIED);
            else
                frame.setState(Frame.NORMAL);
        });
        copy.addActionListener(e -> textPane.copy());
        cut.addActionListener(e -> textPane.cut());
        paste.addActionListener(e -> textPane.paste());
        saveAs.addActionListener(e -> {
            int response = chooser.showSaveDialog(frame);
            if (response == JFileChooser.APPROVE_OPTION) {
                if(new File(chooser.getSelectedFile().getAbsolutePath()).exists()){
                    if(JOptionPane.showConfirmDialog(frame, "The File you are creating already exists. Overwrite it?","File already exists", JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION){
                        currentFile = new File(chooser.getSelectedFile().getAbsolutePath());
                        try {
                            fileWriter = new FileWriter(currentFile);
                            textPane.write(fileWriter);
                        } catch (IOException ex) {
                            wpe.core.Notification.systemNotification(ex.getMessage(), "Error saving the file");
                        }
                        frame.setTitle(currentFile.getName());
                    }
                }
                else{
                    currentFile = chooser.getSelectedFile();
                    try {
                        fileWriter = new FileWriter(currentFile);
                        textPane.write(fileWriter);
                    }catch (IOException ex) {
                        wpe.core.Notification.systemNotification(ex.getMessage(), "Error saving the file");
                    }
                    frame.setTitle(currentFile.getName());
                }

            }
        });
        save.addActionListener(e -> {
            if(currentFile == null){
                int res = chooser.showSaveDialog(frame);
                if(res == JFileChooser.APPROVE_OPTION){
                    try {
                        currentFile = chooser.getSelectedFile();
                        fileWriter = new FileWriter(chooser.getSelectedFile());
                        textPane.write(fileWriter);
                        frame.setTitle(currentFile.getName());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame,"","Error Saving", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
            else{
                try {
                    fileWriter = new FileWriter(currentFile);
                    textPane.write(fileWriter);
                    frame.setTitle(currentFile.getName());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        open.addActionListener(e -> {
            int returnVal = chooser.showOpenDialog(frame); //replace null with your swing container
            if(returnVal == JFileChooser.APPROVE_OPTION){
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(chooser.getSelectedFile()));
                    textPane.read(reader,null);
                    reader.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame,"Unable to open file" + Arrays.toString(ex.getStackTrace()), "Unable to open file", JOptionPane.ERROR_MESSAGE);
                }
            }
            currentFile=chooser.getSelectedFile();
            frame.setTitle(currentFile.getName());
        });
        bold.addActionListener(e ->{
            SimpleAttributeSet bold = new SimpleAttributeSet();
            StyleConstants.setBold(bold, true);
            textPane.setCharacterAttributes(bold, false);
        });
        textPane.getDocument().addUndoableEditListener(
                e -> undoManager.addEdit(e.getEdit()));
        newFile.addActionListener(e -> {
            currentFile = null;
            frame.setTitle("Untitled");
            textPane.setText("");
        });
        undo.addActionListener(e -> {
            try {
                undoManager.undo();
            } catch (CannotUndoException cre) {
                JOptionPane.showMessageDialog(frame, "Error... Unable to Undo\n" + Arrays.toString(cre.getStackTrace()) ,"Error Undoing the operation", JOptionPane.ERROR_MESSAGE);
            }
        });
        redo.addActionListener(e -> {
            try {
                undoManager.redo();
            } catch (CannotRedoException cre) {
                JOptionPane.showMessageDialog(frame, "Error... Unable to Redo\n" + Arrays.toString(cre.getStackTrace()),"Error Redoing the operation", JOptionPane.ERROR_MESSAGE);
            }
        });
        about.addActionListener(e -> JOptionPane.showMessageDialog(frame, "WaveTextEditor Version " + SoftwareInfo.getVersion() + "\n" + SoftwareInfo.getAbout()));
    }

    public static void addComponentsToFrame(){
        fileMenu.add(newFile);
        fileMenu.add(open);
        fileMenu.add(openRecent);
        fileMenu.addSeparator();
        fileMenu.add(save);
        fileMenu.add(saveAs);
        fileMenu.add(saveAll);
        fileMenu.addSeparator();
        fileMenu.add(close);
        if(!SoftwareInfo.getPlatform().equals("Mac OS X"))fileMenu.add(exit);
        menuBar.add(fileMenu);

        editMenu.add(copy);
        editMenu.add(undo);
        editMenu.add(redo);
        editMenu.add(cut);
        editMenu.add(paste);
        menuBar.add(editMenu);


        formatMenu.add(bold);
        formatMenu.add(italic);
        formatMenu.add(underline);
        formatMenu.add(strikeThrough);
        formatMenu.add(superscript);
        formatMenu.add(subscript);
        menuBar.add(formatMenu);

        viewMenu.add(zoomIn);
        viewMenu.add(zoomOut);

        menuBar.add(viewMenu);
        windowMenu.add(zoom);
        windowMenu.add(minimize);
        windowMenu.add(hide);
        windowMenu.addSeparator();
        windowMenu.add(moveWindowToLeft);
        windowMenu.add(moveWindowToRight);
        windowMenu.add(moveWindowToTopLeft);
        windowMenu.add(moveWindowToTopRight);
        windowMenu.add(moveWindowToBottomLeft);
        windowMenu.add(moveWindowToBottomRight);
        menuBar.add(windowMenu);
        if(!SoftwareInfo.getPlatform().equals("Mac OS X")) helpMenu.add(about);
        helpMenu.add(checkForUpdates);
        helpMenu.add(bugReport);
        helpMenu.add(askAQuestion);
        helpMenu.add(docs);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);
        scrollPane.setAutoscrolls(true);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        htmlViewer.setContentType("text/html");
        htmlViewerScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.setLayout(null);
        scrollPane.setBounds(0, 0, 400, 500);
        htmlViewerScrollPane.setBounds(400,0, 400, 500);
        frame.add(scrollPane);
        frame.add(htmlViewerScrollPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
    public static void addAccelerators(){
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, tk.getMenuShortcutKeyMaskEx()));
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, tk.getMenuShortcutKeyMaskEx()));
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, tk.getMenuShortcutKeyMaskEx()));
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, tk.getMenuShortcutKeyMaskEx()));
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, tk.getMenuShortcutKeyMaskEx()));
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, tk.getMenuShortcutKeyMaskEx()));
        openRecent.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK));
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, tk.getMenuShortcutKeyMaskEx()));
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, tk.getMenuShortcutKeyMaskEx()));
        saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, tk.getMenuShortcutKeyMaskEx() | InputEvent.SHIFT_DOWN_MASK));
    }
    public static void quit() throws IOException {
        if(currentFile == null){
            int response = JOptionPane.showConfirmDialog(frame, "Do you want to save the document?(Press Cancel to do nothing)", "Save your file?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(response == JOptionPane.YES_OPTION){
                chooser.showSaveDialog(frame);
                fileWriter = new FileWriter(chooser.getSelectedFile());
                textPane.write(fileWriter);
            }
            else if(response != JOptionPane.CANCEL_OPTION || response != JOptionPane.YES_OPTION){
                if(fileWriter != null){
                    fileWriter.close();
                    System.exit(0);
                }
                else System.exit(0);
            }
        }
        else System.exit(0);
    }

    private static class Delete2EOL extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
//            int caretOffset = textPane.getCaretPosition();
//            int lineNumber = 0;
//            try {
//                lineNumber = textPane.getLineOfOffset(caretOffset);
//                int startOffset = textPane.getLineStartOffset(lineNumber);
//                int endOffset = textPane.getLineEndOffset(lineNumber);
//                textPane.replaceRange("", startOffset, endOffset);
//            } catch (BadLocationException ex) {
//                throw new RuntimeException(ex);
//            }
        }
    }
}
