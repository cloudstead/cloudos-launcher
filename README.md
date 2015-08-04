cloudos-launcher
================

This project builds the Cloudstead Launcher desktop apps for Windows, MacOS and Linux.

This project is a submodule of the main `cloudos` repository. In order to build the apps, first clone
the `cloudos` repository and run `first_time_dev_setup.sh`, this will pull in all the submodules including this one.

In the command-line examples below, the current working directory is the cloudos-launcher (the same directory that
contains this README file).
 
### Build the Launcher

    mvn package
    
### Run the Launcher

    ./bin/run.sh

Normally, the server would listen on a randomly-selected port and server static assets from within the jar file.

By running the command above, the server will listen on port 18080 and static assets will be read from `csapp/dist`

This makes front-end development much easier. If you modify some files in `csapp` and then run `lineman build` from within the `csapp` directory,
the new site will be written to `csapp/dist`, and the cloudos-launcher server will now see the updated files.

### Configuration and Debugging

To change the server port, set the `LAUNCHER_PORT` environment variable. For example:

    LAUNCHER_PORT=8123 ./bin/run.sh

To change where static assets are loaded from, set the `LAUNCHER_ASSETS_DIR` environment variable. For example:

    LAUNCHER_ASSETS_DIR=/path/to/web-root ./bin/run.sh

To debug the server-side API, run:

    ./bin/run.sh debug

The server will then pause during start up and wait for a remote debugger to attach to port 5005.
To set a different debug port, for example port 6060, run:
 
    ./bin/run.sh debug 6060

### Build the Platform-specific Apps

    ../prep-deploy.sh cloudos-launcher
    
The output from the above command will include lines that begin with `ARTIFACT: `

These artifacts represent the install bundles for the platform-specific apps: zipfiles for Windows, DMG files for MacOS, and tarballs for Linux. 
