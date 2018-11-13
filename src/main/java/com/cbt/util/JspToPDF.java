package com.cbt.util;

import org.springframework.stereotype.Component;
import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.security.InvalidParameterException;

@Component
public class JspToPDF {

	  protected int topValue = 10;  
      protected int leftValue = 20;  
      protected int rightValue = 10;  
      protected int bottomValue = 10;  
      protected int userSpaceWidth = 1300;  
      
      public void doConversion2( String htmlDocument, String outputPath )   
              throws InvalidParameterException, MalformedURLException, IOException {  

      PD4ML pd4ml = new PD4ML();  
            
      pd4ml.setHtmlWidth(userSpaceWidth); // set frame width of "virtual web browser"   
            
      // choose target paper format  
      pd4ml.setPageSize(pd4ml.changePageOrientation(PD4Constants.A4));  
      //pd4ml.setPageSize(PD4Constants.A4);
            
      // define PDF page margins  
      pd4ml.setPageInsetsMM(new Insets(topValue, leftValue, bottomValue, rightValue));   

      // source HTML document also may have margins, could be suppressed this way   
      // (PD4ML *Pro* feature):  
      pd4ml.addStyle("BODY {margin: 0}", true);  
            
      // If built-in basic PDF fonts are not sufficient or   
      // if you need to output non-Latin texts, TTF embedding feature should help   
      // (PD4ML *Pro*)  
      pd4ml.useTTF("c:/windows/fonts", true);  

      ByteArrayOutputStream baos = new ByteArrayOutputStream();  
      // actual document conversion from HTML string to byte array  
      pd4ml.render(new StringReader(htmlDocument), baos);   
      // if the HTML has relative references to images etc,   
      // use render() method with baseDirectory parameter instead  
      baos.close();  
        
      System.out.println( "resulting PDF size: " + baos.size() + " bytes" );  
      // in Web scenarios it is a good idea to send the size with   
      // "Content-length" HTTP header  

      File output = new File(outputPath);  
      java.io.FileOutputStream fos = new java.io.FileOutputStream(output);  
      fos.write( baos.toByteArray() );  
      fos.close();  
        
      System.out.println( outputPath + "\ndone." );  
  }  
    
  public String readFile( String path, String encoding ) throws IOException {  

      File f = new File( path );  
      FileInputStream is = new FileInputStream(f);  
      BufferedInputStream bis = new BufferedInputStream(is);  
        
      ByteArrayOutputStream fos = new ByteArrayOutputStream();  
      byte buffer[] = new byte[2048];  

      int read;  
      do {  
          read = is.read(buffer, 0, buffer.length);  
          if (read > 0) {   
              fos.write(buffer, 0, read);   
          }  
      } while (read > -1);  

      fos.close();  
      bis.close();  
      is.close();  

      return fos.toString(encoding);  
  }  
}
