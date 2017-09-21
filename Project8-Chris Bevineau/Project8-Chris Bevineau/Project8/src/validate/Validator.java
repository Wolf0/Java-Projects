/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package validate;

import java.awt.*;
import java.util.Scanner;
import java.util.regex.*;
//import java.lang.*;
import javax.swing.*;

/**
 *
 * @author vince
 */
public class Validator {
    public static String getWord(String message, Scanner sc)
    {
        String word = "";
        while (word.equals(""))
        {
            System.out.print(message);
            if (sc.hasNext("\\w+"))
            {
                word = sc.next();
            }
            else
            {
                System.out.println("The entered data includes illegal characters! Try again.\n");
                sc.next();
            }
            sc.nextLine();
        }
        return word;
    }
    
    public static String getEMail(String message, Scanner sc)
    {
        String eMail = "";
        while (eMail.equals(""))
        {
            System.out.print(message);
            if (sc.hasNextLine())
            {
                eMail = sc.nextLine();
                if (!Pattern.matches("^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$", eMail))
                {
                    System.out.println("This is not a valid email address! Try again.\n");
                    eMail = "";
                }
                //sc.nextLine();
            }
        }
        return eMail;
    }
    
    public static String getName(String message, Scanner sc)
    {
        String name = "";
        while (name.equals(""))
        {
            System.out.print(message);
            if (sc.hasNext("[a-zA-Z]+"))
            {
                name = sc.next();
            }
            else
            {
                System.out.println("The name entered includes non letter characters! Try again.\n");
                sc.next();
            }
            sc.nextLine();
        }
        return name;
    }
    
    public static String getChoice(String message, String pattern, Scanner sc)
    {
        String choice = "";
        while (choice.equals(""))
        {
            System.out.print(message);
            if (sc.hasNext(pattern))
                choice = sc.next();
            else {
                System.out.println("This is not a correct choice!\n");
                sc.next();
            }
            sc.nextLine();
        }
        return choice;
    }
    
    public static double getDouble(String message, Scanner sc)
        {
            double number = 0;
            boolean valid = false;
            while (!valid)
            {
                System.out.print(message);
                if (sc.hasNextDouble())
                {
                    number = sc.nextDouble();
                    valid = true;
                }
                else
                {
                    System.out.println("Invalid input! Please try again.");
                    sc.next();
                }
                sc.nextLine();
            }   
            return number;
        }
        
        public static double getDoubleInRange(String message, Scanner sc, double lowerLimit, double upperLimit)
        {
            double number = 0;
            boolean inRange = false;
            while (!inRange)
            {
                number = getDouble(message, sc);
                if (number>=lowerLimit&&number<=upperLimit)
                    inRange = true;
                else
                {
                    System.out.println("The value submitted is not in the range "+lowerLimit+" to "+upperLimit+"! Please try again.");
                    System.out.println();
                }
            }
            return number;
        }
        public static int getInt(String message, Scanner sc)
        {
            int number = 0;
            boolean valid = false;
            while (!valid)
            {
                System.out.print(message);
                if (sc.hasNextInt())
                {
                    number = sc.nextInt();
                    valid = true;
                }
                else
                {
                    System.out.println("Invalid input! Please try again.");
                    sc.next();
                }
                sc.nextLine();
            }   
            return number;
        }
        
        public static int getIntInRange(String message, Scanner sc, int lowerLimit, int upperLimit)
        {
            int number = 0;
            boolean inRange = false;
            while (!inRange)
            {
                number = getInt(message, sc);
                if (number>=lowerLimit&&number<=upperLimit)
                    inRange = true;
                else
                {
                    System.out.println("The value submitted is not in the range "+lowerLimit+" to "+upperLimit+"! Please try again.");
                    System.out.println();
                }
            }
            return number;
        }//end GetIntInRange
        
        
        /////////////////////////////////////////////////////////////////////////////////////////////////
        //methods for GUI only!
        
        public static Component getParentFrame(Component c)
        {
            while (!(c instanceof Frame))
            {
                c = c.getParent();
                if (c==null)
                    break;
            }
            return c;
        }
        
        public static void errorMessage(String message, JTextField txtField)
        {
            JOptionPane.showMessageDialog(getParentFrame(txtField), message, "Invalid input",JOptionPane.ERROR_MESSAGE);
            txtField.selectAll();
            txtField.requestFocusInWindow();
        }
        
        public static boolean isTextFieldEmpty(JTextField txtField)
        {
            if (txtField.getText().isEmpty())
            {
                errorMessage("The field "+txtField.getName()+" is empty!\nPlease enter the required data.", txtField);
                return true;
            }
            return false;
        }
        
        public static  boolean isTextFieldInt(JTextField txtField)
        {
            if (!isTextFieldEmpty(txtField))
            {
                if (!TryParseInt(txtField.getText()))
                {
                   errorMessage("Data entered for "+txtField.getName()+" is not a valid integer!\nPlease try again!", txtField);
                   return false;
                }
                return true;
            }
            return false;
        }
        
        public static boolean isTextFieldIntInRange(JTextField txtField, int min, int max)
        {
            if (isTextFieldInt(txtField))
            {
                int number = Integer.parseInt(txtField.getText());
                if (number<min||number>max)
                {
                    errorMessage("The entered "+txtField.getName()+" value is not in the range of "+min+" to "+max+"!\nPlease try again.", txtField);
                    return false;
                }
                return true;
            }
            return false;
        }
        
        public static boolean isTextFieldDouble(JTextField txtField)
        {
            if (!isTextFieldEmpty(txtField))
            {
                if(!TryParseDouble(txtField.getText()))
                {
                   errorMessage("Data entered for "+txtField.getName()+" is not a valid number!\nPlease try again!", txtField);
                   return false;
                }
                return true;
            }
            return false;
        }
        
        public static boolean isTextFieldDoubleInRange(JTextField txtField, double min, double max)
        {
            if (isTextFieldDouble(txtField))
            {
                double number = Double.parseDouble(txtField.getText());
                if (number<min||number>max)
                {
                    errorMessage("The entered "+txtField.getName()+" value is not in the range of "+min+" to "+max+"!\nPlease try again.", txtField);
                    return false;
                }
                return true;
            }
            return false;
        }
        
        public static boolean isTextFieldName(JTextField txtField)
        {
            if (!isTextFieldEmpty(txtField))
            {
                //Pattern pattern = Pattern.compile("[a-zA-Z]+");
                if (!Pattern.matches("[a-zA-Z]+", txtField.getText()))
                {
                    errorMessage("The entered "+txtField.getName()+" contains non letter characters!\nPlease try again.", txtField);
                    return false;
                }
                return true;
            }
            return false;
        }
        
        public static boolean isTextFieldEmail(JTextField txtField)
        {
            if (!isTextFieldEmpty(txtField))
            {
                if (!Pattern.matches("^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$",txtField.getText()))
                {
                    errorMessage("The entered "+txtField.getName()+" is not valid!\nPlease try again.", txtField);
                    return false;
                }
                return true;
            }
            return false;
        }
        
        public static boolean isTextFieldWord(JTextField txtField)
        {
            if (!isTextFieldEmpty(txtField))
            {
                if (!Pattern.matches("\\w+", txtField.getText()))
                {
                    errorMessage("The entered "+txtField.getName()+" is not valid!\nPlease try again.", txtField);
                    return false;
                }
                return true;
            }
            return false;
        }
        
        public static boolean isTextFieldMatchingPattern(JTextField txtField, String pattern, String visualPattern)
        {
            if (!isTextFieldEmpty(txtField))
            {
                if (!Pattern.matches(pattern, txtField.getText()))
                {
                    errorMessage("This field should be in the pattern "+visualPattern+".", txtField);
                    return false;
                }
                return true;
            }    
            return false;    
        }
        
        
        public static boolean TryParseInt(String number)
        {
            try {
                Integer.parseInt(number);
                return true;
            } catch (NumberFormatException nfe) {
                return false;
            }
            
        }
        
        public static boolean TryParseDouble(String number)
        {
            try {
                Double.parseDouble(number);
                return true;
            } catch (NumberFormatException nfe) {
                return false;
            }
            
        }
    
}    

