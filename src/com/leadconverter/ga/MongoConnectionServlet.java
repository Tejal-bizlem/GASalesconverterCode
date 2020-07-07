package com.leadconverter.ga;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.db.mongo.ga.MongoDbCache;

public class MongoConnectionServlet implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
       MongoDbCache.getInstance();
    }

    public void contextDestroyed(ServletContextEvent sce) {
       MongoDbCache.getInstance().destroyConnection();
    }

}