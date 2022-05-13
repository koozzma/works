package com.javarush.task.task32.task3209;

import com.javarush.task.task32.task3209.listeners.FrameListener;
import com.javarush.task.task32.task3209.listeners.TabbedPaneChangeListener;
import com.javarush.task.task32.task3209.listeners.UndoListener;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener {
  private  Controller controller;
  private JTabbedPane tabbedPane = new JTabbedPane(); //панель с двумя вкладками.
    private JTextPane htmlTextPane = new JTextPane(); // компонент для визуального редактирования html.
    private JEditorPane plainTextPane = new JEditorPane(); //компонент для редактирования html в виде текста, он будет отображать код html (теги и их содержимое).
    private UndoManager undoManager = new UndoManager();
    private UndoListener undoListener = new UndoListener(undoManager);

    public UndoListener getUndoListener() {
        return undoListener;
    }

    public View (){
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      ExceptionHandler.log(e);
    }
  }
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }
    public void init(){
      initGui();
      addWindowListener(new FrameListener(this));
      setVisible(true);
    }
    public void initMenuBar(){
     JMenuBar jMenuBar = new JMenuBar();
      MenuHelper.initFileMenu(this, jMenuBar);
      MenuHelper.initEditMenu(this, jMenuBar);
      MenuHelper.initStyleMenu(this, jMenuBar);
      MenuHelper.initAlignMenu(this, jMenuBar);
      MenuHelper.initColorMenu(this, jMenuBar);
      MenuHelper.initFontMenu(this, jMenuBar);
      MenuHelper.initHelpMenu(this, jMenuBar);

      getContentPane().add(jMenuBar, BorderLayout.NORTH);
    }

    public void initEditor(){
      htmlTextPane.setContentType("text/html");
      JScrollPane htmlJScrollPane = new JScrollPane(htmlTextPane);
      tabbedPane.addTab("HTML",htmlJScrollPane);
      JScrollPane planeJScrollPane = new JScrollPane(plainTextPane);
      tabbedPane.addTab("Текст",planeJScrollPane);
      tabbedPane.setPreferredSize(new Dimension(300,300));
      tabbedPane.addChangeListener(new TabbedPaneChangeListener(this));
      getContentPane().add(tabbedPane,BorderLayout.CENTER);
    }

    public void initGui(){
      initMenuBar();
      initEditor();
      pack();
    }
    public void exit(){
        controller.exit();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
       String action= actionEvent.getActionCommand();
       switch (action){
           case "Новый" :
               controller.createNewDocument();
               break;
           case "Открыть" :
               controller.openDocument();
               break;
           case "Сохранить" :
               controller.saveDocument();
               break;
           case "Сохранить как..." :
               controller.saveDocumentAs();
               break;
           case "Выход" :
               controller.exit();
               break;
           case "О программе" :
               this.showAbout();
               break;
       }
    }
    public void selectedTabChanged(){
        switch (tabbedPane.getSelectedIndex()) {
            case 0:
                controller.setPlainText(plainTextPane.getText());
                break;
            case 1:
                plainTextPane.setText(controller.getPlainText());
                break;
        }
        resetUndo();
    }

    public boolean canUndo() {
        return undoManager.canUndo();
    }

    public boolean canRedo() {
        return undoManager.canRedo();
    }
    public void undo(){
      try {
          undoManager.undo();
      } catch (Exception e ){
          ExceptionHandler.log(e);
      }
    }
    public void redo(){
      try{
          undoManager.redo();
      } catch (Exception e){
          ExceptionHandler.log(e);
      }
    }
    public void resetUndo(){
        undoManager.discardAllEdits();
    }

    public boolean isHtmlTabSelected() {
        return tabbedPane.getSelectedIndex() == 0;
    }

    public void selectHtmlTab() {
        tabbedPane.setSelectedIndex(0);
        resetUndo();
    }

    public void update() {
        htmlTextPane.setDocument(controller.getDocument());
    }

    public void showAbout() {
        JOptionPane.showMessageDialog(this, "Лучший HTML редактор", "О программе", JOptionPane.INFORMATION_MESSAGE);
    }

}