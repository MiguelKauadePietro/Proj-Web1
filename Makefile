export JAVA_HOME := /usr/lib/jvm/java-21-openjdk-amd64
export PATH := $(JAVA_HOME)/bin:$(PATH)

run:
	mvn spring-boot:run

build:
	mvn compile

test:
	mvn test

clean:
	mvn clean

lint:
	mvn checkstyle:check