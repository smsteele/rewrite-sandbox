package io.sandbox;

import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.mockito.Mockito.*;

class GoogleCloudFunctionTest {

    private final String beforeUUIDAdded = "package org.scratchpad;\n" +
            "\n" +
            "import lombok.AccessLevel;\n" +
            "import lombok.experimental.FieldDefaults;\n" +
            "\n" +
            "@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)\n" +
            "public class ImmaClass {\n" +
            "    int x = 1;\n" +
            "    int y = 2;\n" +
            "\n" +
            "    public int sumOfItsParts() {\n" +
            "        return x + y;\n" +
            "    }\n" +
            "}";

    private final String afterUUIDAdded = "package org.scratchpad;\n" +
            "\n" +
            "import lombok.AccessLevel;\n" +
            "import lombok.experimental.FieldDefaults;\n" +
            "\n" +
            "import java.util.UUID;\n" +
            "\n" +
            "@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)\n" +
            "public class ImmaClass {\n" +
            "    private static final UUID ADDED_ID = UUID.randomUUID();\n" +
            "    int x = 1;\n" +
            "    int y = 2;\n" +
            "\n" +
            "    public int sumOfItsParts() {\n" +
            "        return x + y;\n" +
            "    }\n" +
            "\n" +
            "    public UUID getADDED_ID() {\n" +
            "        return ADDED_ID;\n" +
            "    }\n" +
            "}";

    private final String beforeFieldChange = "package io.sandbox;\n" +
            "\n" +
            "import lombok.AccessLevel;\n" +
            "import lombok.experimental.FieldDefaults;\n" +
            "\n" +
            "@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)\n" +
            "public class ChangeMyFieldsClass {\n" +
            "    Integer x = 1;\n" +
            "    Integer y = 2;\n" +
            "\n" +
            "    public int sumOfItsParts() {\n" +
            "        return x + y;\n" +
            "    }\n" +
            "}";

    private final String afterFieldChange = "package io.sandbox;\n" +
            "\n" +
            "import lombok.AccessLevel;\n" +
            "import lombok.experimental.FieldDefaults;\n" +
            "\n" +
            "import java.util.UUID;\n" +
            "\n" +
            "@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)\n" +
            "public class ChangeMyFieldsClass {\n" +
            "    private static final UUID ADDED_ID = UUID.randomUUID();\n" +
            "    Integer one = 1;\n" +
            "    Integer two = 2;\n" +
            "\n" +
            "    public int sumOfItsParts() {\n" +
            "        return one + two;\n" +
            "    }\n" +
            "\n" +
            "    public UUID getADDED_ID() {\n" +
            "        return ADDED_ID;\n" +
            "    }\n" +
            "}";

    @Test
    void testServiceRefactorsAddingUUID() throws Exception {
        assertRefactored(beforeUUIDAdded, afterUUIDAdded);
    }

    @Test
    void testServiceRefactorsChangingFields() throws Exception {
        assertRefactored(beforeFieldChange, afterFieldChange);
    }

    private void assertRefactored(String before, String after) throws Exception {
        HttpRequest request = mock(HttpRequest.class);
        HttpResponse response = mock(HttpResponse.class);
        BufferedWriter bufferedWriter = mock(BufferedWriter.class);
        InputStream inputStream = new ByteArrayInputStream(before.getBytes());

        doReturn(inputStream).when(request).getInputStream();
        doReturn(bufferedWriter).when(response).getWriter();

        new GoogleCloudFunction().service(request, response);

        verify(response).getWriter();
        verify(bufferedWriter).write(after);
    }
}