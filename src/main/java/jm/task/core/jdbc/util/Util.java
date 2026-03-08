/**
 * Используется пул соединений, паттерн Connection pool

 * History version:
 * 2.0.0 added hibernate to use connection to DBMS MySQL
 * 1.0.0 - first release
 */

package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Properties;

public class Util {
    // реализуйте настройку соединения с БД
    private static final String URL = "db.url";
    private static final String USER = "db.username";
    private static final String PASSWORD = "db.password";
    private static final String POOL_SIZE_KEY = "db.pool_size";

    private static final String DIALECT = "hibernate.dialect";
    private static final String DRIVER = "hibernate.driver";

    private static SessionFactory sessionFactory;

    private Util() {
    }

//    =================== Configure for Hibernate ===================
//       --------------------- for  task 1.1.4 -------------------

    private static void initHibernate() {

        try {
            Properties properties = new Properties();

            properties.put(Environment.DRIVER, PropertiesUtil.get(DRIVER));
            properties.put(Environment.URL,PropertiesUtil.get(URL));
            properties.put(Environment.USER,PropertiesUtil.get(USER));
            properties.put(Environment.PASS,PropertiesUtil.get(PASSWORD));
            properties.put(Environment.DIALECT,PropertiesUtil.get(DIALECT));

             //extra settings for hibernate
             //Дополнительное логирование запросов
            properties.put(Environment.SHOW_SQL, "true");

//            properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

            // Автоматическое создание схемы данных. Возможные значения: validate, update, create, create-drope, none
            // ВНИМАНИЕ!!! Выбросит исключение если  будет стоять значение validate, а таблица удалена
            properties.put(Environment.HBM2DDL_AUTO,"update");

            // connection pool settings for Hibernate
            properties.put(Environment.POOL_SIZE, PropertiesUtil.get(POOL_SIZE_KEY));

            Configuration configuration = new Configuration()
                    .setProperties(properties);

            // add Entity classes to know Hibernate about them
//            configuration.addAnnotatedClass(jm.task.core.jdbc.model.User.class);
            configuration.addAnnotatedClass(User.class);

            sessionFactory = configuration.buildSessionFactory();

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Hibernate", e);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            initHibernate();
        }
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

}

