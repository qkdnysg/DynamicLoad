package com.qkdnysg.dynamicload;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.qkdnysg.interfase.ILoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
import android.widget.Toast;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;

public class MainActivity extends Activity {
	//Button btn_loadJar;
	//Button btn_loadUninstalledAPK;
	//Button btn_loadInstalledAPK;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//btn_loadJar = (Button) findViewById(R.id.btn_loadJar);
		//btn_loadUninstalledAPK = (Button) findViewById(R.id.btn_loadUninstalledAPK);
		//btn_loadInstalledAPK = (Button) findViewById(R.id.btn_loadInstalledAPK);
	}
	
	public void loadJar(View v){
		final File optimizedDexOutputPath = new File(Environment.getExternalStorageDirectory().toString()  
	               + File.separator + "loader_dex.jar");  
	             
	           BaseDexClassLoader cl = new BaseDexClassLoader(Environment.getExternalStorageDirectory().toString(),  
	                optimizedDexOutputPath, optimizedDexOutputPath.getAbsolutePath(), getClassLoader());  
	           Class libProviderClazz = null;  
	             
	           try {  
	               // 载入JarLoader类， 并且通过反射构建JarLoader对象， 然后调用sayHi方法  
	               libProviderClazz = cl.loadClass("com.qkdnysg.interfase.JarLoader");  
	               ILoader loader = (ILoader)libProviderClazz.newInstance();  
	               Toast.makeText(MainActivity.this, loader.sayHi() , 1500).show();  //tst1500?
	           } catch (Exception exception) {  
	               // Handle exception gracefully here.  
	               exception.printStackTrace();  
	           }  
	}
	
	public void loadUninstalledAPK(View v){  
	       String path = Environment.getExternalStorageDirectory() + File.separator;  
	       String filename = "APKtobeLoaded.apk";  
	  
	       // 4.1以后不能够将optimizedDirectory设置到sd卡目录， 否则抛出异常.  
	       File optimizedDirectoryFile = getDir("dex", 0) ;  //getDir:在/data/data/包名/下创建app_dex目录，此处为：/data/data/com.qkdnysg.dynamicload/app_dex
	       DexClassLoader classLoader = new DexClassLoader(path + filename, optimizedDirectoryFile.getAbsolutePath(),  
	                                                        null, getClassLoader());  
	  
	       try {  
	        // 通过反射机制调用， 包名为com.example.loaduninstallapkdemo, 类名为UninstallApkActivity  
	           Class mLoadClass = classLoader.loadClass("com.qkdnysg.apktobeloaded.UninstallApkActivity");  
	           Constructor constructor = mLoadClass.getConstructor(new Class[] {});  
	           Object testActivity = constructor.newInstance(new Object[] {});  
	             
	           // 获取sayHello方法  
	           Method helloMethod = mLoadClass.getMethod("sayHello", null);  
	           helloMethod.setAccessible(true);  
	           Object content = helloMethod.invoke(testActivity, null);  
	           Toast.makeText(MainActivity.this, content.toString(), Toast.LENGTH_LONG).show();  
	             
	       } catch (Exception e) {  
	           e.printStackTrace();  
	       }  
	}  
	
	public void loadInstalledAPK(View v) {  
		try {  
		    String pkgName = "com.qkdnysg.apktobeloaded";  
		    Context context = createPackageContext(pkgName,  
		            Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE) ;  
		      
		    // 获取动态加载得到的资源  
		    Resources resources = context.getResources() ;  
		    // 获取该apk中的字符串资源"tips"， 并且toast出来，apk换肤的实现就是这种原理 
		    int id = resources.getIdentifier("hello_world", "string", pkgName) ;
		    String toast = resources.getString(id) ;  
		    Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show() ;  
		      
		    Class cls = context.getClassLoader().loadClass(pkgName + ".UninstallApkActivity") ;  
		    // 跳转到该Activity  
		    startActivity(new Intent(context, cls)) ;  
		} catch (NameNotFoundException e) {  
		    e.printStackTrace();  
		}catch (ClassNotFoundException e) {  
		    Log.d("", e.toString()) ;  
		} 
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
