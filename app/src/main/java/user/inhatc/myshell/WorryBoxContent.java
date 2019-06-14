package user.inhatc.myshell;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WorryBoxContent extends AppCompatActivity {

    String dbRecord;
    String[] datas;
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

        if (getIntent() != null) {
            dbRecord = getIntent().getStringExtra("record");
            Log.i("MagicShell", dbRecord);
            datas = dbRecord.split("|");
            Log.i("MagicShell", datas[0] + " " + datas[1] + " " + datas[2] + " " + datas[31] + " " + datas[4] + " " + datas[5]);

            worryWriterNick = datas[3];
            worryContent = datas[4];
            worryDate = datas[5];
            answerWriterNick = datas[8];
            answerContent = datas[9];
            answerDate = datas[10];
        }

        TextView writerInfo = (TextView)findViewById(R.id.wContent);
        TextView answerInfo = (TextView)findViewById(R.id.aContent);
        Button btnwbcCancel = (Button)findViewById(R.id.btn_worryBoxContentCancel);
        btnwbcCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String temp = worryDate + "에 작성한 고민...\n\n" + worryContent + "\n\n간절한 답장을 바라는 " + worryWriterNick + "가...";
        writerInfo.setText(temp);
        temp = worryDate + "에 작성한 답변...\n\n" + answerContent + "\n\n친애하는 " + answerWriterNick + "이(가)...";
        answerInfo.setText(temp);
    }
}
