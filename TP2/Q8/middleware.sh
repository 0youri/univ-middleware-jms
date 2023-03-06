#!/bin/bash
javac Receiver.java
jar cvf middleware.jar META-INF/ Receiver.class
asadmin deploy --force middleware.jar