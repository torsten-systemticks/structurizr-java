package com.structurizr.component.matcher;

import com.structurizr.component.Type;
import com.structurizr.util.StringUtils;
import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Matches types where the type extends the specified class.
 */
public class ExtendsTypeMatcher implements TypeMatcher {

    private static final Log log = LogFactory.getLog(ExtendsTypeMatcher.class);

    private final String className;

    public ExtendsTypeMatcher(String className) {
        if (StringUtils.isNullOrEmpty(className)) {
            throw new IllegalArgumentException("A fully qualified class name must be supplied");
        }

        this.className = className;
    }

    @Override
    public boolean matches(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("A type must be specified");
        }

        if (type.getJavaClass() == null) {
            return false;
        }

        JavaClass javaClass = type.getJavaClass();
        try {
            Set<String> superClasses = Stream.of(javaClass.getSuperClasses()).map(JavaClass::getClassName).collect(Collectors.toSet());
            return superClasses.contains(className);
        } catch (ClassNotFoundException e) {
            log.warn("Cannot find super classes of " + type.getFullyQualifiedName(), e);
        }

        return false;
    }

    @Override
    public String toString() {
        return "ExtendsTypeMatcher{" +
                "className='" + className + '\'' +
                '}';
    }

}