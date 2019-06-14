package user.inhatc.myshell;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WorryBoxContent extends AppCompatActivity {

    String worryWriterNick;
    String answerWriterNick;
    String worryContent;
    String answerContent;
    String worryDate;
    String answerDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worry_box_content);
        try {
            if (getIntent() != null) {

                worryWriterNick = getIntent().getStringExtra("WorryWriterNick");
                worryContent = getIntent().getStringExtra("WorryContent");
                worryDate = getIntent().getStringExtra("WorryDate");
                answerWriterNick = getIntent().getStringExtra("AnswerWriterNick");
                answerContent = getIntent().getStringExtra("AnswerContent");
                answerDate = getIntent().getStringExtra("AnswerDate");
            }

            TextView writerInfo = (TextView) findViewById(R.id.wContent);
            TextView answerInfo = (TextView) findViewById(R.id.aContent);
            Button btnwbcCancel = (Button) findViewById(R.id.btn_worryBoxContentCancel);
            btnwbcCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

            String temp = worryDate + "에 작성한 고민...\n\n" + worryContent + "\n\n간절한 답장을 바라는 " + worryWriterNick + "가...";
            writerInfo.setText(temp);

            if (answerDate == null || answerContent == null || answerWriterNick == null)
                temp = "아직 답장이 도착하지 않았습니다.";
            else
                temp = answerDate + "에 작성한 답변...\n\n" + answerContent + "\n\n친애하는 " + answerWriterNick + "이(가)...";
            answerInfo.setText(temp);
        } catch (Exception e) {
            Log.i("MagicShell", e.getMessage());
        }
    }
}
