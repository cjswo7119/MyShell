package user.inhatc.myshell;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static boolean isLower(String word) {    //소문자 포함 검사
        return Pattern.matches("(^[a-z]*$)", word);
    }
    public static boolean isUpper(String word) {    //대문자 포함 검사
        return Pattern.matches("(^[A-Z]*$)", word);
    }
    public static boolean isNumber(String word) {   //숫자 포함 검사
        return Pattern.matches("(^[0-9]*$)", word);
    }
    public static boolean isSymbol(String word) {   //특수문자 포함 검사
        return Pattern.matches("(^[~`!@#$%^&*()_+-={}:;<>,.?/|\"\'\\\\\\[\\]|]*$)", word);  //\는 앞에\\\, [,]는 \\
    }

    SQLiteDatabase myDB;
    private TextView userID;
    private EditText userPassword;
    private EditText userPWCheck;
    private TextView userName;
    private EditText userNickName;
    private TextView userBirth;
    private EditText userEmail;
    private TextView userError;
    private Button UpdateBack;
    private Button UdateUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile);

        userID = (TextView) findViewById(R.id.userID);
        userPassword = (EditText)findViewById(R.id.userPW);
        userPWCheck = (EditText)findViewById(R.id.userPWCheck);
        userName = (TextView) findViewById(R.id.userName);
        userNickName = (EditText)findViewById(R.id.userNickname);
        userBirth = (TextView)findViewById(R.id.userBirth);
        userEmail = (EditText)findViewById(R.id.userEmail);
        userError = (TextView) findViewById(R.id.userError);
        UdateUser = (Button)findViewById(R.id.btnUpdate);
        UdateUser.setOnClickListener(this);
        UpdateBack = (Button)findViewById(R.id.btnCancle);
        UpdateBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //사용자 조회
        myDB = this.openOrCreateDatabase("MagicShell", MODE_PRIVATE, null);

        String sql = "Select Id, Name, Nickname, Birth, Email from User where Id = '"+getIntent().getStringExtra("ID")+"';";
        Cursor cursor = myDB.rawQuery(sql,null);
        cursor.moveToFirst();
        userID.setText(cursor.getString(0));
        userName.setText(cursor.getString(1));
        userNickName.setText(cursor.getString(2));
        userBirth.setText(cursor.getString(3));
        userEmail.setText(cursor.getString(4));
        cursor.close();

        if(myDB != null) myDB.close();
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        super.onCreateDialog(id);
        AlertDialog dlgAlert;

        dlgAlert = new AlertDialog.Builder(this)
                .setTitle("정보수정")
                .setMessage("정보수정을 진행하시겠습니까?")
                .setView(null)
                //확인 버튼
                .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        myDB = ProfileActivity.this.openOrCreateDatabase("MagicShell", MODE_PRIVATE, null);

                        //사용자 정보 변경
                        String sql = "Update User set Password='"+userPassword.getText().toString()+"', Nickname='"
                                +userNickName.getText().toString()+"', Email='"+userEmail.getText().toString()+"' where Id='"+userID.getText().toString()+"';";

                        myDB.execSQL(sql);

                        Toast.makeText(ProfileActivity.this, "수정 완료",Toast.LENGTH_LONG).show();
                        if(myDB != null) myDB.close();
                        dialog.dismiss();
                        onBackPressed();
                    }
                })
                //취소 버튼
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog, int id) {
                        // 다이얼로그를 취소한다
                        dialog.cancel();
                    }
                })
                .create();
        return dlgAlert;
    }
    @Override
    public void onClick(View v) {

        //비밀번호 검사
        int pwCheck=0, pwLow=0, pwNum=0, pwUpp=0, pwSym=0;
        String[] userpw = userPassword.getText().toString().split("");
        if(userPassword.getText().toString().length() < 8) {
            userError.setText("비밀번호는 8자 이상 20자 미만입니다.");
            //if(소문자, 대문자, 숫자, 특수문자 포함)
            return;
        }else {
            for(int i=0; i<userPassword.getText().toString().length(); i++){
                if(isLower(userPassword.getText().toString())) { //소문자 또는 숫자로만 이루어진 경우
                    userError.setText("비밀번호는 소문자와 숫자가 포함되어야 합니다.");
                    return;
                }
                if(isNumber(userPassword.getText().toString())) { //소문자 또는 숫자로만 이루어진 경우
                    userError.setText("비밀번호는 소문자와 숫자가 포함되어야 합니다.");
                    return;
                }
            }
        }
        //비밀번호 입력 검사
        if(!userPassword.getText().toString().equals(userPWCheck.getText().toString())) {
            userError.setText("입력하신 비밀번호와 일치하지 않습니다.");
            return;
        }

        //닉네임 검사
        if(userNickName.getText().toString().length() < 5){
            userError.setText("닉네임은 5자 이상입니다.");
            return;
        }

        //이메일 검사
        if(!checkEmail(userEmail.getText().toString())){
            userError.setText("유효하지 않은 이메일입니다.");
            return;
        }

        showDialog(0); //회원가입 진행 다이얼로그 뛰우기


    }
    public static boolean checkEmail(String email){
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;
    }
}
