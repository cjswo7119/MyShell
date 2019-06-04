package user.inhatc.myshell;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;

public class Shell extends AppCompatImageView {

    private int WorryNo;
    private String WriterNick;
    private String WriterId;
    private String Content;
    private String Date;

    public int number;

    public Shell(Context context) {
        super(context);
    }

    public int getWorryNo() {
        return WorryNo;
    }

    public String getWriterId() { return WriterId; }

    public String getWriterNick() {
        return WriterNick;
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

    public void setWriterId(String writerid) { WriterId = writerid; }

    public void setWriterNick(String writernick) { WriterNick = writernick; }

    public void setContent(String content) { Content = content; }

    public void setDate(String date) { Date = date; }
}
