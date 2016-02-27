package com.iinux.lsms;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;

public class QRActivity extends Activity {
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private final static int RESULT_LOAD_IMAGE = 2;
	private static int QR_WIDTH = 400;
	private static int QR_HEIGHT = 400;
	private Button btnScan, btn_decodeQRCode, btn_generateQRCode, btn_save;
	/**
	 * 显示扫描结果
	 */
	private EditText mEditText;
	/**
	 * 显示扫描拍的图片
	 */
	private ImageView mImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qractivity);

		mEditText = (EditText) findViewById(R.id.etString);
		mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);
		// 点击按钮跳转到二维码扫描界面，这里用的是startActivityForResult跳转
		// 扫描完了之后调到该界面
		btnScan = (Button) findViewById(R.id.btnQR);
		btnScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(QRActivity.this, MipcaCaptureActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
		});
		btn_generateQRCode = (Button) findViewById(R.id.btn_generateQRCode);
		btn_generateQRCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String text = mEditText.getText().toString();
				if ( null == text ||"".equals(text)) {
					General.out(QRActivity.this, "请输入内容", LogLevel.TOAST);
					return;
				}
				Bitmap bitmap = createBitmap(text);
				if (bitmap != null) {
					mImageView.setImageBitmap(bitmap);
				}
			}
		});
		btn_decodeQRCode = (Button) findViewById(R.id.btn_decodeQRCode);
		btn_decodeQRCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = readImage(mImageView);
				mEditText.setText(content);
				General.out(QRActivity.this, "decode QR button press", LogLevel.DEBUG);
			}
		});

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
									.getBitmap().compress(Bitmap.CompressFormat.JPEG, 80,bos);
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
		btn_save = (Button) findViewById(R.id.btnSave);
		btn_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dlg.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, Menu.FIRST, 0, "身份证号码计算");
		menu.add(0, Menu.FIRST + 1, 0, "银行卡号计算");
		menu.add(0, Menu.FIRST + 2, 0, "手机识别码（IMEI号）计算");
		menu.add(0, Menu.FIRST + 3, 0, "自定义Luhn计算");
		menu.add(0, Menu.FIRST + 4, 0, "加载图片");
		menu.add(0, Menu.FIRST + 5, 0, "个人所得税计算");
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				// 显示扫描到的内容
				mEditText.setText(bundle.getString("result"));
				// 显示
				mImageView.setImageBitmap((Bitmap) data
						.getParcelableExtra("bitmap"));
			}
			break;
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

				mImageView
						.setImageBitmap(BitmapFactory.decodeFile(picturePath));

			}
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		General.out(QRActivity.this, "onOptionsItemSelected item id is " + item.getItemId(), LogLevel.DEBUG);
		switch (item.getItemId()) {
		case Menu.FIRST:
			General.out(QRActivity.this, new LsmsCompute().calcShenfenzheng(mEditText.getText().toString()), LogLevel.TOAST);
			break;
		case Menu.FIRST + 1:
			General.out(QRActivity.this, new LsmsCompute().calcLuhn(mEditText.getText().toString(), 19), LogLevel.TOAST);
			break;
		case Menu.FIRST + 2:
			General.out(QRActivity.this, new LsmsCompute().calcLuhn(mEditText.getText().toString(), 15), LogLevel.TOAST);
			break;
		case Menu.FIRST + 3:
			final EditText etInput = new EditText(this);
			AlertDialog dlg = new AlertDialog.Builder(QRActivity.this)
					.setTitle("输入位数").setView(etInput).setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									String bit=etInput.getText().toString();
									String str=mEditText.getText().toString();
									if (bit.length()>0 && str.length()>0){
										try{
											General.out(QRActivity.this,new LsmsCompute().calcLuhn(str,Integer.parseInt(bit)),LogLevel.TOAST);
										}catch(Exception e){
											General.out(QRActivity.this, e, LogLevel.EXCEPTION);
										}
									} else {
										General.out(QRActivity.this, "请输入", LogLevel.TOAST);
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
			break;
		case Menu.FIRST + 4:
			Intent i = new Intent(Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, RESULT_LOAD_IMAGE);
			break;
		case Menu.FIRST + 5:
			General.out(QRActivity.this,
					new LsmsCompute().calcTax(mEditText.getText().toString()),
					LogLevel.TOAST);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 生成二维码图片
	 *
	 * @return
	 */
	private Bitmap createBitmap(String text) {
		Bitmap bitmap = null;
		try {
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix = new QRCodeWriter().encode(text,
					BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);

			// QRCodeWriter writer = new QRCodeWriter();
			// // 把输入的文本转为二维码
			// BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE,
			// QR_WIDTH, QR_HEIGHT);

			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			for (int y = 0; y < QR_HEIGHT; y++) {
				for (int x = 0; x < QR_WIDTH; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * QR_WIDTH + x] = 0xff000000;
					} else {
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}

				}
			}
			bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);

		} catch (WriterException e) {
			General.out(QRActivity.this, e, LogLevel.EXCEPTION);
		}
		return bitmap;
	}

	/**
	 * 解析QR图内容
	 *
	 * @param imageView
	 * @return
	 */
	private String readImage(ImageView imageView) {
		String content = null;
		Map<DecodeHintType, String> hints = new HashMap<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8");

		// 获得待解析的图片
		Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
		RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			Result result = reader.decode(bitmap1);// ,hints);
			// 得到解析后的文字
			content = result.getText();
		} catch (Exception e) {
			General.out(QRActivity.this, e, LogLevel.EXCEPTION);
		}
		return content;
	}

}
