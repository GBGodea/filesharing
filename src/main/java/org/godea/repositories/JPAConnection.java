package org.godea.repositories;

import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.godea.di.EntityInjector;
import org.godea.di.Injector;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@WebListener
public class JPAConnection implements ServletContextListener {
    private EntityManagerFactory entityManager;

    public void contextInitialized(ServletContextEvent event) {
        InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("application.yml");

        System.out.println(inputStream);

        if (inputStream == null) {
            event.getServletContext().log("Cannot find yml");
            return;
        }

        Yaml yml = new Yaml();
        Map<String, Object> data = yml.load(inputStream);
        @SuppressWarnings("unchecked")
        Map<String, String> ymlSource = (Map<String, String>) data.get("jdbc");

        Map<String, Object> jpaConnect = new HashMap<>();
        jpaConnect.put("jakarta.persistence.jdbc.url", ymlSource.get("url"));
        jpaConnect.put("jakarta.persistence.jdbc.user", ymlSource.get("username"));
        jpaConnect.put("jakarta.persistence.jdbc.password", ymlSource.get("password"));
        jpaConnect.put("jakarta.persistence.jdbc.driver", ymlSource.get("driver"));
        jpaConnect.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        jpaConnect.put("hibernate.hbm2ddl.auto", "update");

        StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .applySettings(jpaConnect)
                        .build();

        MetadataSources sources = new MetadataSources(registry);

        new EntityInjector().inject().forEach(sources::addAnnotatedClass);

        Metadata metadata = sources.getMetadataBuilder().build();
        var sessionFactory = metadata.getSessionFactoryBuilder().build();

        entityManager = sessionFactory.unwrap(EntityManagerFactory.class);
        EMFProvider.setFactory(entityManager);
        Injector.initialize("org.godea");

        event.getServletContext().setAttribute("entityManager", entityManager);
        event.getServletContext().log("Database connected");
        System.out.println("Database connected");
    }

    public void contextDestroyed(ServletContextEvent event) {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
            event.getServletContext().log("Database connection closed");
        }
    }
}
