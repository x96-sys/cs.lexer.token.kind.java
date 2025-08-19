BUILD_DIR     = build
MAIN_BUILD    = $(BUILD_DIR)/main
CLI_BUILD     = $(BUILD_DIR)/cli
TEST_BUILD    = $(BUILD_DIR)/test

TARGET_PATH   = src

SRC_MAIN      = $(TARGET_PATH)/main
SRC_CLI       = $(TARGET_PATH)/cli
SRC_TEST      = $(TARGET_PATH)/test

FOUNDATION      = $(SRC_MAIN)/org/x96/sys/foundation
FOUNDATION_TEST = $(SRC_TEST)/org/x96/sys/foundation

TOOL_DIR      = tools
LIB_DIR       = lib

JUNIT_VERSION = 1.13.4
JUNIT_JAR     = $(TOOL_DIR)/junit-platform-console-standalone.jar
JUNIT_URL     = https://maven.org/maven2/org/junit/platform/junit-platform-console-standalone/$(JUNIT_VERSION)/junit-platform-console-standalone-$(JUNIT_VERSION).jar

JACOCO_VERSION = 0.8.13
JACOCO_CLI     = $(TOOL_DIR)/jacococli.jar
JACOCO_AGENT   = $(TOOL_DIR)/jacocoagent-runtime.jar
JACOCO_BASE    = https://maven.org/maven2/org/jacoco

GJF_VERSION   = 1.28.0
GJF_JAR       = $(TOOL_DIR)/google-java-format.jar
GJF_URL       = https://maven.org/maven2/com/google/googlejavaformat/google-java-format/$(GJF_VERSION)/google-java-format-$(GJF_VERSION)-all-deps.jar

JAVA_SOURCES       = $(shell find $(SRC_MAIN) -name "*.java")
JAVA_TEST_SOURCES  = $(shell find $(SRC_TEST) -name "*.java")

DISTRO_JAR = org.x96.sys.foundation.tokenizer.kind.jar
CLI_JAR = org.x96.sys.foundation.tokenizer.kind.cli.jar

f:
	@mkdir -p $(FOUNDATION)/tokenizer/token/
ft:
	@mkdir -p $(FOUNDATION_TEST)/tokenizer/token/

gen/java/kind: f
	@echo "[🔧] [kind] Generating..."
	@ruby scripts/kind.rb > $(FOUNDATION)/tokenizer/token/Kind.java
	@echo "[✅] [kind] Generated successfully!"

gen/java/kind/info: f
	@echo "[🔧] [kind] Generating..."
	@ruby scripts/build_info.rb > $(FOUNDATION)/tokenizer/token/BuildInfo.java
	@echo "[✅] [kind] Generated successfully!"

gen/java/kind/test: ft
	@echo "[🔧] [kind] Generating KindTest..."
	@ruby scripts/kind_test.rb > $(FOUNDATION_TEST)/tokenizer/token/KindTest.java
	@echo "[✅] [kind] Generated successfully!"

# gen/java/kind gen/java/kind/info
build:
	@echo "[🔨] [build] Compiling..."
	@javac -d $(MAIN_BUILD) $(JAVA_SOURCES)
	@echo "[✅] [build] Compiled successfully! in [$(MAIN_BUILD)]"

#  build
build-cli:
	@echo "[🔨] [build] Compiling..."
	@mkdir -p $(CLI_BUILD)
	@javac -cp $(MAIN_BUILD) -d $(CLI_BUILD) \
	    $(SRC_CLI)/org/x96/sys/foundation/CLI.java
	@echo "[✅] [build] Compiled successfully! in [$(CLI_BUILD)]"

#  build-cli
cli:
	@java -cp $(MAIN_BUILD):$(CLI_BUILD) org.x96.sys.foundation.CLI $(ARGS)

build-test: build build-cli tools/junit
	@echo "[🔨] [build] Compiling..."
	@javac -cp $(MAIN_BUILD):$(CLI_BUILD):$(JUNIT_JAR) \
         -d $(TEST_BUILD) $(JAVA_TEST_SOURCES)
	@echo "[✅] [build-cli] Compiled successfully! in [$(TEST_BUILD)]"

#  build-test
test:
	@java -jar $(JUNIT_JAR) \
     execute \
     --class-path $(TEST_BUILD):$(MAIN_BUILD):$(CLI_BUILD) \
     --scan-class-path

# tools/gjf
format:
	find src -name "*.java" -print0 | xargs -0 java -jar $(GJF_JAR) --aosp --replace

coverage-run: build-test tools/jacoco
	java -javaagent:$(JACOCO_AGENT)=destfile=$(BUILD_DIR)/jacoco.exec \
       -jar $(JUNIT_JAR) \
       execute \
       --class-path $(TEST_BUILD):$(MAIN_BUILD):$(CLI_BUILD) \
       --scan-class-path

coverage-report: tools/jacoco
	java -jar $(JACOCO_CLI) report \
     $(BUILD_DIR)/jacoco.exec \
     --classfiles $(MAIN_BUILD) \
     --classfiles $(CLI_BUILD) \
     --sourcefiles $(SRC_MAIN) \
     --sourcefiles $(SRC_CLI) \
     --html $(BUILD_DIR)/coverage \
     --name "Coverage Report"

coverage: coverage-run coverage-report
	@echo "[✅] [relatório] de cobertura disponível em: build/coverage/index.html"
	@echo "[🌐] [abrir] com: open build/coverage/index.html"

distro:
	@jar cf $(DISTRO_JAR) -C $(MAIN_BUILD) .
	@echo "[📦] [lib] criada com sucesso! $(DISTRO_JAR)"

distro-cli:
	@echo "Main-Class: org.x96.sys.foundation.CLI" > manifest.txt
	@jar cfm $(CLI_JAR) manifest.txt -C $(CLI_BUILD) . -C $(MAIN_BUILD) .
	@rm manifest.txt
	@echo "[📦] [cli] Empacotado com sucesso em $(CLI_JAR)"

tools:
	@mkdir -p tools

tools/junit: tools
	@if [ ! -f $(JUNIT_JAR) ]; then \
       echo "[📦] Baixando JUnit..."; \
       curl -s -L -o $(JUNIT_JAR) $(JUNIT_URL); \
    else \
       echo "[✅] JUnit já está em $(JUNIT_JAR)"; \
    fi

tools/jacoco: tools
	@if [ ! -f $(JACOCO_CLI) ] || [ ! -f $(JACOCO_AGENT) ]; then \
       echo "[📦] Baixando JaCoCo..."; \
       curl -s -L -o $(JACOCO_CLI) $(JACOCO_BASE)/org.jacoco.cli/$(JACOCO_VERSION)/org.jacoco.cli-$(JACOCO_VERSION)-nodeps.jar && \
       curl -s -L -o $(JACOCO_AGENT) $(JACOCO_BASE)/org.jacoco.agent/$(JACOCO_VERSION)/org.jacoco.agent-$(JACOCO_VERSION)-runtime.jar; \
    else \
       echo "[✅] JaCoCo já está em tools/"; \
    fi

tools/gjf:
	@if [ ! -f $(GJF_JAR) ]; then \
      echo "[📦] Baixando Google Java Format..."; \
      curl -s -L -o $(GJF_JAR) $(GJF_URL); \
    else \
      echo "[✅] Google Java Format já está em $(GJF_JAR)"; \
    fi

clean:
	@rm -rf $(FOUNDATION)/tokenizer/token/{BuildInfo,Kind}.java
	@rm -rf $(FOUNDATION_TEST)/tokenizer/token/KindTest.java
	@rm -rf $(BUILD_DIR)
	@rm -rf $(TOOL_DIR)
	@rm -rf $(LIB_DIR)
	@rm -rf *.jar
	@echo "[🧹] [clean] Build directory cleaned."
