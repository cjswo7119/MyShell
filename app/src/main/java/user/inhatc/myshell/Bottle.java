package user.inhatc.myshell;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

public class Bottle extends AppCompatImageView {

    public Bottle(Context context) {
        super(context);
    }

    private int AnswerNo;
    private int WorryNo;
    private String Content;
    private String Date;
    private String WriterId;
    private String WriterNick;

    public int number;

    public int getAnswerNo() {
        return AnswerNo;
    }

    public int getWorryNo() {
        return WorryNo;
    }

    public String getContent() {
        return Content;
    }

    public String getDate() {
        return Date;
    }

    public String getWriterId() { return WriterId; }

    public String getWriterNick() {
        return WriterNick;
    }

    public void setAnswerNo(int answerNo) { AnswerNo = answerNo; }

    public void setWorryNo(int worryNo) { WorryNo = worryNo; }

    public void setContent(String content) { Content = content; }

    public void setDate(String date) { Date = date; }

    public void setWriterId(String writerId) { WriterId = writerId; }

    public void setWriterNick(String writerNick) { WriterNick = writerNick; }
}
