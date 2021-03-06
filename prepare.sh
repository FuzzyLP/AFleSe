#!/bin/sh

#
# Prepare AFleSe paths and fetch dependencies
#

# Physical directory where the script is located
_base=$(e=$0;while test -L "$e";do d=$(dirname "$e");e=$(readlink "$e");\
      cd "$d";done;cd "$(dirname "$e")";pwd -P)

# Check that Ciao is installed
if ! which ciao > /dev/null 2>&1; then
    cat <<EOF
Please install Ciao (https://ciao-lang.org)
EOF
fi

# Define a workspace to install dependencies
mkdir -p "$_base"/ciao
export CIAOPATH="$_base"/ciao

echo "Installing dependencies..."
echo

# Get dependencies
ciao get github.com/FuzzyLP/RFuzzy
ciao get ciao_java

# TODO: Get Java dependencies too (for that, a conversion of the Java
# Eclipse project to Maven would be great)

bundle_path() { # bundle
    ciaosh -q <<EOF
use_module(library(bundle/bundle_paths)).
bundle_path(${1}, '.', _P), display(_P), nl.
EOF
}

# Copy Ciao Java files into flese/src/CiaoJava
mkdir -p "$_base"/flese/src/CiaoJava
cp "$(bundle_path ciao_java)"/lib/javall/CiaoJava/*.java "$_base"/flese/src/CiaoJava
cp "$(bundle_path ciao_java)"/lib/javall/CiaoJava/*.html "$_base"/flese/src/CiaoJava

# Create wrappers (such that Java always call Ciao with the right
# environment variables)

wrapper_plserver_path=$_base/ciao/plserver # add .bat for Windows
wrapper_ciaoc_path=$_base/ciao/ciaoc # add .bat for Windows

# Wrapper for plserver
plserver_path="$(bundle_path ciao_java)"/lib/javall/plserver
cat > "$wrapper_plserver_path" <<EOF
#!/bin/sh
export CIAOPATH=$_base/ciao
exec "$plserver_path" "\$@"
EOF
chmod a+x "$wrapper_plserver_path"

# Wrapper for ciaoc
ciaoc_path="$(which ciaoc)"
cat > "$wrapper_ciaoc_path" <<EOF
#!/bin/sh
export CIAOPATH=$_base/ciao
exec "$ciaoc_path" "\$@"
EOF
chmod a+x "$wrapper_ciaoc_path"

# Prepare config.properties
mkdir -p "$_base"/flese/resources
cat > "$_base"/flese/resources/config.properties <<EOF
#Configuration file for Flese
#Contains paths for used directories

#path for data and cache storage
programFilesValidPaths=/var/folders/97/jsb60rx56rb2lkrn0fh9gdd40000gn/T/java-apps/fuzzy-search/
programFilesValidPaths2=/var/folders/97/jsb60rx56rb2lkrn0fh9gdd40000gn/T/java-apps/fuzzy-search/
programFilesValidPaths3= 
programFilesValidPaths4= 
programFilesValidPathsFromTemp=/java-apps/fuzzy-search/

# Location of plserver and ciaoc
plServerPath=${wrapper_plserver_path}
ciaocPath=${wrapper_ciaoc_path}
EOF

# TODO: This does not seem to be necessary...
# cp flese/resources/config.properties flese/build/classes/config.properties 

# TODO: is TomCat necessary?
cat <<EOF

Preparation is complete!

Now you can open this project from Eclipse, start as a server, and
open http://localhost:8080/flese from a web browser.

Make sure that you also have installed:
 - Eclipse IDE for Java EE
 - TomCat 7 (download and install it)

EOF
