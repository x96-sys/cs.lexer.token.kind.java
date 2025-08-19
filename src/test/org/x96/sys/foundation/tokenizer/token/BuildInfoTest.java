package org.x96.sys.foundation.tokenizer.token;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildInfoTest {

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
    assertTrue(fullVersion.contains(BuildInfo.GIT_REVISION), "getFullVersion() deve conter GIT_REVISION");
  }

  @Test
  void testBuildSummary() {
    String summary = BuildInfo.getBuildSummary();
    assertNotNull(summary, "getBuildSummary() não pode ser nulo");
    assertFalse(summary.isEmpty(), "getBuildSummary() não pode ser vazio");

    // Opcional: checa que contém campos conhecidos
    assertTrue(summary.contains(BuildInfo.VERSION), "Summary deve conter VERSION");
    assertTrue(summary.contains(BuildInfo.GIT_REVISION), "Summary deve conter GIT_REVISION");
    assertTrue(summary.contains(BuildInfo.BUILD_TIMESTAMP), "Summary deve conter BUILD_TIMESTAMP");
  }
}
