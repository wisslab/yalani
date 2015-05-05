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

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.swing.event.ChangeListener;
import org.hibernate.annotations.Index;
import org.openide.util.ChangeSupport;



/**
 *
 * @author Kai Eckert (http://www.kaiec.org)
 */

@Entity
public class Record {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id = -1l;

    @Index(name="ppn")
    private String ppn;

    @Index(name="extid")
    private String externalId;

    @Lob
    private String creators;
    @Lob
    private String title;
    private String year;
    @Lob
    private String subjects;
    @Lob
    private String classes;
    private String assignment;
    @Lob
    private String isbn;
    private String signature;
    private boolean context;
    private String edition;
    private String series;
    private Date updated;
    private String language;

    

    

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    

    @Transient
    private ChangeSupport cs = new ChangeSupport(this);
    public void addAssignmentChangeListener(ChangeListener listener) {
        cs.addChangeListener(listener);
    }
    public void removeAssignmentChangeListener(ChangeListener listener) {
        cs.removeChangeListener(listener);
    }

    public boolean isContext() {
        return context;
    }

    public void setContext(boolean context) {
        this.context = context;
    }
    

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        setAssignment(assignment, false);
    }

    public void setAssignment(String assignment, boolean bulk) {
        String old = this.assignment;
        this.assignment = assignment;
        if (!bulk && assignment!=null && !assignment.equals(old) && !assignment.isEmpty()) {
            this.updated = new Date();
            EventLog event = EventLog.createEvent(EventLog.ASSIGNMENT);
            event.setExternalId(this.getExternalId());
            event.setMessage(assignment);
        }
        cs.fireChange();
    }


    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getCreators() {
        return creators;
    }

    public void setCreators(String creators) {
        this.creators = creators;
    }

    public String getPpn() {
        return ppn;
    }

    public void setPpn(String ppn) {
        this.ppn = ppn;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getFirstAuthorLastname() {
        int komma = getCreators().indexOf(",");
        if (komma==-1) return getCreators();
        return getCreators().substring(0,komma);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Record other = (Record) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.ppn == null) ? (other.ppn != null) : !this.ppn.equals(other.ppn)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 79 * hash + (this.ppn != null ? this.ppn.hashCode() : 0);
        hash = 79 * hash + (this.externalId != null ? this.externalId.hashCode() : 0);
        return hash;
    }

    
}
