package site.zhangqun.lanagina;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CalculatorActivity extends AppCompatActivity {

    protected EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_activity_main);
        mEditText = (EditText) findViewById(R.id.calculatorEditText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calculator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        final String input = mEditText.getText().toString();
        final StringBuilder output = new StringBuilder();

        double tempValue;
        try {
            tempValue = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            tempValue = 0;
        }
        final double inputValue = tempValue;

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_calc_person_id:
                output.append(calcShenfenzheng(input));
                showResult(output.toString());
                return true;
            case R.id.action_calc_bank_card:
                output.append(calcLuhn(input, 19));
                showResult(output.toString());
                return true;
            case R.id.action_calc_imei:
                output.append(calcLuhn(input, 15));
                showResult(output.toString());
                return true;
            case R.id.action_calc_luhn:
                final EditText etInput = new EditText(this);
                AlertDialog dlg = new AlertDialog.Builder(this)
                        .setTitle("输入位数")
                        .setView(etInput)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        String bit = etInput.getText().toString();
                                        if (bit.length() > 0 && input.length() > 0) {
                                            try {
                                                output.append(calcLuhn(input, Integer.parseInt(bit)));
                                            } catch (Exception e) {
                                                output.append(e.getMessage());
                                            }
                                        } else {
                                            output.append("输入不正确");
                                        }
                                        showResult(output.toString());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                })
                        .create();
                dlg.show();
                return true;
            case R.id.action_calc_tax:
                output.append(calcTax(inputValue));
                showResult(output.toString());
                return true;
            case R.id.action_calc_get_wage:
                final EditText etInputGetWage = new EditText(this);
                AlertDialog dlgGetWage = new AlertDialog.Builder(this)
                        .setTitle("输入比例")
                        .setView(etInputGetWage)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        String bit = etInputGetWage.getText().toString();
                                        if (bit.length() > 0 && input.length() > 0) {
                                            try {
                                                output.append(calcWageGet(inputValue, Integer.parseInt(bit)));
                                            } catch (Exception e) {
                                                output.append(e.getMessage());
                                            }
                                        } else {
                                            output.append("输入不正确");
                                        }
                                        showResult(output.toString());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                })
                        .create();
                dlgGetWage.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void showResult(String s) {
        TextView tv =new TextView(this);
        tv.setText(s);
        AlertDialog dlg = new AlertDialog.Builder(this)
                .setTitle("计算结果")
                .setView(tv)
                .create();
        dlg.show();

    }

    public String calcShenfenzheng(String s) {
        // StackTraceElement stack[] = Thread.currentThread().getStackTrace();
        // String sx="";
        // for (StackTraceElement ste : stack) {
        // sx+=ste.getClassName()+":"+ste.getMethodName()+">>";
        // }
        // Log.i(General.LogTag,sx);
        int s_len = s.length();
        if (s_len == 0) {
            return "请输入数据";
        }
        String checkBit;
        int mod;
        int result = 0;
        int[] code = new int[20];
        for (int i = 0; i < 20; i += 10) {
            code[i + 0] = 2;
            code[i + 1] = 4;
            code[i + 2] = 8;
            code[i + 3] = 5;
            code[i + 4] = 10;
            code[i + 5] = 9;
            code[i + 6] = 7;
            code[i + 7] = 3;
            code[i + 8] = 6;
            code[i + 9] = 1;
        }
        // Integer.parseInt(tf1.getString(), 10);

        if (s_len == 17) {
            for (int i = 0; i < s_len; i++) {
                result += (s.charAt(s_len - i - 1) - 48) * code[i];
            }
            mod = result % 11;
            if (mod >= 3 && mod <= 10) {
                checkBit = (12 - mod) + "";
            } else if (mod == 2) {
                checkBit = "X";
            } else if (mod == 0) {
                checkBit = "1";
            } else if (mod == 1) {
                checkBit = "0";
            } else {
                checkBit = "";
            }
            return "你的输入为17位，计算得出最后一位应该是：[ " + checkBit + " ]";

            // df.setLabel(String.valueOf(result));
        } else if (s_len == 18) {
            for (int i = 0; i < s_len - 1; i++) {
                result += (s.charAt(s_len - i - 2) - 48) * code[i];
            }
            mod = result % 11;
            if (mod >= 3 && mod <= 10) {
                checkBit = (12 - mod) + "";
            } else if (mod == 2) {
                checkBit = "X";
            } else if (mod == 0) {
                checkBit = "1";
            } else if (mod == 1) {
                checkBit = "0";
            } else {
                checkBit = "";
            }
            if (checkBit.equals(s.charAt(s_len - 1) + "")) {
                return "你的输入身份证号码[ " + s + " ]为正确的身份证号码";
            } else {
                return "你的输入身份证号码[ " + s + " ]为错误的身份证号码";
            }
        } else {
            return "位数不符(你的输入为 " + s_len + " 位)，请确认输入一串17或18位的数字！";
        }
    }

    public String calcLuhn(String s, int bit) {
        int s_len = s.length();
        if (s_len == 0) {
            return "请输入数据";
        }
        String checkBit;
        int mod, t;
        // Integer.parseInt(tf1.getString(), 10);
        int result = 0;

        if (s_len == bit - 1) {
            for (int i = 0; i < s_len; i++) {
                result += (t = (s.charAt(s_len - i - 1) - 48) * 2) > 9 ? t - 9 : t;
                i++;
                {
                    int j = s_len - i - 1;
                    if (j < 0) break;
                    result += (s.charAt(j) - 48);
                }
            }
            mod = result % 10;
            if (mod == 0) {
                checkBit = "0";
            } else if (mod >= 1 && mod <= 9) {
                checkBit = (10 - mod) + "";
            } else {
                checkBit = "";
            }
            return "你的输入为 " + (bit - 1) + " 位，计算得出最后一位应该是：[ " + checkBit + " ]";
        } else if (s_len == bit) {
            for (int i = 0; i < s_len - 1; i++) {
                result += (t = (s.charAt(s_len - i - 2) - 48) * 2) > 9 ? t - 9 : t;
                i++;
                {
                    int j = s_len - i - 2;
                    if (j < 0) break;
                    result += (s.charAt(j) - 48);
                }
            }
            mod = result % 10;
            if (mod == 0) {
                checkBit = "0";
            } else if (mod >= 1 && mod <= 9) {
                checkBit = (10 - mod) + "";
            } else {
                checkBit = "";
            }
            if (checkBit.equals(s.charAt(s_len - 1) + "")) {
                return "你的输入号码[ " + s + " ]为正确的号码";
            } else {
                return "你的输入号码[ " + s + " ]为错误的号码";
            }
        } else {
            return "位数不符(你的输入为 " + s_len + " 位)，请确认输入一串 " + (bit - 1) + " 或 "
                    + bit + " 位的数字！";
        }
    }

    public double calcTax(double s) {
        double XSum = s - 3500;
        double Rate = 0, Balan = 0, TSum;
        if (XSum <= 1500) {
            Rate = 3;
            Balan = 0;
        }
        if ((1500 < XSum) && (XSum <= 4500)) {
            Rate = 10;
            Balan = 105;
        }
        if ((4500 < XSum) && (XSum <= 9000)) {
            Rate = 20;
            Balan = 555;
        }
        if ((9000 < XSum) && (XSum <= 35000)) {
            Rate = 25;
            Balan = 1005;
        }
        if ((35000 < XSum) && (XSum <= 55000)) {
            Rate = 30;
            Balan = 2755;
        }
        if ((55000 < XSum) && (XSum <= 80000)) {
            Rate = 35;
            Balan = 5505;
        }
        if (XSum > 80000) {
            Rate = 45;
            Balan = 13505;
        }
        TSum = (XSum * Rate) / 100 - Balan;
        if (TSum < 0) {
            TSum = 0;
        }
        return TSum;
    }

    public String calcWageGet(double originWage, double rate) {
        rate = rate / 100;
        double reportWage = originWage * rate;
        double oldAndLostJobBaseNumber, medicalAndWorkInjuryAndBirthBaseNumber, reserveBaseNumber;
        if (reportWage < 2317) {
            oldAndLostJobBaseNumber = 2317;
        } else if (reportWage > 17379) {
            oldAndLostJobBaseNumber = 17379;
        } else {
            oldAndLostJobBaseNumber = reportWage;
        }
        double pOld = oldAndLostJobBaseNumber * 0.08;
        double coOld = oldAndLostJobBaseNumber * 0.2;
        double pLostJob = oldAndLostJobBaseNumber * 0.002;
        double coLostJob = oldAndLostJobBaseNumber * 0.01;
        if (reportWage < 3476) {
            medicalAndWorkInjuryAndBirthBaseNumber = 3476;
        } else if (reportWage > 17379) {
            medicalAndWorkInjuryAndBirthBaseNumber = 17379;
        } else {
            medicalAndWorkInjuryAndBirthBaseNumber = reportWage;
        }
        double pMedical = 3 + 0.02 * medicalAndWorkInjuryAndBirthBaseNumber;
        double coMedical = medicalAndWorkInjuryAndBirthBaseNumber * 0.1;
        double coWorkInjury = medicalAndWorkInjuryAndBirthBaseNumber * 0.01;
        double coBirth = medicalAndWorkInjuryAndBirthBaseNumber * 0.008;
        if (reportWage < 1560) {
            reserveBaseNumber = 1560;
        } else if (reportWage > 17379) {
            reserveBaseNumber = 17379;
        } else {
            reserveBaseNumber = reportWage;
        }
        double pReserve = reserveBaseNumber * 0.12;
        double coReserve = reserveBaseNumber * 0.12;
        double pTotal = pReserve + pLostJob + pOld + pMedical;
        double coTotal = coReserve + coBirth + coWorkInjury + coLostJob + coOld + coMedical;
        double beforeTaxWage = originWage - pTotal;
        double tax = calcTax(beforeTaxWage);
        double getWage = beforeTaxWage - tax;
        return "最后能拿到手的是" + getWage + "，其中税交了" + tax + "，个人交了" + pTotal + "，单位交了" + coTotal + "，个人养老交了" + pOld + "，个人医疗交了" + pMedical + "，个人失业交了" + pLostJob + "，个人公积金交了" + pReserve + "，单位养老交了" + coOld + "，单位医疗交了" + coMedical + "，单位失业交了" + coLostJob + "，单位工伤交了" + coWorkInjury + ",单位生育交了" + coBirth + "，单位公积金交了" + coReserve;
    }
}
