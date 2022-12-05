package com.websarva.wings.android.schedulemission;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import static com.websarva.wings.android.schedulemission.DBContract.DBEntry;
import java.util.Locale;

public class TextActivity extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private int id = 0;
    private EditText editTitle = null;
    private EditText editContents = null;

    //date
    private TextView text_date = null;
    //time
    private TextView text_time = null;

    //日付、時間入れる
    String[] s={null, null};// 0:日付 1:時間

    int[] i = {-1, -1,};// 0:日付 1:時間


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        // ビューオブジェクト取得
        editTitle = findViewById(R.id.editTitle);
        editContents = findViewById(R.id.editContents);
        text_time = findViewById(R.id.text_time);
        text_date = findViewById(R.id.text_date);
    }

    // 「登録」ボタン　タップ時に呼び出されるメソッド
    public void btnReg_onClick(View view) {

        // ヘルパーを準備
        SampDatabaseHelper helper = new SampDatabaseHelper(this);

        // 入力欄に入力されたデータをを取得
        String title    = editTitle.getText().toString();
            String contents = editContents.getText().toString();


        // 書き込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getWritableDatabase()) {

            // 入力されたタイトルとコンテンツをContentValuesに設定
            // ContentValuesは、項目名と値をセットで保存できるオブジェクト
            ContentValues cv = new ContentValues();

            //データ取得
            cv.put(DBEntry.COLUMN_NAME_TITLE, title);
            cv.put(DBEntry.COLUMN_NAME_CONTENTS, contents);
            cv.put(DBEntry.COLUMN_NAME_DATE, s[0]);
            cv.put(DBEntry.COLUMN_NAME_TIME, s[1]);

            // データ新規登録
            db.insert(DBEntry.TABLE_NAME , null, cv);

        }

        // 終了
        finish();

    }

    // 「キャンセル」ボタン　タップ時に呼び出される
    public void btnCancel_onClick(View view) {

        // TextActivityを終了
        finish();
    }


    //年月日表示
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)   {

        //データ表示
        String str_date = String.format(Locale.US, "%04d/%02d/%02d",year, monthOfYear+1, dayOfMonth);
        text_date.setText( str_date  );

        //データ取得
        s[0] = str_date;

        //日付、時間入力したらデータを保存できる
        i[0] = 0;
        Button btnSave = findViewById(R.id.btn_reg);
        if(  i[0] != -1 && i[1] != -1 ) {
            btnSave.setEnabled(true);
        }
    }

    //年月日入力
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePick();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    //時間表示
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String str_ti = String.format(Locale.US, "%02d:%02d", hourOfDay, minute);
        text_time.setText( str_ti );

        //データ取得
        s[1] = str_ti;

        //日付、時間入力したらデータを保存できる
        i[1] = 0;
        Button btnSave = findViewById(R.id.btn_reg);
        if(  i[0] != -1 && i[1] != -1 ) {
            btnSave.setEnabled(true);
        }
    }

    //時間入力
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePick();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

}