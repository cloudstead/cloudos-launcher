cloudos-launcher
================

This project builds the Cloudstead Launcher desktop apps for Windows, MacOS and Linux.

This project is a submodule of the main `cloudos` repository. In order to build the apps, first clone
the cloudos repository and run `first_time_dev_setup.sh`, this will pull in all the submodules including this one.
 
### Building

    mvn package && ../prep-deploy.sh cloudos-launcher
    
The output from the latter command will include lines that begin with `ARTIFACT: `, these are the 
platform-specific packages.

### Developing

To run the Launcher directly from this repository without building a platform-specific app, there are a couple options.

#### jrun

To use `jrun` launcher utility (as would be available on a cloudos-server, for example), simply run:
    
    jrun .

If `jrun` is not on your path, you can find it in `cloudos-apps/apps/java/files/jrun`. 
Run it from there, or copy it somewhere on your PATH.
 
`jrun` will load the environment variables LAUNCHER_PORT and LAUNCHER_ASSETS_DIR from an environment 
file, if one is found. `jrun` checks for environment files in this order:

  * `/etc/cloudos-launcher.env`
  * `~/.cloudos-launcher.env`
  * `/etc/cloudos.env`
  * `~/.cloudos.env`
  * `./cloudos-launcher.env`
  
#### java

To run the launcher as "raw" as possible:

    java -jar $(find target -maxdepth 1 -type f -name "cloudos-launcher-*.jar")
    
To specify a LAUNCHER_PORT and/or LAUNCHER_ASSETS_DIR, pass them on the command line:

    LAUNCHER_PORT=8000 \
    LAUNCHER_ASSETS_DIR=$(pwd)/csapp/dist \
    java -jar $(find target -maxdepth 1 -type f -name "cloudos-launcher-*.jar")
    
By setting `LAUNCHER_ASSETS_DIR=$(pwd)/csapp/dist`, the web content for the launcher will be read from disk,
instead of from within the launcher jar file. This makes front-end development much easier, since you can
modify files in csapp, then run `lineman build` to rebuild the front-end code (which will be written to `csapp/dist`)
 