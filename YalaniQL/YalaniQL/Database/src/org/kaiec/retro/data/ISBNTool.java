/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kaiec.retro.data;

/**
 *
 * @author kai
 */
public class ISBNTool {
    public boolean checkISBN(String isbn) {
        isbn = trimISBN(isbn);
        if (isbn.length()==10) return checkISBN10(isbn);
        if (isbn.length()==13) return checkISBN13(isbn);
        return false;
    }

    public String convertTo10(String isbn) {
        isbn = trimISBN(isbn);
        if (isbn.length()==10) return isbn;
        if (!isbn.startsWith("978")) return isbn;
        isbn = stripChecksum(isbn);
        isbn = isbn.substring(3, isbn.length());
        return isbn + calculateChecksum10(isbn);

    }

    public String stripChecksum(String isbn) {
        if (isbn.length()==10 || isbn.length()==13) {
            return isbn.substring(0, isbn.length()-1);
        }
        if (isbn.length()==9 || isbn.length()==12) {
            return isbn;
        }
        throw new RuntimeException("Invalid ISBN");
    }
    public String getChecksum(String isbn) {
        if (isbn.length()==10 || isbn.length()==13) {
            return isbn.substring(isbn.length()-1, isbn.length());
        }
        throw new RuntimeException("Invalid ISBN");
    }


    public boolean checkISBN10(String isbn) {
        isbn = trimISBN(isbn);
        if (isbn.length()!=10) return false;
        String cs = getChecksum(isbn);
        String ncs = calculateChecksum10(isbn);
        return cs.equals(ncs);
    }

    public boolean checkISBN13(String isbn) {
        isbn = trimISBN(isbn);
        if (isbn.length()!=13) return false;
        String cs = getChecksum(isbn);
        String ncs = calculateChecksum13(isbn);
        return cs.equals(ncs);
    }

    public String calculateChecksum10(String isbn) {
        isbn = trimISBN(isbn);
        isbn = stripChecksum(isbn);
        if (isbn.length()!=9) throw new RuntimeException("Invalid ISBN: " + isbn);
        int sum = 0;
        for (int i = 0; i < isbn.length();i++) {
            int z = Integer.parseInt(isbn.substring(i, i+1));
            sum+=(i+1) * z;
        }
        int cs = sum%11;
        if (cs==10) return "X";
        return Integer.toString(cs);

    }

    public String calculateChecksum13(String isbn) {
        isbn = trimISBN(isbn);
        isbn = stripChecksum(isbn);
        if (isbn.length()!=12) throw new RuntimeException("Invalid ISBN: " + isbn);
        int sum = 0;
        for (int i = 0; i < isbn.length();i++) {
            try {
                int z = Integer.parseInt(isbn.substring(i, i+1));
                sum+=z*3^(i%2);
            } catch (NumberFormatException nfe) {
                throw new RuntimeException("Invalid ISBN: " + isbn);
            }
        }
        int cs = sum%10;
        return Integer.toString(cs);

    }

    public String trimISBN(String isbn) {
        StringBuffer res = new StringBuffer();
        char[] charArray = isbn.toCharArray();
        for (int i = 0; i < isbn.length(); i++) {
            if (
                    charArray[i]=='0'
                    || charArray[i]=='1'
                    || charArray[i]=='2'
                    || charArray[i]=='3'
                    || charArray[i]=='4'
                    || charArray[i]=='5'
                    || charArray[i]=='6'
                    || charArray[i]=='7'
                    || charArray[i]=='8'
                    || charArray[i]=='9'
                    || charArray[i]=='X'
                    ) {
                res.append(charArray[i]);
            }
        }
        return res.toString();
    }
}
