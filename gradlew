#!/bin/sh

# Help Message
if [ "$1" = "--help" ] || [ "$1" = "-h" ]; then
  echo "Gradle Wrapper Script"
  echo "Launches Gradle for this project."
fi

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/bin/java" ] ; then
        # Some systems use primary-jvm
        JAVACMD="$JAVA_HOME/bin/java"
    else
        JAVACMD="java"
    fi
else
    JAVACMD="java"
fi

if [ ! -x "$JAVACMD" ] && ! command -v "$JAVACMD" >/dev/null 2>&1; then
    echo "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH." >&2
    exit 1
fi

# Execute gradle using the standard system-level installation
if command -v gradle >/dev/null 2>&1; then
    exec gradle "$@"
else
    echo "ERROR: Local Gradle installation not found. Please construct/install Gradle or build via AIDE." >&2
    exit 1
fi
