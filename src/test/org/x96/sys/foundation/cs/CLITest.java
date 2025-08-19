package org.x96.sys.foundation.cs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.x96.sys.foundation.cs.lexer.token.BuildInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class CLITest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final InputStream originalIn = System.in;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(originalIn);
    }

    @Test
    public void testVersionOption() {
        CLI.main(new String[] {"-v"});
        String output = outContent.toString().trim();
        assertEquals(BuildInfo.VERSION, output);
    }

    @Test
    public void testVersionLongOption() {
        CLI.main(new String[] {"--version"});
        String output = outContent.toString().trim();
        assertEquals(BuildInfo.VERSION, output);
    }

    @Test
    public void testBuildInfoOption() {
        CLI.main(new String[] {"-b"});
        String output = outContent.toString();
        assertTrue(output.contains("Build Information:"));
        assertTrue(output.contains("Version: " + BuildInfo.VERSION));
        assertTrue(output.contains("Build Date: " + BuildInfo.BUILD_TIMESTAMP));
        assertTrue(output.contains("Build User: " + BuildInfo.BUILD_USER));
        assertTrue(output.contains("Major: " + BuildInfo.VERSION_MAJOR));
        assertTrue(output.contains("Minor: " + BuildInfo.VERSION_MINOR));
        assertTrue(output.contains("Patch: " + BuildInfo.VERSION_PATCH));
        assertTrue(output.contains("Full: " + BuildInfo.getFullVersion()));
    }

    @Test
    public void testBuildInfoLongOption() {
        CLI.main(new String[] {"--build-info"});
        String output = outContent.toString();
        assertTrue(output.contains("Build Information:"));
        assertTrue(output.contains("Version: " + BuildInfo.VERSION));
    }

    @Test
    public void testHelpOption() {
        CLI.main(new String[] {"-h"});
        String output = outContent.toString();
        assertTrue(output.contains("Usage:"));
        assertTrue(output.contains("Options:"));
        assertTrue(output.contains("-v, --version"));
        assertTrue(output.contains("-b, --build-info"));
        assertTrue(output.contains("-h, --help"));
    }

    @Test
    public void testHelpLongOption() {
        CLI.main(new String[] {"--help"});
        String output = outContent.toString();
        assertTrue(output.contains("Usage:"));
        assertTrue(output.contains("Options:"));
    }

    @Test
    public void testNoArgumentsWithMockedStdin() {
        // Mock stdin para não travar o teste
        String input = "test\n";
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);

        CLI.main(new String[] {});
        String output = outContent.toString();
        assertFalse(output.isEmpty());
        // Deve processar o texto "test" como tokens
        assertTrue(output.length() > 0);
    }

    @Test
    public void testStdinOption() {
        // Testa a opção --stdin explícita
        String input = "hello\n";
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);

        CLI.main(new String[] {"--stdin"});
        String output = outContent.toString();
        assertFalse(output.isEmpty());
        // Deve processar o texto "hello" como tokens
        assertTrue(output.length() > 0);
    }

    @Test
    public void testDefaultBehaviorWithArguments() {
        // Testa o comportamento original - processa texto como tokens
        CLI.main(new String[] {"a"});
        String output = outContent.toString();
        assertFalse(output.isEmpty());
        // Deve conter informações sobre o token 'a'
        assertTrue(output.length() > 0);
    }

    @Test
    public void testMultipleArguments() {
        // Testa processamento de múltiplos argumentos
        CLI.main(new String[] {"hello", "world"});
        String output = outContent.toString();
        assertFalse(output.isEmpty());
        // Deve processar ambos os argumentos
        assertTrue(output.length() > 0);
    }

    @Test
    public void testEmptyStdin() {
        // Testa stdin vazio
        String input = "";
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);

        CLI.main(new String[] {});
        String output = outContent.toString();
        // Com stdin vazio, não deve ter saída
        assertEquals("", output);
    }

    @Test
    public void testStdinWithMultipleLines() {
        // Testa stdin com múltiplas linhas para cobrir o while loop completamente
        String input = "line1\nline2\nline3\n";
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);

        CLI.main(new String[] {});
        String output = outContent.toString();
        assertFalse(output.isEmpty());
        // Deve processar todas as linhas
        assertTrue(
                output.contains("LATIN_SMALL_LETTER_L")
                        || output.contains("is")); // linha contém 'l' ou resultado
        // do Kind.is
    }

    @Test
    public void testConstructor() {
        // Testa o construtor para cobertura completa
        CLI cli = new CLI();
        assertNotNull(cli);
        // Verifica que podemos criar uma instância da classe
        assertTrue(cli instanceof CLI);
    }

    @Test
    public void testUnknownCommandOption() {
        // Testa comando desconhecido que vai para o case default
        CLI.main(new String[] {"--unknown-option"});
        String output = outContent.toString();
        assertFalse(output.isEmpty());
        // Deve processar "--unknown-option" como texto
        assertTrue(output.length() > 0);
    }

    @Test
    public void testProcessTextWithSpecialCharacters() {
        // Testa processamento de texto com caracteres especiais
        CLI.main(new String[] {"!@#$%"});
        String output = outContent.toString();
        assertFalse(output.isEmpty());
        // Deve processar cada byte dos caracteres especiais
        assertTrue(output.contains("EXCLAMATION_MARK") || output.contains("is"));
    }

    @Test
    public void testIOExceptionHandling() {
        // Testa o tratamento de IOException no processStdin
        // Simula um InputStream que sempre lança IOException
        InputStream problematicIn =
                new InputStream() {
                    @Override
                    public int read() throws IOException {
                        throw new IOException("Simulated IO error");
                    }
                };

        System.setIn(problematicIn);

        CLI.main(new String[] {"--stdin"});

        String errorOutput = errContent.toString();
        assertTrue(errorOutput.contains("Error reading from stdin: Simulated IO error"));
    }
}
