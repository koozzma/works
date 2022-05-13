package com.javarush.task.task32.task3209;

import com.javarush.task.task32.task3209.listeners.UndoListener;

import javax.imageio.IIOException;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;

public class Controller {
    private View view;
    private HTMLDocument document;
    private File currentFile;

    public HTMLDocument getDocument() {
        return document;
    }

    public Controller(View view) {
        this.view = view;
    }
    public void init(){
createNewDocument();
    }

    public void resetDocument(){
        UndoListener undoListener = view.getUndoListener();
        if(document != null){
            document.removeUndoableEditListener(undoListener);
        }
        document = (HTMLDocument) new HTMLEditorKit().createDefaultDocument();
        document.addUndoableEditListener(undoListener);
        view.update();
    }
    public void exit(){
        System.exit(0);
    }

    public void setPlainText(String text){
        resetDocument();
        StringReader stringReader = new StringReader(text);
            try {
                new  HTMLEditorKit().read(stringReader,document,0);
            } catch (IOException | BadLocationException e) {
                ExceptionHandler.log(e);
            }
        }

        public String getPlainText() {
            StringWriter stringWriter = new StringWriter();
            try {
                new HTMLEditorKit().write(stringWriter, document, 0, document.getLength());
            } catch (IOException | BadLocationException e) {
                ExceptionHandler.log(e);
            }
            return stringWriter.toString();
        }

    public void createNewDocument() {
     view.selectHtmlTab();
     resetDocument();
     view.setTitle("HTML редактор");
     currentFile = null;

    }

    public void openDocument() {
        view.selectHtmlTab();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new HTMLFileFilter());
        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            resetDocument();
            view.setTitle(currentFile.getName());

            try (FileReader fileReader = new FileReader(currentFile)) {
                new HTMLEditorKit().read(fileReader, document, 0);
            } catch (IOException | BadLocationException e) {
                ExceptionHandler.log(e);
            }

            view.resetUndo();
        }
    }

    public void saveDocument() {
        view.selectHtmlTab();
        if(currentFile!= null){
            try {
                FileWriter fileWriter = new FileWriter(currentFile);
                new HTMLEditorKit().write(fileWriter, document, 0, document.getLength());
            } catch (Exception e) {
                ExceptionHandler.log(e);
            }
        } else {
            saveDocumentAs();
        }
    }
    public void saveDocumentAs() {
     view.selectHtmlTab();
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new HTMLFileFilter());
       if(jFileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
           currentFile = jFileChooser.getSelectedFile();
           view.setTitle(currentFile.getName());

           try {
               FileWriter fileWriter = new FileWriter(currentFile);
               new HTMLEditorKit().write(fileWriter, document, 0, document.getLength());
           } catch (Exception e) {
               ExceptionHandler.log(e);
           }
       }
    }

    public static void main(String[] args) {
     View view = new View();
     Controller controller = new Controller(view);
     view.setController(controller);
     view.init();
     controller.init();
    }
}
