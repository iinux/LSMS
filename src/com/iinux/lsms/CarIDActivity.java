package com.iinux.lsms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CarIDActivity extends Activity{
	ArrayList<String> listAll;
	Activity a;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startView(this, null);
	}
	public void startView(final Activity activity,String query) {
		a=activity;
		try {
			initData(query);
		} catch (Exception e) {
			General.out(activity, e, LogLevel.EXCEPTION);
			return;
		}
		ListView listView=new ListView(activity);
		listView.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_expandable_list_item_1,listAll));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String s = ((TextView)arg1).getText().toString();
				if (s.indexOf(',') == -1) {
					new CarIDActivity().startView(activity, s);
				}
			}
		});
		activity.setContentView(listView);
	}
	private void initData(String query) throws IOException{
		InputStream is = a.getAssets().open("CarID.txt");
		BufferedReader br=new BufferedReader(new InputStreamReader(is));
		String s;
		String[] ss;
		String province;
		listAll = new ArrayList<String>();
		while(true){
			s=br.readLine();
			if (s==null) break;
			ss=s.split(",");
			province=ss[0].substring(0,ss[0].length()-1);
			ss[0].substring(ss[0].length()-1);
			if (query==null){
				if (!listAll.contains(province)){
					listAll.add(province);
				}
			}else{
				if (province.equals(query)){
					listAll.add(s);
				}
			}
		}
		br.close();
	}
}
	
	
	
	
	/*
}
	private SimpleAdapter adapter;// ��������������
	private ListView listView; // �����б���ͼ����
	private List<Map<String, Object>> list;// �����б�����
	public static MainActivity ma;

	public CarID(Activity activity) {
		// ʵ�����б�����
		list = new ArrayList<Map<String, Object>>();
		listView = new ListView(activity);// ʵ�����б���ͼ
		// ʵ��һ���б���������
		Map<String, Object> map = new HashMap<String, Object>();
		// ���б��������������
		map.put("item1_imageivew", R.drawable.ic_launcher);
		map.put("item1_bigtv", "BIGTV");
		map.put("item1_smalltv", "SMALLTV");
		// ���б�������ӵ��б�������
		list.add(map);
		// ʹ��Android �ṩ��SimpleAdapter������,�޷�ʵ�����������
		// adapter = new SimpleAdapter(this, list, R.layout.main,
		// new String[] {"item1_imageivew", "item1_bigtv", "item1_smalltv" },
		// new int[] {R.id.iv, R.id.bigtv, R.id.smalltv });
		// --ʹ���Զ������������ɼ�����ListView��ÿһ����¼�����
		MySimpleAdapter adapter = new MySimpleAdapter(this, list,
				R.layout.main, new String[] { "item1_imageivew", "item1_bigtv",
						"item1_smalltv" }, new int[] { R.id.iv, R.id.bigtv,
						R.id.smalltv });
		// Ϊ�б���ͼ������������������ӳ�䵽�б���ͼ�У�
		listView.setAdapter(adapter);
		// //��ʾ�б���ͼ
		activity.setContentView(listView);
	}
}

class MySimpleAdapter extends BaseAdapter {
	//����һ��LayoutInflater����������������ʵ�������֣�
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;//����List��������
	private int layoutID; //��������ID
	private String flag[];//����ListView�����������ӳ������
	private int ItemIDs[];//����ListView�����������ID����
	private  Context context;
	public MySimpleAdapter(Context context, List<Map<String, Object>> list,
			int layoutID, String flag[], int ItemIDs[]) {
		//���ù�����ʵ������Ա��������
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
		this.layoutID = layoutID;
		this.flag = flag;
		this.ItemIDs = ItemIDs;
		this.context=context;
	}
	@Override
	public int getCount() {
		return list.size();//����ListView��ĳ���
	}

	@Override
	public Object getItem(int arg0) {
		return 0;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	//ʵ��������������Լ������������
	//getView(int position, View convertView, ViewGroup parent)
	//��һ�����������Ƶ�����
	//�ڶ������������Ƶ���ͼ����ָ����ListView��ÿһ��Ĳ���
	//������������view�ĺϼ������ﲻ��Ҫ
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//������ͨ��mInflater����ʵ����Ϊһ��view
		convertView = mInflater.inflate(layoutID, null);
		for (int i = 0; i < flag.length; i++) {//����ÿһ����������
			//ÿ���������ƥ���жϣ��õ��������ȷ����
			if (convertView.findViewById(ItemIDs[i]) instanceof ImageView) {
				//findViewById()����������ʵ���������е����
				//�����ΪImageView���ͣ���Ϊ��ʵ����һ��ImageView����
				ImageView iv = (ImageView) convertView.findViewById(ItemIDs[i]);
				//Ϊ�������������
				iv.setBackgroundResource((Integer) list.get(position).get(
						flag[i]));
			} else if (convertView.findViewById(ItemIDs[i]) instanceof TextView) {
				//�����ΪTextView���ͣ���Ϊ��ʵ����һ��TextView����
				TextView tv = (TextView) convertView.findViewById(ItemIDs[i]);
				//Ϊ�������������
				tv.setText((String) list.get(position).get(flag[i]));
			} 
		} 
		//Ϊ��ť���ü���
		((Button)convertView.findViewById(R.id.btn)).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//���ﵯ��һ���Ի��򣬺�������ϸ����
						new AlertDialog.Builder(context)
						.setTitle("�Զ���SimpleAdapter")
						.setMessage("��ť�ɹ����������¼���")
						.show();
					} 
				});
		//Ϊ��ѡ�����ü���
		((CheckBox)convertView.findViewById(R.id.cb)).
		setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//���ﵯ��һ���Ի��򣬺�������ϸ����
				new AlertDialog.Builder(MainActivity.ma)
				.setTitle("�Զ���SimpleAdapter")
				.setMessage("CheckBox�ɹ�����״̬�ı�����¼���")
				.show();
			}
		});
		return convertView;
	} 
}*/