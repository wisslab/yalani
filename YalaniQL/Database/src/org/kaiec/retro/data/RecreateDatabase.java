/*
 * This file is part of Semtinel (http://www.semtinel.org).
 * Copyright (c) 2007-2010 Kai Eckert (http://www.kaiec.org).
 * 
 * Semtinel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Semtinel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Semtinel.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package org.kaiec.retro.data;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.hibernate.Session;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public final class RecreateDatabase implements ActionListener {

        private static boolean lucene = true;
        private static boolean fulltext = false;

    public void actionPerformed(ActionEvent e) {
        recreate();
    }


    public static void recreate() {
       HibernateUtil hibernateUtil = HibernateUtil.getInstance();
            SchemaExport export = new SchemaExport(hibernateUtil.getConfig());
            export.create(false, true);
            hibernateUtil.getSession().clear();
            RecordList.getInstance().datachanged();


            // Fulltext
            if (fulltext && lucene) {
                createFTLucene(hibernateUtil.getSession());
            } else if (fulltext) {
                createFT(hibernateUtil.getSession());
            }
    }


    public static void createFT(Session session) {
        session.createSQLQuery("CREATE ALIAS IF NOT EXISTS FT_INIT FOR \"org.h2.fulltext.FullText.init\"").executeUpdate();
        session.createSQLQuery("CALL FT_INIT()").executeUpdate();
        session.createSQLQuery("CALL FT_CREATE_INDEX('PUBLIC', 'RVK', 'DESCRIPTION')").executeUpdate();

    }

    public static void createFTLucene(Session session) {
        if (!fulltext) return;
        session.createSQLQuery("CREATE ALIAS IF NOT EXISTS FTL_INIT FOR \"org.h2.fulltext.FullTextLucene.init\"").executeUpdate();
        session.createSQLQuery("CALL FTL_INIT()").executeUpdate();
        session.createSQLQuery("CALL FTL_CREATE_INDEX('PUBLIC', 'RVK', 'DESCRIPTION')").executeUpdate();
    }

    public static String getFTQuery(String var) {
        if (!fulltext) return null;
        return "SELECT r.notation FROM FT_SEARCH_DATA(:"+var+", 0, 0) FT, RVK r WHERE FT.TABLE='RVK' AND r.ID=FT.KEYS[0];";
    }

    public static String getFTLuceneQuery(String var) {
        if (!fulltext) return null;
        return "SELECT r.notation FROM FTL_SEARCH_DATA(:"+var+", 0, 0) FT, RVK r WHERE FT.TABLE='RVK' AND r.ID=FT.KEYS[0];";
    }

    public static String getFulltextQuery(String var) {
        if (fulltext && lucene) return getFTLuceneQuery(var);
        if (fulltext) return getFTQuery(var);
        return null;
    }


}
