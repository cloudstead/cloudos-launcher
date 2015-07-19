/* Exports a function which returns an object that overrides the default &
 *   plugin file patterns (used widely through the app configuration)
 *
 * To see the default definitions for Lineman's file paths and globs, see:
 *
 *   - https://github.com/linemanjs/lineman/blob/master/config/files.coffee
 */
module.exports = function(lineman) {
	//Override file patterns here
	return {
		js: {
			vendor: [
				"vendor/js/localized_en.js",
				"vendor/js/jquery.js",
				"vendor/js/handlebars-v1.3.0.js",
				"vendor/js/ember.js",
				"vendor/js/i18n.js",
				"vendor/js/foundation.min.js",
				"vendor/js/jquery.timepicker.min.js",
				"vendor/js/jquery.timepicker.min.js",
				"vendor/js/FileSaver.js",
				"vendor/js/jszip.min.js",
				"vendor/js/twin-bcrypt.min.js",
				"vendor/js/notify.min.js",
			],
			app: [
				"app/js/input_data/**/*.js",
				"app/js/config/**/*.js",
				"app/js/strings/**/*.js",
				"app/js/utility.js",
				"app/js/setup.js",
				"app/js/**/*.js"
			],
		}
	};
};