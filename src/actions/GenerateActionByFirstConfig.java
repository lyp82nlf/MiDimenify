package actions;

import dialog.SingleDimenDialog;
import model.Dimen;
import util.Constants;

import java.util.ArrayList;

public class GenerateActionByFirstConfig extends GenerateAction {

    @Override
    public void showScaleDialog(String suffixType, String fileName) {
        SingleDimenDialog singleDimenDialog = new SingleDimenDialog(project, suffixType, data,"(First Config)");
        singleDimenDialog.show();
        int invalidIndex = singleDimenDialog.invalidBucketIndex();
        if (singleDimenDialog.isOK() && invalidIndex == -1) {
            ArrayList<Dimen> data = singleDimenDialog.getConversionValues();
            saveValues(data);
            String targetFileName = (fileName == null || fileName.length() <= 0) ? Constants.FILE_NAME : fileName;
            createDirectoriesAndFilesIfNeeded(psiFile.getParent().getParent(),targetFileName);

        } else if (singleDimenDialog.isOK()) {
            singleDimenDialog.showAlert(invalidIndex);
        }
    }

    @Override
    protected String getSaveKey() {
        return Constants.SAVE_KEY_FIRST_CONFIG;
    }
}
