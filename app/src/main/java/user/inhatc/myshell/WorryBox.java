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
    String[] dbData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worry_box);

        Button btn_toMain = (Button)findViewById(R.id.btn_boxCancel);
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
        dbData = new String[allRCD.getCount()];
        if (allRCD != null) {
            if(allRCD.moveToFirst()) {
                int i=0;
                do {
                    String strRecord = allRCD.getString(5) + "에 작성된 고민...";
                    aryMBRList.add(strRecord);
                    dbData[i] = allRCD.getString(0) + " " + allRCD.getString(1) + " " + allRCD.getString(2) + " " +
                            allRCD.getString(3) + " " + allRCD.getString(4) + " " + allRCD.getString(5) + " " +
                            allRCD.getString(6) + " " + allRCD.getString(7) + " " + allRCD.getString(8) + " " +
                            allRCD.getString(9) + " " + allRCD.getString(10);
                    i++;
                } while(allRCD.moveToNext());
            }
        }
        adtMembers = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, aryMBRList);

        lstView = (ListView)findViewById(R.id.worrylstView);
        lstView.setAdapter(adtMembers);
        lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent wbContentIntent = new Intent(WorryBox.this, WorryBoxContent.class);
                wbContentIntent.putExtra("record", dbData[pos]);
                startActivity(wbContentIntent);
            }
        });
    }
}
