package com.qkdnysg.apktobeloaded;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class UninstallApkActivity extends Activity implements ISayHello {
	
	 private View mShowView = null ;  
     
	    @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.activity_main);  
	          
	        mShowView = findViewById(R.id.show) ;  
	        mShowView.setOnClickListener(new OnClickListener() {  
	              
	            @Override  
	            public void onClick(View v) {  
	                Toast.makeText(UninstallApkActivity.this, "这是已安装的apk被动态加载了", Toast.LENGTH_SHORT).show();  
	            }  
	        }) ;  
	    }  
	  
	   
	@Override
	public String sayHello() {
		// TODO Auto-generated method stub
		return "Hello, this apk is not installed";
	}

}
