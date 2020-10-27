package io.sandbox;

import org.openrewrite.java.ChangeFieldName;
import org.openrewrite.java.JavaIsoRefactorVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;

import java.util.function.Predicate;

import static org.openrewrite.java.tree.TypeUtils.isOfClassType;

/**
 * A {@link JavaIsoRefactorVisitor} transforming "x" and "y" fields IFF the class is named ChangeMyFieldsClass.
 * If the above conditions pass, "x" and "y" are renamed to "one" and "two." Note that the presence of either an "x" or
 * "y" field will result in a refactoring.
 */
public class FieldNameChangingVisitor extends JavaIsoRefactorVisitor {

    private static final String TARGET_CLASS = "io.sandbox.ChangeMyFieldsClass";

    private static final String TARGET_TYPE = "java.lang.Integer";

    private final Predicate<String> isX = "x"::equals;

    private final Predicate<String> isY = "y"::equals;

    @Override
    public J.ClassDecl visitClassDecl(J.ClassDecl clazz) {

        if (isOfClassType(clazz.getType(), TARGET_CLASS)) {
            clazz.getFields().stream()
                    .flatMap(variableDecls -> variableDecls.getVars().stream())
                    .map(J.VariableDecls.NamedVar::getSimpleName)
                    .filter(isX.or(isY))
                    .forEach(n -> {
                        String toName = isX.test(n) ? "one" : "two";
                        andThen(
                                new ChangeFieldName.Scoped(
                                        JavaType.Class.build(TARGET_TYPE),
                                        n,  // the current name
                                        toName // the desired name
                                ));
                    });
        }
        return super.visitClassDecl(clazz);
    }
}
