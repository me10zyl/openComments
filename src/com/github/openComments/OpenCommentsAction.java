package com.github.openComments;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;

/**
 * Created by ZyL on 2016/11/9.
 */
public class OpenCommentsAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getData(CommonDataKeys.PROJECT);
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        final Document document = editor.getDocument();
        final SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();
        CaretModel caretModel = editor.getCaretModel();
        int selectionEnd = selectionModel.getSelectionEnd();
        int currentLine = document.getLineNumber(selectionEnd);
        int positionToInsert = document.getLineEndOffset(currentLine);
        WriteCommandAction.runWriteCommandAction(project, new Runnable(){

            @Override
            public void run() {
                String sql = String.format("\nselect * from USER_COL_COMMENTS where table_name = '%s';", selectedText);
                document.insertString(positionToInsert, sql);
                selectionModel.setSelection(positionToInsert + 1, positionToInsert + sql.length());
                caretModel.moveToOffset(positionToInsert + sql.length());
            }
        });
    }

    @Override
    public void update(AnActionEvent e) {
        final Project project = e.getData(CommonDataKeys.PROJECT);
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        e.getPresentation().setVisible((project != null && editor != null
                && editor.getSelectionModel().hasSelection()));
    }
}
