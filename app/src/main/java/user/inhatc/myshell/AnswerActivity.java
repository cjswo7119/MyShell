package user.inhatc.myshell;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AnswerActivity extends AppCompatActivity {

    int AnswerNo;
    int WorryNo;
    String AnswerContent;
    String AnswerDate;
    String AnswerWriterId;
    String AnswerWriterNick;
    String WriterId;
    String WriterNick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        if (getIntent() != null) {
            AnswerNo = getIntent().getIntExtra("AnswerNo", 0);
            WorryNo = getIntent().getIntExtra("WorryNo", 0);
            AnswerContent = getIntent().getStringExtra("AnswerContent");
            AnswerDate = getIntent().getStringExtra("AnswerDate");
            AnswerWriterId = getIntent().getStringExtra("AnswerWriterId");
            AnswerWriterNick = getIntent().getStringExtra("AnswerWriterNick");
            WriterId = getIntent().getStringExtra("ID");
            WriterNick = getIntent().getStringExtra("NICK");

            TextView answerContent = (TextView)findViewById(R.id.answerContent);
            String content = AnswerDate + "에 작성한 답변...\n\n";
            content += AnswerContent + "\n\n";
            content += "친애하는 " + AnswerWriterNick + "이(가)...";
            answerContent.append(content);
        }

        Button btn_answerReadCancel    = (Button)findViewById(R.id.btn_answerReadCancel);
        Button btn_answerReadComplete  = (Button)findViewById(R.id.btn_answerReadComplete);
        btn_answerReadCancel.setOnClickListener(listener_answerReadCancel);
        btn_answerReadComplete.setOnClickListener(listener_answerReadComplete);
    }

    View.OnClickListener listener_answerReadCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setResult(RESULT_CANCELED);
            finish();
        }
    };

    View.OnClickListener listener_answerReadComplete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
