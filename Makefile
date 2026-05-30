JAVA_HOME := $(HOME)/.sdkman/candidates/java/21.0.8-tem
export JAVA_HOME

run:
	mvn spring-boot:run

build:
	mvn compile

test:
	mvn test

clean:
	mvn clean
