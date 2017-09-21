/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project8;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;
import validate.Validator;


public class ReadFile {
    String fileName;
    List<String> fileContent = new ArrayList<>();
    //BufferedReader reader;
    
    public ReadFile(String fileName)
    {
        this.fileName = "";
        this.fileName = fileName;
    }
    
    public static boolean isFileReal(String fileName)
    {
        Path filePath = Paths.get("PriceList.txt");
        return Files.exists(filePath);
    }
    
    public boolean isLineCategory(String line)
    {
        return line.contains("<");
    }
    
    public List<String> getFileContent()
    {
        //List<String> fileContent = new ArrayList<>();
        if (!fileName.isEmpty())
        {
            try{
            BufferedReader reader = Files.newBufferedReader(Paths.get(fileName), Charset.defaultCharset());
            //this.reader = reader;
            //reader.
            fileContent = Files.readAllLines(Paths.get(fileName), Charset.forName("UTF-8"));
            //this.fileContent = fileContent;
            } catch (IOException io){};
        }
        return fileContent;
    }
    
    public String getItem(String line)
    {
        String item = "";
        if (!line.isEmpty())
        {
            if (line.contains("\t"))
            {
                item =  line.substring(0, line.indexOf('\t'));
            }
        }
        return item;
    }
    
    public int getPrice(String line)
    {
        String price="";
        
        if (!line.isEmpty())
        {
            if (line.contains("\t"))
            {
                price = line.substring(line.indexOf('\t')+1);
            }
        }
        if (Validator.TryParseInt(price))
            return Integer.parseInt(price);
        else
            return 0;
    }
    
    public String getCategory(String line)
    {
        return line.replace("<", "").replace(">", "");
    }
}
