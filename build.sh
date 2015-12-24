#!/bin/sh

# Set the variable MCSERVER to ~/Desktop/server
# unless it's already set
: ${MCSERVER:="C:/root/minecraft/Minecraft_plugins/server"}

BUKKIT="$MCSERVER/bukkit.jar"
RABBITMQ="$MCSERVER/lib/commons-cli-1.1.jar;$MCSERVER/lib/rabbitmq-client.jar;$MCSERVER/lib/commons-io-1.2.jar"

# Make sure that the bukkit jar
# exists and is readable
if [ ! -r $BUKKIT ]; then
    echo "$BUKKIT doesn't seem to exist.  Make sure you have bukkit.jar installed at $MCSERVER and run again.  If your server is not at $MCSERVER, set your MCSERVER environment variable to point to the correct directory."
    exit 1
fi

# Make the build directories if they aren't there.
# Throw away any error if they are.
mkdir bin 2>/dev/null
mkdir dist 2>/dev/null

# Remove any previous build products
rm -f bin/*/*.class
rm -f dist/*.jar

# Get the name of this plugin
# from the directory it's in
HERE=`pwd`
NAME=`basename "$HERE"`

# 1. Compile
echo "Compiling with javac..."
#javac -Xlint:deprecation src/*/*.java -d bin -classpath "$BUKKIT;$RABBITMQ" -sourcepath src -target 1.6 -g:lines,vars,source -source 1.6 || exit 2
javac -Xlint:deprecation src/*/*.java -d bin -classpath "$BUKKIT;$RABBITMQ" -sourcepath src -target 1.7 -g:lines,vars,source -source 1.6 || exit 2


# 2. Build the jar
echo "Creating jar file..."
jar -cf dist/"$NAME.jar" *.yml -C bin . || exit 3

# 3. Copy to server
echo "Deploying jar to $MCSERVER/plugins..."
test ! -d "$MCSERVER/plugins" && mkdir -p "$MCSERVER/plugins" 
cp dist/$NAME.jar "$MCSERVER/plugins" || exit 4

echo "Completed Successfully."


