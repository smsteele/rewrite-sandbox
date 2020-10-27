package io.sandbox

import org.junit.jupiter.api.Test
import org.openrewrite.RefactorVisitor
import org.openrewrite.RefactorVisitorTestForParser
import org.openrewrite.java.JavaParser
import org.openrewrite.java.tree.J

class UUIDFieldAddingVisitorTest : RefactorVisitorTestForParser<J.CompilationUnit> {
    override val parser: JavaParser = JavaParser.fromJavaVersion()
            .build()

    override val visitors: Iterable<RefactorVisitor<*>> = listOf(UUIDFieldAddingVisitor())

    @Test
    fun testVisitClassDeclAddingUUIDField() = assertRefactored(
            before = """
                package org.scratchpad;
                
                import lombok.AccessLevel;
                import lombok.experimental.FieldDefaults;
                
                @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
                public class ImmaClass {
                    int x = 1;
                    int y = 2;
                
                    public int sumOfItsParts() {
                        return x + y;
                    }
                }
            """,
            after = """
                package org.scratchpad;

                import lombok.AccessLevel;
                import lombok.experimental.FieldDefaults;
                
                import java.util.UUID;
                
                @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
                public class ImmaClass {
                    private static final UUID ADDED_ID = UUID.randomUUID();
                    int x = 1;
                    int y = 2;
                
                    public int sumOfItsParts() {
                        return x + y;
                    }
                
                    public UUID getADDED_ID() {
                        return ADDED_ID;
                    }
                }
            """
    )
}
