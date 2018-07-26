package cn.translate;

import cn.translate.http.HttpUtils;
import cn.translate.http.ICallBack;
import cn.translate.http.bean.TranslateBean;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;
import org.apache.http.util.TextUtils;

import java.awt.*;
import java.util.List;

public class BDTranslateAction extends AnAction {

    public BDTranslateAction() {
        super("translate");
        HttpUtils.initOkHttp();
    }

    public void actionPerformed(AnActionEvent anActionEvent) {
        final Editor mEditor = (Editor) anActionEvent.getData(PlatformDataKeys.EDITOR);
        if (null == mEditor) {
            return;
        }
        SelectionModel model = mEditor.getSelectionModel();
        final String selectedText = model.getSelectedText();
        if (TextUtils.isEmpty(selectedText)) {
            return;
        }
        doTranslate(mEditor, selectedText);
    }


    private void doTranslate(final Editor mEditor, String selectedText) {
        HttpUtils.doGetAsyn(selectedText, new ICallBack() {
            @Override
            public void onRequestSuccess(List<TranslateBean> result) {
                if (result == null || result.size() == 0) {
                    return;
                }

                StringBuilder builder = new StringBuilder(result.get(0).getSrc());
                builder.append(":\n");
                builder.append("---------------\n");
                for (TranslateBean item :result) {
                    builder.append(item.getDst())
                            .append("\n");
                }
                showPopupBalloon(mEditor, builder.toString());
            }
        });
    }

    private void showPopupBalloon(final Editor editor, final String result) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                JBPopupFactory factory = JBPopupFactory.getInstance();
                factory.createHtmlTextBalloonBuilder(result, null, new JBColor(new Color(186, 238, 186), new Color(73, 117, 73)), null)
                        .setFadeoutTime(5000)
                        .createBalloon()
                        .show(factory.guessBestPopupLocation(editor), Balloon.Position.below);
            }
        });
    }
}
