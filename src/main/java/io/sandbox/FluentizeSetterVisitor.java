package io.sandbox;

import org.openrewrite.Formatting;
import org.openrewrite.Tree;
import org.openrewrite.java.AutoFormat;
import org.openrewrite.java.JavaIsoRefactorVisitor;
import org.openrewrite.java.MethodMatcher;
import org.openrewrite.java.tree.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link JavaIsoRefactorVisitor} that converts traditional pojo setters (i.e with a void return type) into
 * fluent style setters that return this.
 */
public class FluentizeSetterVisitor extends JavaIsoRefactorVisitor {

    public FluentizeSetterVisitor() {
        setCursoringOn();
    }

    @Override
    public J.MethodDecl visitMethod(J.MethodDecl methodDecl) {
        J.ClassDecl classDecl = this.getCursor().firstEnclosingRequired(J.ClassDecl.class);
        String pattern = TypeUtils.asClass(classDecl.getType()).getFullyQualifiedName() + " set*(*)";
        MethodMatcher methodMatcher = new MethodMatcher(pattern);
        // determine if method is setter and already fluent
        // return unchanged if so.
        if (!methodMatcher.matches(methodDecl, classDecl) && !JavaType.Primitive.Void.equals(methodDecl.getReturnTypeExpr().getType())) {
            return methodDecl;
        }
        // otherwise, change setter to be fluent
        andThen(new Scoped(classDecl, methodDecl));
        return super.visitMethod(methodDecl);
    }

    public class Scoped extends JavaIsoRefactorVisitor {

        private final J.ClassDecl classDecl;
        private final J.MethodDecl scope;

        public Scoped(J.ClassDecl classDecl, J.MethodDecl scope) {
            this.classDecl = classDecl;
            this.scope = scope;
        }

        @Override
        public J.MethodDecl visitMethod(J.MethodDecl methodDecl) {
            J.MethodDecl md = super.visitMethod(methodDecl);
            if (scope.isScope(md)) {
                JavaType.Class type = this.classDecl.getType();
                md = md.withReturnTypeExpr(type.toTypeTree());
                J.Block<Statement> body = methodDecl.getBody();
                List<Statement> statements = new ArrayList<>(body.getStatements());
                // get name returns Iden (which is Identity?)
                Expression returnThis = J.Ident.build(Tree.randomId(), "this", this.classDecl.getName().getType(), Formatting.EMPTY);
                statements.add(new J.Return(Tree.randomId(), returnThis, Formatting.EMPTY));
                md = md.withBody(body.withStatements(statements));
                andThen(new AutoFormat(md));
                return md;
            }
            return md;
        }
    }
}
