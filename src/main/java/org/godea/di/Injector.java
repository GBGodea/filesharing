package org.godea.di;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Injector {
    private static final Map<Class<?>, Object> services = new HashMap<>();

    public static void inject(Object target) {
        for(Field field : target.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(Autowired.class)) {
                Class<?> type = field.getType();
                Object service = services.get(type);

                if(service == null) {
                    throw new IllegalStateException("There is no registered bean for " + type);
                }
                field.setAccessible(true);

                try {
                    field.set(target, service);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void initialize(String basePackage) {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forPackage(basePackage))
                        .filterInputsBy(new FilterBuilder().includePackage(basePackage))
                        .setScanners(Scanners.TypesAnnotated)
        );

        Set<Class<?>> beanClasses = new HashSet<>();
        beanClasses.addAll(reflections.getTypesAnnotatedWith(Service.class));

        for(Class<?> clazz : beanClasses) {
            try {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                services.put(clazz, instance);
            } catch (Exception e) {
                throw new RuntimeException("Couldn't create bean " + clazz.getName(), e);
            }
        }
    }
}
