// This is the initial data that will be added to te 'data_bags/cloudos/init.json'.
INITIAL_CLOUDOS_DATA = {
	id: "init",
	run_as: "cloudos",
	server_tarball: "http://cloudstead.io/downloads/cloudos-server.tar.gz",
};

// This variable holds the data collected from user input.
var DATA = {};

// Sub tab group name for non exclusive tabs.
GENERAL_SUBTAB_GROUP = "generalTabGroup";

// Path to folder containing the apps data.
APPS_DATA_PATH = "js/apps/";

// File to look in for metadata for individual apps.
METADATA_FILENAME = "config-metadata.json";

// File to look in for translations for individual apps.
TRANSLATION_FILENAME = "translations.json";

// This variable will hold translations for appropriate language.
var TRANSLATIONS = {};

// Input types dictionary
INPUT_TYPES = {
	'hostname_part': 'text',
	'hostname': 'text',
	'field': 'text',
	'host': 'text',
	'port': 'text',
	'login': 'text',
	'list_ipaddr': 'text',
	'fqdn': 'text',
	'cron': 'text',
	'username': 'text',
	'text': 'text',
	'hash': 'password',
	'secret_key': 'password',
	'password': 'password',
	'email': 'email',
	'pick_one': 'choice',
	'file': 'file'
};
