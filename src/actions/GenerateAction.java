package actions;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.xml.XmlAttributeImpl;
import com.intellij.psi.impl.source.xml.XmlDocumentImpl;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.impl.source.xml.XmlTextImpl;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import dialog.SingleDimenAllConfigDialog;
import model.Dimen;
import model.TmpBean;
import util.Constants;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GenerateAction extends AbstractDimenAction {
    String attributeName;


    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        super.actionPerformed(e);
        project = e.getData(PlatformDataKeys.PROJECT);
        psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = PlatformDataKeys.EDITOR.getData(e.getDataContext());
        if (psiFile == null || editor == null) {
            e.getPresentation().setEnabled(false);
            return;
        }
        int offset = editor.getCaretModel().getOffset();
        PsiElement psiElement = psiFile.findElementAt(offset);
        //        1.拿到dp值
        if (psiFile.getFileType() == StdFileTypes.XML) {
            xmlFile = (XmlFile) psiFile;
            currentBucketIndex = getBucketIndex(psiFile);
            if (currentBucketIndex == -1) {
                String directoryName = psiFile.getParent().getName();
                if (directoryName.startsWith(Constants.VALUES_PREFIX)) {
                    //nxhdpi
                    String bucket = directoryName.substring(Constants.VALUES_PREFIX.length());
                    String val = JOptionPane.showInputDialog(null, Constants.SCALE_TEXT_PREFIX + bucket, Constants.NEW_BUCKET, JOptionPane.INFORMATION_MESSAGE);
                    try {
                        float value = Float.parseFloat(val);
                        Dimen dimen = new Dimen().setBucket(bucket).setFactorDp(value).setFactorSp(value).setMandatory(false).setSelected(true);
                        data.add(dimen);
                        saveValues(data);
                        currentBucketIndex = data.size() - 1;
                    } catch (NullPointerException ex) {
                        return;
                    } catch (NumberFormatException ex) {
                        showAlert(3);
                        return;
                    }
                } else {
                    showAlert(2);
                    return;
                }
            }
//        2.生成dialog
            initialize(psiElement);
//        3.填充dialog
//        4.点击生成dimen文件
        } else {
            e.getPresentation().setEnabled(false);
        }


    }

    private void initialize(PsiElement psiElement) {
        PsiElement selectedNode = psiElement;
        PsiElement rootParent;
        PsiElement subNode;
        if (psiElement != null && psiElement.getParent() != null && psiElement.getParent().getParent() != null) {
            subNode = psiElement.getParent();
            rootParent = subNode.getParent();

            while (!(rootParent instanceof XmlDocumentImpl) && (!(selectedNode instanceof XmlTagImpl) || !((XmlTagImpl) selectedNode).getName().equals(Constants.DIMEN_TAG))) {
                rootParent = rootParent.getParent();
                subNode = subNode.getParent();
                selectedNode = selectedNode.getParent();
            }

            if (subNode instanceof XmlTagImpl && selectedNode instanceof XmlTagImpl) {
                XmlTagImpl currentNode = (XmlTagImpl) selectedNode;
                if (((XmlTagImpl) subNode).getName().equals(Constants.RESOURCES_TAG) && currentNode.getName().equals(Constants.DIMEN_TAG)) {
                    XmlAttributeImpl attribute = null;
                    XmlTextImpl value = null;
                    for (PsiElement element : currentNode.getChildren()) {
                        if (element instanceof XmlAttributeImpl && ((XmlAttributeImpl) element).getLocalName().equals(Constants.NAME_TAG)) {
                            attribute = (XmlAttributeImpl) element;
                        } else if (element instanceof XmlTextImpl) {
                            value = (XmlTextImpl) element;
                        }
                    }

                    if (attribute != null) {
                        attributeName = attribute.getValue();
                        String val = value.getValue().toLowerCase().trim();
                        showScaleDialog(val.substring(val.length() - 2), xmlFile.getOriginalFile().getName());
                    }
                }
            }
        }
    }

    public void showScaleDialog(String suffixType, String fileName) {
        SingleDimenAllConfigDialog singleDimenDialog = new SingleDimenAllConfigDialog(project, suffixType, data);
        singleDimenDialog.show();
        int invalidIndex = singleDimenDialog.invalidBucketIndex();
        if (singleDimenDialog.isOK() && invalidIndex == -1) {
            ArrayList<Dimen> data = singleDimenDialog.getConversionValues();
            saveValues(data);
            String targetFileName = (fileName == null || fileName.length() <= 0) ? Constants.FILE_NAME : fileName;
            createDirectoriesAndFilesIfNeeded(psiFile.getParent().getParent(), targetFileName);

        } else if (singleDimenDialog.isOK()) {
            singleDimenDialog.showAlert(invalidIndex);
        }
    }

    @Override
    protected void calculateAndWriteScaledValueToFiles(String fileName) {
        if (currentBucketIndex >= data.size()) {
            showAlert(4);
            return;
        }

        XmlTag[] dimens = getDimenValuesInFile(xmlFile);
        for (XmlTag tag : dimens) {
            try {
                if (tag.getAttribute("name").getValue().equalsIgnoreCase(attributeName)) {
                    dimens = new XmlTag[]{tag};
                    break;
                }
            } catch (Exception e) {
                return;
            }
        }
        if (dimens.length > 0) {
            HashMap<String, TmpBean> floatDimen = normalizeToHashMap(dimens, currentBucketIndex);
            writeScaledValuesToFiles(psiFile.getParent().getParent(), floatDimen,fileName);
        }
    }


    @Override
    protected String getSaveKey() {
        return PropertiesComponent.getInstance().getValue(Constants.CURRENT_SAVE_KEY, Constants.SAVE_KEY_ALL_CONFIG_1);
    }
}
