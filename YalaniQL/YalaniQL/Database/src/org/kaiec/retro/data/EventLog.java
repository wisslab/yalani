/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kaiec.retro.data;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author kai
 */
@Entity
public class EventLog {

    public static final String APPSTART = "APPSTART";
    public static final String APPEND = "APPEND";
    public static final String LINK = "LINK";
    public static final String PICKLIST = "PICKLIST";
    public static final String LOSTFOCUS = "LOSTFOCUS";
    public static final String GOTFOCUS = "GOTFOCUS";
    public static final String ASSIGNMENT = "ASSIGNMENT";
    public static final String ASS_RVK = "ASS_RVK";
    public static final String ASS_DROPDOWN = "ASS_DROPDOWN";
    public static final String ASS_EDIT = "ASS_EDIT";
    public static final String ASS_PICK = "ASS_PICK";
    public static final String ASS_MASS = "ASS_MASS";
    public static final String FILTER_NEW = "FILTER_NEW";
    public static final String FILTER_ON = "FILTER_ON";
    public static final String FILTER_OFF = "FILTER_OFF";
    public static final String CONTEXT_ON = "CONTEXT_ON";
    public static final String CONTEXT_OFF = "CONTEXT_OFF";
    public static final String OTHER = "OTHER";

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Date timestamp;
    private String externalId;
    private String type;
    private String type2;
    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public static EventLog createEvent(String type) {
        EventLog event = new EventLog();
        event.setType(type);
        event.setTimestamp(new Date());
        try {
            HibernateUtil.getInstance().getSession().save(event);
        } catch (Exception e) {
            System.out.println("Could not write to DB, is it already created?");
            HibernateUtil.getInstance().throwSessionAway();
        }
        return event;
    }

}
