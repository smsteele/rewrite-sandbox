package io.sandbox;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import org.openrewrite.*;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class GoogleCloudFunction implements HttpFunction {

    private final Iterable<RefactorVisitor<? extends Tree>> visitors = List.of(
            new UUIDFieldAddingVisitor(),
            new FieldNameChangingVisitor());

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        JavaParser parser = JavaParser.fromJavaVersion().build();
        Refactor refactor = new Refactor().visit(this.visitors);
        String javaCuText = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        List<J.CompilationUnit> parsedCus = parser.reset().parse(javaCuText);
        J.CompilationUnit parsedCu = parsedCus.iterator().next();
        String responseBody = refactor.fix(Collections.singletonList(parsedCu))
                .stream()
                .findAny()
                .map(Change::getFixed)
                .map(SourceFile::print)
                .orElse(parsedCu.print());

        J.CompilationUnit fixed = refactor.fixed(parsedCu);

        assert fixed != null;
        response.getWriter().write(responseBody);
    }
}
