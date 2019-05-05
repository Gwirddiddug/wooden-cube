package org.kaa.utils;

import org.kaa.model.RealSpace;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Gwirggiddug on 05.06.2016.
 */
public class ClassUtils {
    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static <T extends RealSpace> Class<T>[] getClasses(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = null;
        try {
            resources = classLoader.getResources(path);
        } catch (IOException e) {

            //we'll get empty list
        }

        List<File> dirs = new ArrayList<>();
        while (resources!=null && resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }

        List<Class<T>> classes = new ArrayList<>();
        try {
            for (File directory : dirs) {
                List<Class<T>> list = findClasses(directory, packageName).stream()
                        .filter(cls -> cls.isInstance(RealSpace.class))
                        .map(cls -> (Class<T>) cls)
                        .collect(Collectors.toList());
                classes.addAll(list);
            }
        } catch (ClassNotFoundException e) {
            //we'll get empty list
        }
        Class[] result = new Class[classes.size()];
        Arrays.stream(result).map(cls -> (Class<T>) cls);
        return classes.toArray(result);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    public static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
