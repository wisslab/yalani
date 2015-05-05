/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kaiec.retro.data;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;

/**
 *
 * @author kai
 */
@Entity
public class Statistics {

    public static String calcFromUpdated(long breaktime) {
        StringBuilder sb = new StringBuilder();
        sb.append("Neues Intervall nach ");
        sb.append(breaktime);
        sb.append(" Minute(n).\n");
        List<Record> records = new ArrayList<Record>();
        for (Record rec : RecordList.getInstance().getRecords()) {
            if (rec.getUpdated()!=null) records.add(rec);
        }
        Collections.sort(records, new Comparator<Record>() {

            public int compare(Record o1, Record o2) {
                return o1.getUpdated().compareTo(o2.getUpdated());
            }

        });
        Date intervalStart = null;
        Date intervalEnd = null;
        breaktime = breaktime * 60000;
        int count = 0;
        for (Record rec:records) {
            if (rec.getUpdated()==null) continue;
            if (intervalEnd==null || (rec.getUpdated().getTime() - intervalEnd.getTime())>breaktime) {
                if (intervalStart!=null && intervalEnd!=null && count>1) {
                    outputInterval(sb, intervalStart, intervalEnd, count);
                }
                count = 0;
                intervalStart = rec.getUpdated();
            }
            intervalEnd = rec.getUpdated();
            count++;
        }
        if (count>1) outputInterval(sb, intervalStart, intervalEnd, count);

        return sb.toString();
    }

    private static void outputInterval(StringBuilder sb, Date start, Date end, int count) {
                    sb.append(DateFormat.getDateTimeInstance().format(start));
                    sb.append(" - ");
                    sb.append(DateFormat.getDateTimeInstance().format(end));
                    sb.append(": ");
                    sb.append(count);
                    sb.append(" (");
                    long duration = end.getTime() - start.getTime();
                    sb.append(Math.round(((double) count / duration) * 3600000));
                    sb.append("/h)\n");
    }

}
