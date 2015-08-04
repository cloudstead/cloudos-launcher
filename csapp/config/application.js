/*
Exports an object that defines
 all of the configuration needed by the projects'
 depended-on grunt tasks.

You can find the parent object in: node_modules/lineman/config/application.js
*/

// This is the path to the folder containing thhe apps folder.
var APPS_DATA_CWD = 'app/js/input_data/';

// This is the path to the folder containing the file with the list of apps.
var APPS_DATA_LIST_CWD = 'app/js/input_data/';

// Change this if you wish to change the name of the folder containing the apps.
// NOTE: If you change this value make sure you also chage the value of the 'APPS_DATA_PATH'
// variable in the 'app/js/config/config.js' file.
var APPS_DATA_FOLDER = 'apps/';

// This is the path to the folder containing the file with the list of apps.
var APPS_DATA_LIST_FILE = 'apps.js';

// This is the folder where the apps folder will be copied to.
var APPS_DATA_DEST = 'js/';

module.exports = function(lineman) {
	// Override application configuration here. Common examples follow in the comments.
	return {

		appendTasks:{
			common: ["copy:dev_apps", "copy:dev_apps_list"],
			dist: ["copy:dist_apps", "copy:dist_apps_list", "copy:build"]
		},


		copy: {
			dist_apps_list: {
				files:[
					{
						expand: true,
						cwd: APPS_DATA_LIST_CWD,
						src: APPS_DATA_LIST_FILE,
						dest: 'dist/' + APPS_DATA_DEST
					}
				]
			},
			dist_apps: {
				files:[
					{
						expand: true,
						cwd: APPS_DATA_CWD,
						src: APPS_DATA_FOLDER + "**/*",
						dest: 'dist/' + APPS_DATA_DEST
					}
				]
			},
			dev_apps_list: {
				files:[
					{
						expand: true,
						cwd: APPS_DATA_LIST_CWD,
						src: APPS_DATA_LIST_FILE,
						dest: 'generated/' + APPS_DATA_DEST
					}
				]
			},
			dev_apps: {
				files:[
					{
						expand: true,
						cwd: APPS_DATA_CWD,
						src: APPS_DATA_FOLDER + "**/*",
						dest: 'generated/' + APPS_DATA_DEST
					}
				]
			},
			build: {
				files:[
						{	expand: true,
							cwd: 'dist',
							src: '**/*',
							dest: '../target/classes/web'
							}
				]
			}
		},

	}
}
