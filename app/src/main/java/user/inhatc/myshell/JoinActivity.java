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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity implements View.OnClickListener {

    public static boolean isKorea(String word) {    //한글 포함 검사
        return Pattern.matches("(^[가-힣]*$)", word);
    }
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
    private EditText userID;
    private EditText userPassword;
    private EditText userPWCheck;
    private EditText userName;
    private EditText userNickName;
    private Spinner birthYear;
    private Spinner birthMonth;
    private Spinner birthDay;
    private EditText userEmail;
    private TextView joinError;
    private Button joinBack;
    private Button joinUser;

    String date = "";  //데이터베이스에 저장될 사용자 생년월일
    Calendar cal = Calendar.getInstance();    //현재시간 가져오기
    int year = cal.get(cal.YEAR);           //현재시간의 년도 가져오기
    int month = cal.get(cal.MONTH)+1;       //현재시간의 월 가져오기
    int day = cal.get(cal.DATE);            //현재시간의 일 가져오기

    String current = Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(day);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_join);

        //스피너에 년 표시.현재부터 -120년 전까지
        ArrayList arrYear = new ArrayList<>();
        for(int num=year; num>year-120; num--){
            arrYear.add(num);
        }
        birthYear = (Spinner)findViewById(R.id.spnYear);
        ArrayAdapter AdtYear = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrYear);
        birthYear.setAdapter(AdtYear);
        //스피너에 월 표시.
        ArrayList arrMonth = new ArrayList<>();
        for(int num=1; num<=12; num++){
            arrMonth.add(num);
        }
        birthMonth = (Spinner)findViewById(R.id.spnMonth);
        ArrayAdapter AdtMonth = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrMonth);
        birthMonth.setAdapter(AdtMonth);
        //스피너에 일 표시.
        ArrayList arrDay = new ArrayList<>();
        for(int num=1; num<=31; num++){
            arrDay.add(num);
        }
        birthDay = (Spinner)findViewById(R.id.spnDay);
        ArrayAdapter AdtDay = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrDay);
        birthDay.setAdapter(AdtDay);

        userID = (EditText)findViewById(R.id.txtID);
        userPassword = (EditText)findViewById(R.id.txtPW);
        userPWCheck = (EditText)findViewById(R.id.txtPWCheck);
        userName = (EditText)findViewById(R.id.txtName);
        userNickName = (EditText)findViewById(R.id.txtNickname);
        userEmail = (EditText)findViewById(R.id.txtEmail);
        joinError = (TextView) findViewById(R.id.lblError);
        joinUser = (Button)findViewById(R.id.btnJoin);
        joinUser.setOnClickListener(this);
        joinBack = (Button)findViewById(R.id.btnBack);
        joinBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
                .setTitle("회원가입")
                .setMessage("회원가입을 진행하시겠습니까?")
                .setView(null)
                //확인 버튼
                .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        myDB = JoinActivity.this.openOrCreateDatabase("MagicShell", MODE_PRIVATE, null);

                        //사용자 추가
                        String sql = "";
                        sql="Insert into User " +
                                "values ('"+userID.getText().toString()+"', '"+userPassword.getText().toString()+"', '"+userName.getText().toString()+"', '"
                                +userNickName.getText().toString()+"', "+Integer.parseInt(date)+", '"+userEmail.getText().toString()+"',0,"+"'"+current+"');";
                        myDB.execSQL(sql);

                        Toast.makeText(JoinActivity.this, sql,Toast.LENGTH_LONG).show();
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
        if(userID.getText().toString().length() < 2) {
            joinError.setText("아이디는 2글자 이상 15자 미만입니다.");
            return;
        }else {
            if(isLower(userID.getText().toString())) { //소문자 또는 숫자로만 이루어진 경우
                joinError.setText("아이디는 소문자와 숫자 조합입니다.");
                return;
            }
            if(isNumber(userID.getText().toString())) { //소문자 또는 숫자로만 이루어진 경우
                joinError.setText("아이디는 소문자와 숫자 조합입니다.");
                return;
            }
        }
        String security;
        int pwCheck=0, pwLow=0, pwNum=0, pwUpp=0, pwSym=0;
        String[] userpw = userPassword.getText().toString().split("");
        if(userPassword.getText().toString().length() < 8) {
            joinError.setText("비밀번호는 8자 이상 20자 미만입니다.");
            //if(소문자, 대문자, 숫자, 특수문자 포함)
            return;
        }else {
            for(int i=0; i<userPassword.getText().toString().length(); i++){
                if(isLower(userpw[i])){         //소무자가 포함된 경우
                    pwLow=8;
                }else if(isNumber(userpw[i])){  //숫자가 포한된 경우
                    pwNum=4;
                }else if(isUpper(userpw[i])){   //대문자가 포함된 경우
                    pwUpp=2;
                }else if(isSymbol(userpw[i])){  //특수문자가 포함된 경우
                    pwSym=1;
                }
                pwCheck=pwLow+pwNum+pwUpp+pwSym; //보안등급 합산
                if(isLower(userPassword.getText().toString())) { //소문자 또는 숫자로만 이루어진 경우
                    joinError.setText("비밀번호는 소문자와 숫자가 포함되어야 합니다.");
                    return;
                }
                if(isNumber(userPassword.getText().toString())) { //소문자 또는 숫자로만 이루어진 경우
                    joinError.setText("비밀번호는 소문자와 숫자가 포함되어야 합니다.");
                    return;
                }
            }
        }
        if(!userPassword.getText().toString().equals(userPWCheck.getText().toString())) {
            joinError.setText("입력하신 비밀번호와 일치하지 않습니다.");
            return;
        }
        if(userName.getText().toString().length() < 2) {
            joinError.setText("성명은 2자 이상입니다.");
            return;
        }
        if(!isKorea(userName.getText().toString())) {  //한글아닌 다른 문자가 포함된 경우
            joinError.setText("성명은 한글만 입력 가능합니다.");
            return;
        }
        if(userNickName.getText().toString().length() < 5){
            joinError.setText("닉네임은 5자 이상입니다.");
            return;
        }
        Date today = new Date(); //오늘 날짜
        date=birthYear.getSelectedItem().toString()+ String.format("%02d",birthMonth.getSelectedItem())+String.format("%02d",birthDay.getSelectedItem());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");  //값을 비교하기 위해 위의 선언한 date의 형식과 같게 맞추기 위해 포맷형식(yyyymmdd)지정.
        String strToday = sdf.format(today).toString(); //현재날짜정보(cld)를 위에서 지정한 포맷형식(yyyymmdd)으로 문자열 strToday생성.

        if(Integer.parseInt(date)>Integer.parseInt(strToday)) {  //현재 날짜보다 미래인 경우 에러메세지 출력.
                joinError.setText("미래에서 오셨군요^^");
            return;
        }
        if(!checkEmail(userEmail.getText().toString())){
            joinError.setText("유효하지 않은 이메일입니다.");
            return;
        }
        myDB = this.openOrCreateDatabase("MagicShell", MODE_PRIVATE, null);

        //중복아이디 검사
        String sql = "Select Id from User where Id = '"+userID.getText().toString()+"'";
        Cursor cursor = myDB.rawQuery(sql,null);
        if(!cursor.moveToFirst());  //가져온 레코드가 없으면 null 즉 아이디 중복이 아님.
        else {
            joinError.setText("아이디가 중복입니다.");
            return;
        }

        showDialog(0); //회원가입 진행 다이얼로그 뛰우기

        if(myDB != null) myDB.close();
    }
    public static boolean checkEmail(String email){
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;
    }
}
