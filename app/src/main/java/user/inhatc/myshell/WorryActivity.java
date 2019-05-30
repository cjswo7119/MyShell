package user.inhatc.myshell;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class WorryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worry);

        if (getIntent() != null) {
            int WorryNo = getIntent().getIntExtra("WorryNo", 0);
            String WorryTitle = getIntent().getStringExtra("WorryTitle");
            String WorryContent = getIntent().getStringExtra("WorryContent");
            String WorryWriter = getIntent().getStringExtra("WorryWriter");
            String WorryDate = getIntent().getStringExtra("WorryDate");

            Toast.makeText(this, Integer.toString(WorryNo) + "/" + WorryTitle + '/' + WorryContent + '/' + WorryWriter + '/' + WorryDate, Toast.LENGTH_LONG).show();
        }
    }
}
