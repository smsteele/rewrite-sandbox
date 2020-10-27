package io.sandbox;

import org.openrewrite.Tree;
import org.openrewrite.java.AddField;
import org.openrewrite.java.GenerateGetter;
import org.openrewrite.java.JavaIsoRefactorVisitor;
import org.openrewrite.java.tree.J;

import java.util.List;

import static org.openrewrite.Formatting.EMPTY;
import static org.openrewrite.Formatting.format;

/**
 * A {@link JavaIsoRefactorVisitor} that adds a {@link java.util.UUID} field and its getter to any class visited.
 */
public class UUIDFieldAddingVisitor extends JavaIsoRefactorVisitor {
    @Override
    public J.ClassDecl visitClassDecl(J.ClassDecl classDecl) {
        andThen(new AddField.Scoped(classDecl,
                List.of(new J.Modifier.Private(Tree.randomId(), EMPTY),
                        new J.Modifier.Static(Tree.randomId(), format(" ")),
                        new J.Modifier.Final(Tree.randomId(), format(" "))),
                "java.util.UUID",
                "ADDED_ID",
                "UUID.randomUUID()"
        ));
        andThen(new GenerateGetter.Scoped(classDecl, "ADDED_ID"));
        return super.visitClassDecl(classDecl);
    }
}
