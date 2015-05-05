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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.logging.Logger;

import org.hibernate.*;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private SessionFactory sessionFactory;
    private Configuration config;
    private DatabaseSettings settings;
    private Transaction transaction;
    private Session session;
    private Connection connection;
    private Logger log = Logger.getLogger(getClass().getName());
    private Preferences preferences = new Preferences();
    
    /**
     * 
     * @param settings the DatabaseSettings for initializing the HibernateUtil
     */
    private HibernateUtil() {

        try {
            this.settings = preferences;
            config = new AnnotationConfiguration().configure();
//          config.getProperties().clear();
            config.setProperty("hibernate.connection.url", settings.getConnectionUrl());
//            config.setProperty("hibernate.connection.username","sa");
//            config.setProperty("hibernate.connection.password","hzetrad2341hg");
//            config.setProperty("connection.username","sa");
//            config.setProperty("connection.password","hzetrad2341hg");
//            config.setProperty("connection.username", "sa");
//            config.setProperty("connection.password", "hzetrad2341hg");
//          config.setProperty("connection.driver_class","org.h2.Driver");
//          config.setProperty("connection.url","jdbc:h2:datastore");
//          config.setProperty("connection.username","sa");
//          config.setProperty("connection.password","");
//          config.setProperty("connection.pool_size","1");
//          config.setProperty("dialect","org.hibernate.dialect.H2Dialect");
//          config.setProperty("current_session_context_class","thread");
//          config.setProperty("cache.provider_class","org.hibernate.cache.NoCacheProvider");

            ((AnnotationConfiguration) config).addAnnotatedClass(Record.class);
            ((AnnotationConfiguration) config).addAnnotatedClass(RVK.class);
            ((AnnotationConfiguration) config).addAnnotatedClass(EventLog.class);

            settings.addPropertyChangeListener(new PropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals("databaseLocation")) {
                        DatabaseSettings hs = (DatabaseSettings) evt.getSource();
                        close();
                        
                        if (sessionFactory != null) {
                            sessionFactory.close();
                        }

                        config.setProperty("hibernate.connection.url", hs.getConnectionUrl());
//                        config.setProperty("hibernate.connection.username","sa");
//                        config.setProperty("hibernate.connection.password","hzetrad2341hg");
//                        config.setProperty("connection.username","sa");
//                        config.setProperty("connection.password","hzetrad2341hg");
                        sessionFactory = config.buildSessionFactory();
			log.info("New Database URL: " + hs.getConnectionUrl());
                    }
                }
            
            });

            sessionFactory = config.buildSessionFactory();
            
        } catch (Throwable ex) {

            log.severe(ex.getMessage());
            // throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     *
     * @return the Connection object associated with the current database
     */
    public Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }

            Class.forName(getConfig().getProperty("hibernate.connection.driver_class"));
            connection = DriverManager.getConnection(settings.getConnectionUrl(), 
                    getConfig().getProperty("hibernate.connection.username"), 
                    getConfig().getProperty("hibernate.connection.password"));
//                    "sa","hzetrad2341hg");
            log.fine("Database URL: " + settings.getConnectionUrl());
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException("Database driver not found: " + cnfe, cnfe);            
        } catch (SQLException sqle) {
            throw new RuntimeException("Error opening connection: " + sqle, sqle);
        }
        return connection;
    }

    public void commitTransaction() {
            if (transaction != null && transaction.isActive()) {
                transaction.commit();

            }
            transaction = session.beginTransaction();

    }

    /**
     *
     * @return the current Session
     * @throws HibernateException
     */
    public Session getSession() throws HibernateException {

        if (session == null || !session.isOpen()) {
            if (sessionFactory == null) {
                sessionFactory = config.buildSessionFactory();
            }
            getConnection();
            session = sessionFactory.openSession(connection);
            transaction = session.beginTransaction();
        }

        return session;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public Configuration getConfig() throws HibernateException {
        return config;
    }

    public Connection disconnect() {
        if (session != null && session.isConnected()) {
            if (transaction != null && transaction.isActive()) {
                transaction.commit();
            }

            return session.disconnect();
        }

        return getConnection();
    }

    public void reconnect() {
        if (session != null && !session.isConnected()) {
            session.reconnect(connection);
            transaction = session.beginTransaction();
        }
    }

    public void close() {
        if (session != null) {
            if (transaction != null && transaction.isActive()) {
                transaction.commit();
            }
            session.close();
        }

        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException sqle) {
            throw new RuntimeException("Error closing connection: " + sqle, sqle);
        }
    }

    public void throwSessionAway() {
        if (session != null) {
            session = null;
        }

        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException sqle) {
            throw new RuntimeException("Error closing connection: " + sqle, sqle);
        }
    }

    public Preferences getPreferences() {
        return preferences;
    }


    static HibernateUtil instance;
    public static HibernateUtil getInstance() {
        if (instance==null) instance = new HibernateUtil();
        return instance;
    }
}