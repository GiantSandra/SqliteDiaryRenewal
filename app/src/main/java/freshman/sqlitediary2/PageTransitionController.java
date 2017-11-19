package freshman.sqlitediary2;

import android.content.Context;
import android.content.Intent;

/**
 * Created by gin on 2017/11/08.
 */

public class PageTransitionController {

    public static void goToDetailActivity(int row_id, String sentoName, String titleImageFilePath, Context context){
        Intent intent = new Intent(context, DiaryDetail.class);
        intent.putExtra("row_id", row_id);
        intent.putExtra("sento_name", sentoName);
        intent.putExtra("title_image_path", titleImageFilePath);
        context.startActivity(intent);
    }
}
