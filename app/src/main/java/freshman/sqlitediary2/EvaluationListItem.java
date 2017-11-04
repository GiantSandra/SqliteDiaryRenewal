package freshman.sqlitediary2;

/**
 * Created by gin on 2017/03/14.
 */

public class EvaluationListItem {

    private String title = null;
    private float num_star;
    private float num;

    public EvaluationListItem(){}

    public EvaluationListItem(String title, float num_star, float num)
    {
        this.title = title;
        this.num_star = num_star;
        this.num = num;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setNumStar(int num_star)
    {
        this.num_star = num_star;
    }

    public void setNum(int num)
    {
        this.num = num;
    }

    public String getTitle()
    {
        return this.title;
    }

    public float getNumStar()
    {
        return this.num_star;
    }

    public float getNum()
    {
        return this.num;
    }


}
