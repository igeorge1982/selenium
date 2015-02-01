package test.java;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
 
public class SampleDataProvider {
 
    @DataProvider
    public static Iterator<Object[]> fileDataProvider (ITestContext context) {
        //Get the input file path from the ITestContext
        String inputFile = context.getCurrentXmlTest().getParameter("filenamePath");
        //Get a list of String file content (line items) from the test file.
        List<String> testData = getFileContentList(inputFile);
 
        //We will be returning an iterator of Object arrays so create that first.
        List<Object[]> dataToBeReturned = new ArrayList<Object[]>();
 
        //Populate our List of Object arrays with the file content.
        for (String userData : testData)
        {
            dataToBeReturned.add(new Object[] { userData } );
        }
        //return the iterator - testng will initialize the test class and calls the
        //test method with each of the content of this iterator.
        return dataToBeReturned.iterator();
 
    }
    
    @DataProvider(name="colors") 
    public static Iterator<Object[]> getColors(){
    	  Set<Object[]> result=new HashSet<Object[]>();
    	  result.add(new Object[]{"black"});
    	  result.add(new Object[]{"silver"});
    	  result.add(new Object[]{"gray"});
    	  result.add(new Object[]{"white"});
    	  result.add(new Object[]{"maroon"});
    	  result.add(new Object[]{"red"});
    	  result.add(new Object[]{"purple"});
    	  result.add(new Object[]{"fuchsia"});
    	  result.add(new Object[]{"green"});
    	  result.add(new Object[]{"lime"});
    	  result.add(new Object[]{"olive"});
    	  result.add(new Object[]{"yellow"});
    	  result.add(new Object[]{"navy"});
    	  result.add(new Object[]{"blue"});
    	  result.add(new Object[]{"teal"});
    	  result.add(new Object[]{"aqua"});
    	  return result.iterator();
    	}
 
    private static List<String> getFileContentList(String filenamePath)
    {
        //Sample utility method to get the file content, any version of
        //this can be adapted, this is just one way of achieving the result.
        InputStream is;
        List<String> lines = new ArrayList<String> ();
        try {
            is = new FileInputStream(new File(filenamePath));
            DataInputStream in = new DataInputStream(is);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null)   {
                lines.add(strLine);
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }    
        return lines;
    }
}


