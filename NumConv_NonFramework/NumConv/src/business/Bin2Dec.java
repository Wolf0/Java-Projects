
package business;

import java.util.ArrayList;

/**
 *
 * @author
 */

public class Bin2Dec {
    private String result;
    private ArrayList<String> resultsteps;
    private String emsg;
    
    public Bin2Dec(String value) {
        emsg = "";
        if (isValid(value)) {
            convert(value);
        } else {
            emsg = "Illegal value binary: must be only zeros and ones";
        }
    }
    private boolean isValid(String v) {
        for (int i=0; i< v.length(); i++) {
            if (v.charAt(i) != '1' && v.charAt(i) != '0') {
                return false;
            }
        }
        return true;
    }
    private void convert(String v) {
        int r = 0;
        String reverse = new StringBuilder(v).reverse().toString();
        resultsteps = new ArrayList<>();
        for (int i=0; i < reverse.length(); i++) {
            if (reverse.charAt(i) == '1') {
                int p = (int) Math.pow(2,i);
                r += p;
                resultsteps.add("There is a(n) " + p + " in the number (2^" + i + ")" );
            }
        }
        this.result = String.valueOf(r);
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
