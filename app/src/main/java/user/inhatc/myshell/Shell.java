package user.inhatc.myshell;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Toast;

public class Shell extends AppCompatImageView {

    private int WorryNo;
    private String Writer;
    private String Title;
    private String Content;
    private String Date;

    private View.OnClickListener listener;

    public Shell(Context context) {
        super(context);
    }

    public int getWorryNo() {
        return WorryNo;
    }

    public String getWriter() {
        return Writer;
    }

    public String getTitle() {
        return Title;
    }

    public String getContent() {
        return Content;
    }

    public String getDate() {
        return Date;
    }

    public void setWorryNo(int worryNo) {
        WorryNo = worryNo;
    }

    public void setWriter(String writer) {
        Writer = writer;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setContent(String content) {
        Content = content;
    }

    public void setDate(String date) {
        Date = date;
    }
}
