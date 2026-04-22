#!/bin/sh
# Maven Wrapper startup script
# See https://maven.apache.org/wrapper/

set -e

if [ -z "$JAVA_HOME" ] ; then
    JAVACMD="java"
else
    JAVACMD="$JAVA_HOME/bin/java"
fi

MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-$(cd "$(dirname "$0")" && pwd)}"
MAVEN_WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
MAVEN_WRAPPER_PROPERTIES="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties"

if [ -r "$MAVEN_WRAPPER_PROPERTIES" ]; then
    MAVEN_URL=$(grep 'distributionUrl' "$MAVEN_WRAPPER_PROPERTIES" | cut -d'=' -f2-)
fi

MAVEN_HOME="$HOME/.m2/wrapper/dists/apache-maven-3.9.6"

if [ ! -d "$MAVEN_HOME" ]; then
    echo "Downloading Maven..."
    mkdir -p "$MAVEN_HOME"
    if command -v curl > /dev/null 2>&1; then
        curl -fsSL "$MAVEN_URL" -o "$MAVEN_HOME/maven.zip"
    elif command -v wget > /dev/null 2>&1; then
        wget -q "$MAVEN_URL" -O "$MAVEN_HOME/maven.zip"
    else
        echo "Error: curl or wget is required to download Maven." >&2
        exit 1
    fi
    unzip -q "$MAVEN_HOME/maven.zip" -d "$MAVEN_HOME"
    rm "$MAVEN_HOME/maven.zip"
fi

MAVEN_BIN=$(find "$MAVEN_HOME" -name 'mvn' -path '*/bin/mvn' | head -1)
exec "$MAVEN_BIN" "$@"
