package site.zhangqun.lanagina;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class QRActivity extends AppCompatActivity {

    private final static int RESULT_LOAD_IMAGE = 2;

    protected ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qractivity_main);
        mImageView = (ImageView) findViewById(R.id.qrImageView);
    }

    public void onScanBarcode(View v) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("扫描条形码");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    public void onScanQrcode(View v) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("扫描二维码");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    public void onGenerateQrcode(View v) {
        EditText qrText = (EditText) findViewById(R.id.qrText);

        // 二维码图片
        Bitmap bmp = null;
        QRCodeWriter writer = new QRCodeWriter();
        try {
            // 参数分别表示为: 条码文本内容，条码格式，宽，高
            BitMatrix bitMatrix = writer.encode(qrText.getText().toString(), BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            // 绘制每个像素
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

        } catch (WriterException e) {
            e.printStackTrace();
        }

        mImageView.setImageBitmap(bmp);
    }

    public void onLoadQrcode(View v) {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public void onResolveQrcode(View v) {
        Map<DecodeHintType, String> hints = new HashMap<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");

        // 获得待解析的图片
        Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
        RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            Result result = reader.decode(bitmap1);// ,hints);
            // 得到解析后的文字
            EditText tv = (EditText) findViewById(R.id.qrText);
            tv.setText(result.getText());
        } catch (Exception e) {
            General.out(QRActivity.this, e, LogLevel.EXCEPTION);
        }
    }

    public void onSaveQrcode(View v) {
        final EditText etInput = new EditText(this);
        final AlertDialog dlg = new AlertDialog.Builder(QRActivity.this)
                .setTitle("输入文件名（不含扩展名）").setView(etInput)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        General.out(QRActivity.this, "AlertDialog EditText:"
                                + etInput.getText().toString(), LogLevel.DEBUG);
                        File myCaptureFile = new File(General.getAppPath()
                                + "/" + etInput.getText().toString() + ".jpg");
                        BufferedOutputStream bos;
                        try {
                            General.out(QRActivity.this, myCaptureFile.toString(), LogLevel.DEBUG);
                            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                            ((BitmapDrawable) mImageView.getDrawable())
                                    .getBitmap().compress(Bitmap.CompressFormat.JPEG, 80, bos);
                            bos.flush();
                            bos.close();
                            General.out(QRActivity.this, "Save file ( " + myCaptureFile
                                    + " ) success!", LogLevel.TOAST);
                        } catch (Exception e) {
                            General.out(QRActivity.this, e, LogLevel.EXCEPTION);
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        }).create();
        dlg.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "扫码取消！", Toast.LENGTH_LONG).show();
            } else {
                EditText tv = (EditText) findViewById(R.id.qrText);
                tv.setText(result.getContents());

                mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
            }
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if (resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    mImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                }
                break;
        }
    }
}
