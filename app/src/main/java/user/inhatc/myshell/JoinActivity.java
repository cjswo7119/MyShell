package user.inhatc.myshell;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

    Calendar cal = Calendar.getInstance();  //현재시간 가져오기
    int year=cal.get(cal.YEAR);           //현재시간의 년도 가져오기

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
    }

    @Override
    public void onClick(View v) {
        if(userID.getText().toString().length() < 2) {
            joinError.setText("아이디는 2글자 이상 15자 미만입니다.");
            if(isLower(userID.getText().toString()) || isNumber(userID.getText().toString())) { //소문자 또는 숫자로만 이루어진 경우
                joinError.setText("아이디는 소문자와 숫자 조합입니다.");
                return;
            }
            return;
        }
        if(userPassword.getText().toString().length() < 8) {
            joinError.setText("비밀번호는 8자 이상 20자 미만입니다.");
            //if(소문자, 대문자, 숫자, 특수문자 포함)
            return;
        }
        if(userPassword.getText().equals(userPWCheck.getText())) {
            joinError.setText("입력하신 비밀번호와 일치하지 않습니다.");
            return;
        }
        if(userName.getText().toString().length() < 5) {
            joinError.setText("성명은 5자 이상입니다.");
            if(!isKorea(userName.getText().toString())) {  //한글아닌 다른 문자가 포함된 경우
                joinError.setText("성명은 한글로만 작성가능합니다.");
                return;
            }
            return;
        }
        if(userNickName.getText().toString().length() < 5){
            joinError.setText("닉네임은 5자 이상입니다.");
            return;
        }
        Date today = new Date(); //오늘 날짜
        String date=birthYear.getSelectedItem().toString()+ String.format("%02d",birthMonth.getSelectedItem())+String.format("%02d",birthDay.getSelectedItem());
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
        joinError.setText("성공");
    }
    public static boolean checkEmail(String email){
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;
    }
}
