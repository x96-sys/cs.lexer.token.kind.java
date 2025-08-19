package org.x96.sys.foundation.cs.lexer.token;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BuildInfoTest {

    @Test
    public void testGetFullVersion() {
        String fullVersion = BuildInfo.getFullVersion();
        assertNotNull(fullVersion);
        assertTrue(fullVersion.contains(BuildInfo.VERSION));
        assertTrue(fullVersion.contains(BuildInfo.GIT_REVISION));
        // Formato esperado: "VERSION (GIT_REVISION)"
        String expected = BuildInfo.VERSION + " (" + BuildInfo.GIT_REVISION + ")";
        assertEquals(expected, fullVersion);
    }

    @Test
    public void testGetBuildSummary() {
        String summary = BuildInfo.getBuildSummary();
        assertNotNull(summary);

        // Verifica se todos os componentes estão presentes no summary
        assertTrue(summary.contains("Version: " + BuildInfo.VERSION));
        assertTrue(summary.contains("Revision: " + BuildInfo.GIT_REVISION));
        assertTrue(summary.contains("Built: " + BuildInfo.BUILD_TIMESTAMP));
        assertTrue(summary.contains("by " + BuildInfo.BUILD_USER + "@" + BuildInfo.BUILD_HOST));
        assertTrue(summary.contains("OS: " + BuildInfo.BUILD_OS));
        assertTrue(summary.contains("Ruby: " + BuildInfo.BUILD_RUBY));
        assertTrue(summary.contains("Java: " + BuildInfo.BUILD_JAVA));
    }

    @Test
    public void testConstants() {
        // Testa se as constantes não são nulas/vazias
        assertNotNull(BuildInfo.VERSION);
        assertFalse(BuildInfo.VERSION.isEmpty());

        assertTrue(BuildInfo.VERSION_MAJOR >= 0);
        assertTrue(BuildInfo.VERSION_MINOR >= 0);
        assertTrue(BuildInfo.VERSION_PATCH >= 0);

        assertNotNull(BuildInfo.GIT_REVISION);
        assertNotNull(BuildInfo.BUILD_TIMESTAMP);
        assertNotNull(BuildInfo.BUILD_USER);
        assertNotNull(BuildInfo.BUILD_HOST);
        assertNotNull(BuildInfo.BUILD_OS);
        assertNotNull(BuildInfo.BUILD_RUBY);
        assertNotNull(BuildInfo.BUILD_JAVA);
    }

    @Test
    public void testVersionNumbers() {
        // Verifica se a versão string corresponde aos números
        String[] versionParts = BuildInfo.VERSION.split("\\.");
        assertEquals(3, versionParts.length, "Versão deve ter formato x.y.z");

        assertEquals(Integer.parseInt(versionParts[0]), BuildInfo.VERSION_MAJOR);
        assertEquals(Integer.parseInt(versionParts[1]), BuildInfo.VERSION_MINOR);
        assertEquals(Integer.parseInt(versionParts[2]), BuildInfo.VERSION_PATCH);
    }

    @Test
    void testFieldsNotEmpty() {
        assertNotNull(BuildInfo.VERSION, "VERSION não pode ser nulo");
        assertFalse(BuildInfo.VERSION.isEmpty(), "VERSION não pode ser vazio");

        assertNotNull(BuildInfo.GIT_REVISION, "GIT_REVISION não pode ser nulo");
        assertFalse(BuildInfo.GIT_REVISION.isEmpty(), "GIT_REVISION não pode ser vazio");

        assertNotNull(BuildInfo.BUILD_TIMESTAMP, "BUILD_TIMESTAMP não pode ser nulo");
        assertFalse(BuildInfo.BUILD_TIMESTAMP.isEmpty(), "BUILD_TIMESTAMP não pode ser vazio");

        assertNotNull(BuildInfo.BUILD_USER, "BUILD_USER não pode ser nulo");
        assertFalse(BuildInfo.BUILD_USER.isEmpty(), "BUILD_USER não pode ser vazio");
    }

    @Test
    void testFullVersion() {
        String fullVersion = BuildInfo.getFullVersion();
        assertNotNull(fullVersion, "getFullVersion() não pode ser nulo");
        assertTrue(fullVersion.contains(BuildInfo.VERSION), "getFullVersion() deve conter VERSION");
        assertTrue(
                fullVersion.contains(BuildInfo.GIT_REVISION),
                "getFullVersion() deve conter GIT_REVISION");
    }

    @Test
    void testBuildSummary() {
        String summary = BuildInfo.getBuildSummary();
        assertNotNull(summary, "getBuildSummary() não pode ser nulo");
        assertFalse(summary.isEmpty(), "getBuildSummary() não pode ser vazio");

        // Opcional: checa que contém campos conhecidos
        assertTrue(summary.contains(BuildInfo.VERSION), "Summary deve conter VERSION");
        assertTrue(summary.contains(BuildInfo.GIT_REVISION), "Summary deve conter GIT_REVISION");
        assertTrue(
                summary.contains(BuildInfo.BUILD_TIMESTAMP), "Summary deve conter BUILD_TIMESTAMP");
    }
}
