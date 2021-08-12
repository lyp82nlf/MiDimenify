package util;

public interface Constants {
    String SAVE_KEY = "com.mi.dsg";
    String SAVE_KEY_ALL_CONFIG_1 = "com.mi.dsg.all.config1";
    String SAVE_KEY_ALL_CONFIG_2 = "com.mi.dsg.all.config2";
    String SAVE_KEY_FIRST_CONFIG = "com.mi.dsg.first.key";
    String SAVE_KEY_SECOND_CONFIG = "com.mi.dsg.senond.key";
    String CURRENT_SAVE_KEY = "com.mi.dsg.save.key";
    String INIT_MODEL_JSON = "[{\"bucket\": \"dimens\",\"directory\": \"values\",\"factorSp\": 2.75,\"factorDp\": 2.75,\"isSelected\": true,\"isMandatory\": true}, {\"bucket\": \"dimens-nxhdpi\",\"factorSp\": 2.75,\"factorDp\":  2.75,\"directory\": \"values-nxhdpi\",\"isSelected\": true,\"isMandatory\": true}, {\"bucket\": \"dimens-xxhdpi\",\"factorSp\": 3,\"factorDp\": 3,\"directory\": \"values-xxhdpi\",\"isSelected\": true,\"isMandatory\": true}, {\"bucket\": \"dimens-xxxhdpi\",\"factorSp\": 2.75,\"factorDp\": 2.75,\"directory\": \"values-xxxhdpi\",\"isSelected\": true,\"isMandatory\": true}]";
    String VALUES_PREFIX = "values-";
    String SCALE_TEXT_PREFIX = "Please a scale value for ";
    String NEW_BUCKET = "Add new Configuration";
    String DP = "dp";
    String PX = "px";
    String SP = "sp";
    String FILE_NAME = "dimens.xml";

    String MIGRATION_FLAG = "v2Tov3";
    String RESOURCES_TEXT = "<resources>\n</resources>";
    String RESOURCES_TAG = "resources";
    String DIMEN_TAG = "dimen";
    String NAME_TAG = "name";



    String PLACEHOLDER_DIMEN = "    <dimen name=\"{0}\">{1}{2}</dimen>\n";



    String MESSAGES[] = {
            "There are duplicate buckets please fix before adding more."
            , "Custom buckets are restricted to 5"
            , "Could not map the resource folder to a density value"
            , "Could not convert the value into a number"
            , "Could not map xml the file to a density bucket. Please check if the source density bucket was exists."
    };
    String ERROR_TITLE = "Error";
    String TIPS_TITLE = "Tips";
    String TITLE = "Set scale factors for ";
    String METRIC = " metric";
    String DEFAULT_BUCKET = "sw600dp-land";
    float DEFAULT_SCALE_FACTOR = 3f;
    int ERROR_CODE[] = {1, 2};
}
