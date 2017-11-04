package freshman.sqlitediary2;

/**
 * Created by gin on 2017/03/15.
 */

public class SettingEvaluationListItem {

    private String title = null;
    private float num_star;

    public SettingEvaluationListItem(){}

    public SettingEvaluationListItem(String title, float num_star)
    {
        this.title = title;
        this.num_star = num_star;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getNum_star() {
        return num_star;
    }

    public void setNum_star(float num_star) {
        this.num_star = num_star;
    }
}
