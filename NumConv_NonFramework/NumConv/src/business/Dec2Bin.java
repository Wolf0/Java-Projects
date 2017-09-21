
package business;

import java.util.ArrayList;

/**
 *
 * @author: 
 */
public class Dec2Bin {
    private String result;
    private ArrayList<String> resultsteps;
    private String emsg;
    
    public Dec2Bin(String value) {
        emsg = "";
        try {
           long n = Long.parseLong(value);
           if (n < 0 ) {
               emsg = "Bad decimal value: must be positive.";
           } else {
              resultsteps = new ArrayList<>();
              this.result = "";
              convertByRecur(n);
           }
        } catch (NumberFormatException e) {
           emsg = "Illegal value: not a decimal integer";
        }
    }
    private void convertByRecur(long n) {
        int r = 0;
        
        r = (int)(n % 2);
        long newval = n / 2;
        resultsteps.add(n + " divided by 2 = " + newval + " w/remainder of: "  +r);
        
        if (newval > 0) {
            convertByRecur(newval);
        } 
        this.result += String.valueOf(r);
    }
    public String getResult() {
        return this.result;
    }
    public ArrayList<String> getResultSteps() {
        return this.resultsteps;
    }
    public String getErrorMsg() {
        return this.emsg;
    }
}
