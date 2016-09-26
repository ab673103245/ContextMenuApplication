package qianfeng.contextmenuapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private ListView lv;

    private List<String> list;
    private ArrayAdapter<String> adapter;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn = ((Button) findViewById(R.id.btn));


        initInfo();

        // 注册上下文菜单
        registerForContextMenu(btn);

        registerForContextMenu(lv);


    }

    // 添加上下文菜单的方法


    @Override  //  这个View v  就是我们注册的那个 register(btn)，谁调用register()，谁被长按时，这个方法就会被执行,
                // 先是onCreate，然后长按的时候，才会执行onCtreateContextMenu(),对应的View就是被长按的那个控件

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        // 怎样添加,--->
        getMenuInflater().inflate(R.menu.menu,menu);  // 加载这个布局文件的item内容

        if (v == btn) {  // 这两者是否相等，可以用hashCode()方法来检验
            menu.setHeaderTitle("上下文菜单");
            menu.setHeaderIcon(R.mipmap.ic_launcher);

            menu.add(Menu.NONE,1,2,"删除数据");

            SubMenu subMenu = menu.addSubMenu("二级菜单1");
            subMenu.add(Menu.NONE, 2, 3, "查找数据");
            subMenu.add(Menu.NONE,3,4,"修改数据");
        }else if(v == lv)
        {

            menu.setHeaderTitle("ListView");

            // 关键要想按第二级菜单，要利用一级菜单里面的item来获取上下文，获取被长按的item的position的值，然后用全局变量来记录，再传递给第二级菜单
            menu.add("查看数据");
            SubMenu subMenu = menu.addSubMenu(Menu.NONE, 2, 4, "操作数据");
            subMenu.add(Menu.NONE,3,5,"删除数据");
            subMenu.add(Menu.NONE,4,6,"修改数据");

        }


        super.onCreateContextMenu(menu, v, menuInfo);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.add:
                Toast.makeText(this,"123",Toast.LENGTH_SHORT).show();
                break;

            case 2:
                // 思路，要想到第二级菜单，是不是要先点击第一级菜单，这是不是可以利用第一级菜单的上下文，获取到已经操作的list集合的position？（也可以说成是item的position）
                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//                ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo())
                position = menuInfo.position;
                break;

            case 3:
                list.remove(position);
                adapter.notifyDataSetChanged();   // adapter.notifyDataSetChanged();说明这个notifyDataSetChanged是可以直接使用在Adapter类里面的
                // 既然调用了adapter这个对象，那这个notifyDataSetChanged就是在Adapter类里面的方法，在这个Adapter类里面使用，是毫无疑问的可以的。
                break;

        }
        return super.onContextItemSelected(item);
    }


    @Override
    protected void onDestroy() {

        unregisterForContextMenu(lv);
        unregisterForContextMenu(btn);
        super.onDestroy();
    }

    private void initInfo()
    {
        lv = (ListView) findViewById(R.id.lv);

        list = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            list.add("lisi:"+i);
        }

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        lv.setAdapter(adapter);

    }

    public void onclick(View view) {
        // 给按钮加一个点击事件

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("提示").setMessage("是否退出应用？").setIcon(R.mipmap.ic_launcher).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"确定",Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("取消",null).setNeutralButton("中立",null);

        // 创建一个警告框
        AlertDialog alertDialog = builder.create();

        // 显示一个警告框
        alertDialog.show();


    }
}
