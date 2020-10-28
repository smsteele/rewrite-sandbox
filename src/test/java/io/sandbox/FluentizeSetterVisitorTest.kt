package io.sandbox

import org.junit.jupiter.api.Test
import org.openrewrite.RefactorVisitor
import org.openrewrite.RefactorVisitorTestForParser
import org.openrewrite.java.JavaParser
import org.openrewrite.java.tree.J

class FluentizeSetterVisitorTest : RefactorVisitorTestForParser<J.CompilationUnit> {
    override val parser: JavaParser = JavaParser.fromJavaVersion()
            .build()

    override val visitors: Iterable<RefactorVisitor<*>> = listOf(FluentizeSetterVisitor())

    @Test
    fun test() {
        var clarse: String =
                """
                    class A {
                        A foo() {
                           return this;
                        }
                    }
                """.trimIndent()
        val parse = parser.parse(clarse)
        parse.toString();
    }

    @Test
    fun testVisitFluentizingSetterField() = assertRefactored(
            before = """
                class Foo {
                    String bar;
                    void setBar(String value) {
                        bar = value;
                    }
                }
            """,
            after = """
                class Foo {
                    String bar;
                    Foo setBar(String value) {
                        bar = value;
                        return this;
                    }
                }
            """
    )

    @Test
    fun testVisitFluentizingSetterFieldAlreadyFluent() = assertUnchanged(
            before = """
                class Foo {
                    String bar;
                    Foo setBar(String value) {
                        bar = value;
                        return this;
                    }
                }
            """
    )
}
