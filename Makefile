# Garante que docker (Rancher Desktop/Homebrew) e mvn estejam no PATH mesmo quando
# o make é iniciado por um shell que não carregou o profile (ex.: pelo IDE).
export PATH := $(HOME)/.rd/bin:/opt/homebrew/bin:/opt/homebrew/sbin:/usr/local/bin:$(PATH)

# Detecta uma JDK 21 de forma portável
# Para forçar uma JDK específica: make run JAVA_HOME=/caminho
JAVA_HOME := $(shell /usr/libexec/java_home -v 21 2>/dev/null || ls -d /usr/lib/jvm/*21*/ 2>/dev/null | head -1 || echo $$JAVA_HOME)
export JAVA_HOME

DOCKER_COMPOSE ?= docker-compose
COMPOSE := $(DOCKER_COMPOSE) -f docker/docker-compose.yml

.PHONY: run build test clean lint db-up db-down db-reset db-logs

db-up:
	$(COMPOSE) up -d
	@echo "Aguardando o MySQL ficar pronto..."
	@until $(COMPOSE) exec -T mysql mysqladmin ping -uroot -proot --silent >/dev/null 2>&1; do \
		sleep 2; \
	done
	@echo "MySQL pronto."

db-down:
	$(COMPOSE) down

db-reset:
	$(COMPOSE) down -v
	$(MAKE) db-up

db-logs:
	$(COMPOSE) logs -f

run: db-up
	mvn spring-boot:run

build:
	mvn compile

test:
	mvn test

clean:
	mvn clean

lint:
	mvn checkstyle:check
