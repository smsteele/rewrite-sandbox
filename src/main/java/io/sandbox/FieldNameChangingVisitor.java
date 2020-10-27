package io.sandbox;

import org.openrewrite.java.ChangeFieldName;
import org.openrewrite.java.JavaIsoRefactorVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;

import static org.openrewrite.java.tree.TypeUtils.isOfClassType;

public class FieldNameChangingVisitor extends JavaIsoRefactorVisitor {

    private static final String TARGET_CLASS = "io.sandbox.ChangeMyFieldsClass";

    private static final String TARGET_TYPE = "java.lang.Integer";


    @Override
    public J.ClassDecl visitClassDecl(J.ClassDecl clazz) {

        if (isOfClassType(clazz.getType(), TARGET_CLASS)) {
            clazz.getFields().stream()
                    .flatMap(variableDecls -> variableDecls.getVars().stream())
                    .map(J.VariableDecls.NamedVar::getSimpleName)
                    .filter(n -> "x".equals(n) || "y".equals(n))
                    .forEach(n -> {
                        String toName = "x".equals(n) ? "one" : "two";
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
