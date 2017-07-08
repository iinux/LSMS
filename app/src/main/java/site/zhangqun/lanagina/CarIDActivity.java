package site.zhangqun.lanagina;

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
import android.widget.Toast;

public class CarIDActivity extends Activity {
    ArrayList<String> listAll;
    Activity a;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startView(this, null);
    }

    public void startView(final Activity activity, String query) {
        a = activity;
        try {
            initData(query);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
        ListView listView = new ListView(activity);
        listView.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_expandable_list_item_1, listAll));
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String s = ((TextView) arg1).getText().toString();
                if (s.indexOf(',') == -1) {
                    new CarIDActivity().startView(activity, s);
                }
            }
        });
        activity.setContentView(listView);
    }

    private void initData(String query) throws IOException {
        InputStream is = a.getAssets().open("CarID.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s;
        String[] ss;
        String province;
        listAll = new ArrayList<String>();
        while (true) {
            s = br.readLine();
            if (s == null) break;
            ss = s.split(",");
            province = ss[0].substring(0, ss[0].length() - 1);
            ss[0].substring(ss[0].length() - 1);
            if (query == null) {
                if (!listAll.contains(province)) {
                    listAll.add(province);
                }
            } else {
                if (province.equals(query)) {
                    listAll.add(s);
                }
            }
        }
        br.close();
    }
}

	
	
	
	/*
}
	private SimpleAdapter adapter;// 声明适配器对象
	private ListView listView; // 声明列表视图对象
	private List<Map<String, Object>> list;// 声明列表容器
	public static MainActivity ma;

	public CarID(Activity activity) {
		// 实例化列表容器
		list = new ArrayList<Map<String, Object>>();
		listView = new ListView(activity);// 实例化列表视图
		// 实例一个列表数据容器
		Map<String, Object> map = new HashMap<String, Object>();
		// 往列表容器中添加数据
		map.put("item1_imageivew", R.drawable.ic_launcher);
		map.put("item1_bigtv", "BIGTV");
		map.put("item1_smalltv", "SMALLTV");
		// 将列表数据添加到列表容器中
		list.add(map);
		// 使用Android 提供的SimpleAdapter适配器,无法实现组件监听；
		// adapter = new SimpleAdapter(this, list, R.layout.main,
		// new String[] {"item1_imageivew", "item1_bigtv", "item1_smalltv" },
		// new int[] {R.id.iv, R.id.bigtv, R.id.smalltv });
		// --使用自定义适配器，可监听其ListView中每一项的事件监听
		MySimpleAdapter adapter = new MySimpleAdapter(this, list,
				R.layout.main, new String[] { "item1_imageivew", "item1_bigtv",
						"item1_smalltv" }, new int[] { R.id.iv, R.id.bigtv,
						R.id.smalltv });
		// 为列表视图设置适配器（将数据映射到列表视图中）
		listView.setAdapter(adapter);
		// //显示列表视图
		activity.setContentView(listView);
	}
}

class MySimpleAdapter extends BaseAdapter {
	//声明一个LayoutInflater对象（其作用是用来实例化布局）
	private LayoutInflater mInflater;
	private List<Map<String, Object>> list;//声明List容器对象
	private int layoutID; //声明布局ID
	private String flag[];//声明ListView项中所有组件映射索引
	private int ItemIDs[];//声明ListView项中所有组件ID数组
	private  Context context;
	public MySimpleAdapter(Context context, List<Map<String, Object>> list,
			int layoutID, String flag[], int ItemIDs[]) {
		//利用构造来实例化成员变量对象
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
		this.layoutID = layoutID;
		this.flag = flag;
		this.ItemIDs = ItemIDs;
		this.context=context;
	}
	@Override
	public int getCount() {
		return list.size();//返回ListView项的长度
	}

	@Override
	public Object getItem(int arg0) {
		return 0;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	//实例化布局与组件以及设置组件数据
	//getView(int position, View convertView, ViewGroup parent)
	//第一个参数：绘制的行数
	//第二个参数：绘制的视图这里指的是ListView中每一项的布局
	//第三个参数：view的合集，这里不需要
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//将布局通过mInflater对象实例化为一个view
		convertView = mInflater.inflate(layoutID, null);
		for (int i = 0; i < flag.length; i++) {//遍历每一项的所有组件
			//每个组件都做匹配判断，得到组件的正确类型
			if (convertView.findViewById(ItemIDs[i]) instanceof ImageView) {
				//findViewById()函数作用是实例化布局中的组件
				//当组件为ImageView类型，则为其实例化一个ImageView对象
				ImageView iv = (ImageView) convertView.findViewById(ItemIDs[i]);
				//为其组件设置数据
				iv.setBackgroundResource((Integer) list.get(position).get(
						flag[i]));
			} else if (convertView.findViewById(ItemIDs[i]) instanceof TextView) {
				//当组件为TextView类型，则为其实例化一个TextView对象
				TextView tv = (TextView) convertView.findViewById(ItemIDs[i]);
				//为其组件设置数据
				tv.setText((String) list.get(position).get(flag[i]));
			} 
		} 
		//为按钮设置监听
		((Button)convertView.findViewById(R.id.btn)).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//这里弹出一个对话框，后文有详细讲述
						new AlertDialog.Builder(context)
						.setTitle("自定义SimpleAdapter")
						.setMessage("按钮成功触发监听事件！")
						.show();
					} 
				});
		//为复选框设置监听
		((CheckBox)convertView.findViewById(R.id.cb)).
		setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//这里弹出一个对话框，后文有详细讲述
				new AlertDialog.Builder(MainActivity.ma)
				.setTitle("自定义SimpleAdapter")
				.setMessage("CheckBox成功触发状态改变监听事件！")
				.show();
			}
		});
		return convertView;
	} 
}*/