/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 *
 * @author Nursalim
 */
public class CurrencyUtils {
    public String formatRupiah(BigDecimal nominal){
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        
        
        return kursIndonesia.format(nominal);
    }
    
    public String unFormatRupiah(String nominalFormat){        
        return nominalFormat.replaceAll("Rp. ", "").replaceAll(",00", "").replaceAll("\\.", "");
    }
    
    public boolean isNumeric(String number){
        if (number == null) {
            return false;
        }
        try {
            BigInteger d = new BigInteger(number);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
