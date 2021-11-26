#!/bin/bash
# Compile Client Java Class.
javac Client.java
# Execute the Java Code with hostname ($1) and port ($2).
java Client $1 $2