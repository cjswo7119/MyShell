package user.inhatc.myshell;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;

public class WorryBox extends AppCompatActivity {

    SimpleAdapter worryADT;
    ArrayList<String> aryMBRList;
    ArrayAdapter<String> adtMembers;
    ListView lstView;
    SQLiteDatabase myDB;
    String[][] dbData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worry_box);
        try {
            Button btn_toMain = (Button) findViewById(R.id.btn_boxCancel);
            btn_toMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            myDB = this.openOrCreateDatabase("MagicShell", MODE_PRIVATE, null);
            String[] args = {getIntent().getStringExtra("ID"), getIntent().getStringExtra("ID")};
            Cursor allRCD = myDB.query("WorryBox", null, "worryWriterId=? or answerWriterId=?", args, null, null, null, null);

            aryMBRList = new ArrayList<String>();
            dbData = new String[allRCD.getCount()][11];
            if (allRCD != null) {
                if (allRCD.moveToFirst()) {
                    int i = 0;
                    do {
                        String strRecord = allRCD.getString(5) + "에 작성된 고민...";
                        aryMBRList.add(strRecord);
                        for (int j = 0; j < 11; j++) {
                            dbData[i][j] = allRCD.getString(j);
                        }
                        i++;
                    } while (allRCD.moveToNext());
                }
            }
            adtMembers = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, aryMBRList);

            lstView = (ListView) findViewById(R.id.worrylstView);
            lstView.setAdapter(adtMembers);
            lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    Intent wbContentIntent = new Intent(WorryBox.this, WorryBoxContent.class);
                    wbContentIntent.putExtra("WorryWriterNick", dbData[pos][3]);
                    wbContentIntent.putExtra("WorryContent", dbData[pos][4]);
                    wbContentIntent.putExtra("WorryDate", dbData[pos][5]);
                    wbContentIntent.putExtra("AnswerWriterNick", dbData[pos][8]);
                    wbContentIntent.putExtra("AnswerContent", dbData[pos][9]);
                    wbContentIntent.putExtra("AnswerDate", dbData[pos][10]);
                    startActivity(wbContentIntent);
                }
            });
        } catch (Exception e) {
            Log.i("MagicShell", e.getMessage());
        }
    }
}
