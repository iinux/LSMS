package site.zhangqun.lanagina;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class General {
    public static final String LogTag = "Zhang";
    public static final String voiceFile = "/storage/sdcard0/Apk/voice/lsms.mp3";

    public static final String getAppPath() {
        File dirFile = new File(Environment.getExternalStorageDirectory()
                .getPath() + "/Lanagina");
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        return dirFile.toString();
    }

    public static final void out(Context context, Object s, LogLevel l) {
        //toast.setGravity(Gravity.CENTER, 0, 0);
        switch (l) {
            case TOAST: {
                String ss = (String) s;
                Toast.makeText(context, ss, Toast.LENGTH_SHORT).show();
            }
            break;
            case EXCEPTION: {
                Exception ss = (Exception) s;
                ss.printStackTrace();
                Toast.makeText(context, ss.toString(), Toast.LENGTH_SHORT).show();
            }
            break;
            case DEBUG: {
                String ss = (String) s;
                Log.i("Zhang", ss);
            }
            break;
            default:
                break;
        }
    }
}

enum LogLevel {
    TOAST, EXCEPTION, DEBUG, WARNING, ERROR
}
