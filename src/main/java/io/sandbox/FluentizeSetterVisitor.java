package io.sandbox;

import org.openrewrite.java.JavaIsoRefactorVisitor;
import org.openrewrite.java.tree.J;

/**
 * A {@link JavaIsoRefactorVisitor} that converts traditional pojo setters (i.e with a void return type) into
 * fluent style setters that return this.
 */
public class FluentizeSetterVisitor extends JavaIsoRefactorVisitor {
    @Override
    public J.ClassDecl visitClassDecl(J.ClassDecl classDecl) {

        return super.visitClassDecl(classDecl);
    }
}
