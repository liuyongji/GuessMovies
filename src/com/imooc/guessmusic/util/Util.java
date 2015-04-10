package com.imooc.guessmusic.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;

public class Util {

	public static View getView(Context context, int layoutId) {
		LayoutInflater inflater = (LayoutInflater)context.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View layout = inflater.inflate(layoutId, null);
		
		return layout;
	}
	 public static String getFromAssets(Context context,String fileName){  
	        String result = "";  
	            try {  
	                InputStream in = context.getResources().getAssets().open(fileName);  
	                //获取文件的字节数  
	                int lenght = in.available();  
	                //创建byte数组  
	                byte[]  buffer = new byte[lenght];  
	                //将文件中的数据读到byte数组中  
	                in.read(buffer);  
	                result = EncodingUtils.getString(buffer, "UTF-8");  
	            } catch (Exception e) {  
	                e.printStackTrace();  
	            }  
	            return result;  
	    }
	 public static Bitmap getImageFromAssetsFile(Context context, String fileName) {   
		    Bitmap image = null;   
		    AssetManager am = context.getResources().getAssets();   
		    try {   
		        InputStream is = am.open(fileName);   
		        image = BitmapFactory.decodeStream(is);   
		        is.close();   
		    } catch (IOException e) {   
		        e.printStackTrace();   
		    }   
		    return image;   
		} 
}
