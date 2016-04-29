package com.tool;
  
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
  
/**
 * 缓存InputStream，以便InputStream的重复利用 
 * @author boyce 
 * @version 2014-2-24 
 */  
public class InputStreamCacher {  
      

    /** 
     * 将InputStream中的字节保存到ByteArrayOutputStream中。 
     */  
    private ByteArrayOutputStream byteArrayOutputStream = null;  
      
    public InputStreamCacher(InputStream inputStream) {  
        if (null==inputStream)
            return;  
          
        byteArrayOutputStream = new ByteArrayOutputStream();  
        byte[] buffer = new byte[1024];    
        int len;    
        try {  
            while ((len = inputStream.read(buffer)) > -1 ) {    
                byteArrayOutputStream.write(buffer, 0, len);    
            }
            byteArrayOutputStream.flush();
            if(inputStream!=null){
                inputStream.close();
            }
        } catch (IOException e) {
            Log.e("ByteArrayOutputStream",e.getMessage());
        }
    }  
      
    public InputStream getInputStream() {  
        if (null==byteArrayOutputStream)
            return null;  
          
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());  
    }  
}  