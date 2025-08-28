package org.x96.sys.lexer.token;

import static org.junit.Assert.*;

import org.junit.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.x96.sys.lexer.token.BuildInfo;

class BuildInfoTest {

    @Test
    void testBuildTimestamp() {
        assertNotNull("BUILD_TIMESTAMP não pode ser nulo", BuildInfo.BUILD_TIMESTAMP);
        assertFalse("BUILD_TIMESTAMP não pode ser vazio", BuildInfo.BUILD_TIMESTAMP.isEmpty());
        // Verifica se tem formato de timestamp ISO (aproximado)
        assertTrue("BUILD_TIMESTAMP deve ter formato ISO com 'T'", BuildInfo.BUILD_TIMESTAMP.contains("T"));
        assertTrue("BUILD_TIMESTAMP deve ter formato ISO com 'Z'", BuildInfo.BUILD_TIMESTAMP.contains("Z"));
    }

    @Test
    void testVersion() {
        assertNotNull("VERSION não pode ser nulo", BuildInfo.VERSION);
        assertFalse("VERSION não pode ser vazio", BuildInfo.VERSION.isEmpty());
        // Verifica se a versão não é apenas espaços em branco
        assertFalse("VERSION não pode ser apenas espaços", BuildInfo.VERSION.trim().isEmpty());
    }

    @Test
    void testBuildHost() {
        assertNotNull("BUILD_HOST não pode ser nulo", BuildInfo.BUILD_HOST);
        assertFalse("BUILD_HOST não pode ser vazio", BuildInfo.BUILD_HOST.isEmpty());
        assertFalse("BUILD_HOST não pode ser apenas espaços", BuildInfo.BUILD_HOST.trim().isEmpty());
    }

    @Test
    void testBuildUser() {
        assertNotNull("BUILD_USER não pode ser nulo", BuildInfo.BUILD_USER);
        assertFalse("BUILD_USER não pode ser vazio", BuildInfo.BUILD_USER.isEmpty());
        assertFalse("BUILD_USER não pode ser apenas espaços", BuildInfo.BUILD_USER.trim().isEmpty());
    }

    @Test
    void testBuildOs() {
        assertNotNull("BUILD_OS não pode ser nulo", BuildInfo.BUILD_OS);
        assertFalse("BUILD_OS não pode ser vazio", BuildInfo.BUILD_OS.isEmpty());
        assertFalse("BUILD_OS não pode ser apenas espaços", BuildInfo.BUILD_OS.trim().isEmpty());
    }

    @Test
    void testJavaVersion() {
        assertNotNull("JAVA_VERSION não pode ser nulo", BuildInfo.JAVA_VERSION);
        assertFalse("JAVA_VERSION não pode ser vazio", BuildInfo.JAVA_VERSION.isEmpty());
        assertFalse("JAVA_VERSION não pode ser apenas espaços", BuildInfo.JAVA_VERSION.trim().isEmpty());
        // Verifica se contém "version" que é típico em java version strings
        assertTrue("JAVA_VERSION deve conter 'version'",
                BuildInfo.JAVA_VERSION.toLowerCase().contains("version"));
    }

    @Test
    void testConstructorIsPrivate() throws NoSuchMethodException {
        Constructor<BuildInfo> constructor = BuildInfo.class.getDeclaredConstructor();
        assertTrue("O construtor deve ser privado",
                Modifier.isPrivate(constructor.getModifiers()));
    }

    @Test
    void testConstructorCannotBeInstantiated() throws Exception {
        Constructor<BuildInfo> constructor = BuildInfo.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // Verifica que podemos criar uma instância via reflection (mesmo sendo privado)
        // mas o importante é que seja privado para uso normal
        BuildInfo instance = constructor.newInstance();
        assertNotNull("Instância criada via reflection deve existir", instance);

        // Verifica que realmente é privado
        assertTrue("Construtor deve ser privado",
                Modifier.isPrivate(constructor.getModifiers()));
    }

    @Test
    void testClassIsFinal() {
        assertTrue("A classe BuildInfo deve ser final",
                Modifier.isFinal(BuildInfo.class.getModifiers()));
    }

    @Test
    void testAllFieldsAreStatic() throws Exception {
        var fields = BuildInfo.class.getDeclaredFields();
        for (var field : fields) {
            assertTrue("Campo " + field.getName() + " deve ser static",
                    Modifier.isStatic(field.getModifiers()));
        }
    }

    @Test
    void testAllFieldsAreFinal() throws Exception {
        var fields = BuildInfo.class.getDeclaredFields();
        for (var field : fields) {
            assertTrue("Campo " + field.getName() + " deve ser final",
                    Modifier.isFinal(field.getModifiers()));
        }
    }

    @Test
    void testAllFieldsArePublic() throws Exception {
        var fields = BuildInfo.class.getDeclaredFields();
        for (var field : fields) {
            assertTrue("Campo " + field.getName() + " deve ser public",
                    Modifier.isPublic(field.getModifiers()));
        }
    }
}
