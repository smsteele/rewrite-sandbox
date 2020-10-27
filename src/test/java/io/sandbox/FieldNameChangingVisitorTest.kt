package io.sandbox

import org.junit.jupiter.api.Test
import org.openrewrite.RefactorVisitor
import org.openrewrite.RefactorVisitorTestForParser
import org.openrewrite.java.JavaParser
import org.openrewrite.java.tree.J

class FieldNameChangingVisitorTest : RefactorVisitorTestForParser<J.CompilationUnit> {
    override val parser: JavaParser = JavaParser.fromJavaVersion()
            .build()

    override val visitors: Iterable<RefactorVisitor<*>> = listOf(FieldNameChangingVisitor())

    @Test
    fun testVisitClassDeclChangingFields() = assertRefactored(
            before = """
                package io.sandbox;
                
                import lombok.AccessLevel;
                import lombok.experimental.FieldDefaults;
                
                @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
                public class ChangeMyFieldsClass {
                    Integer x = 1;
                    Integer y = 2;
                
                    public int sumOfItsParts() {
                        return x + y;
                    }
                }
            """,
            after = """
                package io.sandbox;

                import lombok.AccessLevel;
                import lombok.experimental.FieldDefaults;
                
                @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
                public class ChangeMyFieldsClass {
                    Integer one = 1;
                    Integer two = 2;
                
                    public int sumOfItsParts() {
                        return one + two;
                    }
                }
            """
    )

    @Test
    fun testVisitClassDeclChangingFieldsOnlyOneFieldToChange() = assertRefactored(
            before = """
                package io.sandbox;
                
                import lombok.AccessLevel;
                import lombok.experimental.FieldDefaults;
                
                @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
                public class ChangeMyFieldsClass {
                    Integer x = 1;
                
                    public int sumOfItsParts() {
                        return x + 2;
                    }
                }
            """,
            after = """
                package io.sandbox;

                import lombok.AccessLevel;
                import lombok.experimental.FieldDefaults;
                
                @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
                public class ChangeMyFieldsClass {
                    Integer one = 1;
                
                    public int sumOfItsParts() {
                        return one + 2;
                    }
                }
            """
    )

    @Test
    fun testVisitClassDeclChangingFieldsNotTargetClassName() = assertUnchanged(
            before = """
                package io.sandbox;
                
                import lombok.AccessLevel;
                import lombok.experimental.FieldDefaults;
                
                @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
                public class DontChangeMyFieldsClass {
                    Integer x = 1;
                    Integer y = 2;
                
                    public int sumOfItsParts() {
                        return x + y;
                    }
                }
            """
    )


    @Test
    fun testVisitClassDeclChangingFieldsNoFieldsToChange() = assertUnchanged(
            before = """
                package io.sandbox;
                
                import lombok.AccessLevel;
                import lombok.experimental.FieldDefaults;
                
                @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
                public class DontChangeMyFieldsClass {
                
                    public int sumOfItsParts() {
                        return 1 + 2;
                    }
                }
            """
    )
}
