API_HOST = "http://107.170.244.149:18080/";

function add_api_auth (xhr) {
	var token = LauncherStorage.getToken();
	console.log("TOKEN: ", token);
	if (token !== undefined) {
		xhr.setRequestHeader(API.API_TOKEN, token);
	}
}

function buildResponse(data, status, jqXHR) {
	return {
		status: status,
		statusCode: jqXHR.status,
		data: data,
		jqXHR: jqXHR,
		isSuccess: function() {
			return this.statusCode === 200;
		}
	};
}

API = {

	API_TOKEN: '__launcher_session',
	API_PREFIX: 'api/',

	_get: function (url) {
		var result = null;
		url = API.API_PREFIX + url;

		Ember.$.ajax({
			type: 'GET',
			url: url,
			async: false,
			beforeSend: add_api_auth,
			success: function (response, status, jqXHR) {
				console.log("GET Response: ", response, status, jqXHR);
				result = buildResponse(response, status, jqXHR);
			},
			error: function (jqXHR, status, error) {
				// $.notify(Em.I18n.translations['errors'].generalServerError, { position: "bottom-right", autoHideDelay: 10000, className: 'error' });
				result = buildResponse(error, status, jqXHR);
			}
		});

		return result;
	},

	_update: function (method, url, data) {
		var result = null;
		url = API.API_PREFIX + url;

		Ember.$.ajax({
			type: method,
			url: url,
			async: false,
			contentType: 'application/json',
			data: JSON.stringify(data),
			beforeSend: add_api_auth,
			success: function (response, status, jqXHR) {
				result = buildResponse(response, status, jqXHR);
			},
			error: function (jqXHR, status, error) {
				result = buildResponse(error, status, jqXHR);
			}
		});
		return result;
	},

	_post: function(url, data) { return API._update('POST', url, data); },
	_put:  function(url, data) { return API._update('PUT', url, data); },

	_delete: function (url) {
		var result = null;
		url = API.API_PREFIX + url;

		Ember.$.ajax({
			type: 'DELETE',
			url: url,
			async: false,
			beforeSend: add_api_auth,
			'success': function (accounts, status, jqXHR) {
				result = buildResponse("true", "success", { status: 200 });
			},
			'error': function (jqXHR, status, error) {
				result = buildResponse("false", "error", { status: 200 });
				// console.log('error deleting '+url+': '+error);
				// $.notify(Em.I18n.translations['errors'].generalServerError, { position: "bottom-right", autoHideDelay: 10000, className: 'error' });
			}
		});

		console.log("DELETE: ", result);

		return result;
	},

	login: function(username, password) {
		// var response = this._post("auth/" + username, password);

		// response.data = response.isSuccess() ? response.data : "Wrong password";

		var response = {
			status: "success",
			statusCode: 200,
			data: "valid-token",
			jqXHR: {},
			isSuccess: function() {
				return true;
			}
		};
		return response;
	},


	get_clouds: function() {
		return this._get("clouds");
	},

	get_cloud: function(cloudName) {

		var a = this._get("clouds/"+cloudName);
		console.log(a);
		return a;
	},

	update_cloud: function(cloudData) {
		return this._post("clouds/"+cloudData.name, cloudData);
	},

	delete_cloud: function(cloudName) {
		return this._delete("clouds/"+cloudName);
	},


	get_configs: function() {
		return this._get("configs");
	},

	update_config: function(configData) {
		return this._post("configs/"+configData.name, configData);
	},

	delete_config: function(configName) {
		return this._delete("configs/"+configName);
	},

	get_cloudsteads: function() {
		return this._get("instances");
	},

	update_cloudstead: function(cloudsteadData) {
		return this._post("instances/"+cloudsteadData.name, cloudsteadData);
	},

	delete_cloudstead: function(cloudsteadName) {
		return this._delete("instances/"+cloudsteadName);
	},

	get_cloud_types: function() {

		var a = this._get("cloud_types");
		console.log(a);
		return a;
		// var response = '[ { "name": "cloud 1", "id": "1", "options":[ { "name": "cloud option 1", "value": "1", "type":"CHOICE", "defaultValue": "One", "required": true, "choices":[ "choice 1", "choice 2" ] } ], "providerName": "Rackspace", "regions":[ { "name": "SE Europe", "country": "Serbia", "region": "Europe", "vendor": "Rackspace" }, { "name": "East US", "country": "USA", "region": "N. America", "vendor": "Rackspace" }, { "name": "West US", "country": "USA", "region": "N. America", "vendor": "Rackspace" } ], "smallestInstanceType": { "name": "small", "storage_type": "local", "memory": 512, "vcpu": 4, "gpu": 2, "system_storage": 1, "storage": 3, "storage_medium": "ssd", "storage_geometry": "star", "storage_bandwidth": 200, "networking": "low", "network_bandwidth": 4 }, "cloudClassName": "cloudos_class", "optionsMap":{ "string": { "name": "option", "value": "value", "type": "CHOICE", "defaultValue": "default", "required": true, "choices":[ "choice 1", "choice 2" ] } }, "instanceTypes":[ { "name": "Instance type 1", "storage_type": "local", "memory": 3, "vcpu": 3, "gpu": 2, "system_storage": 2, "storage": 2, "storage_medium": "ssd", "storage_geometry": "star", "storage_bandwidth": 2, "networking": "low", "network_bandwidth": 5 }, { "name": "Instance type 2", "storage_type": "local", "memory": 3, "vcpu": 3, "gpu": 2, "system_storage": 2, "storage": 2, "storage_medium": "ssd", "storage_geometry": "star", "storage_bandwidth": 2, "networking": "low", "network_bandwidth": 5 }, { "name": "Instance type 3", "storage_type": "local", "memory": 3, "vcpu": 3, "gpu": 2, "system_storage": 2, "storage": 2, "storage_medium": "ssd", "storage_geometry": "star", "storage_bandwidth": 2, "networking": "low", "network_bandwidth": 5 } ] } ]';

		// return JSON.parse(response);
	},

	get_cloud_type: function(cloudTypeName) {

		var a = this._get("cloud_types/"+cloudTypeName);
		console.log(a);
		return a;
		// var response = '[ { "name": "cloud 1", "id": "1", "options":[ { "name": "cloud option 1", "value": "1", "type":"CHOICE", "defaultValue": "One", "required": true, "choices":[ "choice 1", "choice 2" ] } ], "providerName": "Rackspace", "regions":[ { "name": "SE Europe", "country": "Serbia", "region": "Europe", "vendor": "Rackspace" }, { "name": "East US", "country": "USA", "region": "N. America", "vendor": "Rackspace" }, { "name": "West US", "country": "USA", "region": "N. America", "vendor": "Rackspace" } ], "smallestInstanceType": { "name": "small", "storage_type": "local", "memory": 512, "vcpu": 4, "gpu": 2, "system_storage": 1, "storage": 3, "storage_medium": "ssd", "storage_geometry": "star", "storage_bandwidth": 200, "networking": "low", "network_bandwidth": 4 }, "cloudClassName": "cloudos_class", "optionsMap":{ "string": { "name": "option", "value": "value", "type": "CHOICE", "defaultValue": "default", "required": true, "choices":[ "choice 1", "choice 2" ] } }, "instanceTypes":[ { "name": "Instance type 1", "storage_type": "local", "memory": 3, "vcpu": 3, "gpu": 2, "system_storage": 2, "storage": 2, "storage_medium": "ssd", "storage_geometry": "star", "storage_bandwidth": 2, "networking": "low", "network_bandwidth": 5 }, { "name": "Instance type 2", "storage_type": "local", "memory": 3, "vcpu": 3, "gpu": 2, "system_storage": 2, "storage": 2, "storage_medium": "ssd", "storage_geometry": "star", "storage_bandwidth": 2, "networking": "low", "network_bandwidth": 5 }, { "name": "Instance type 3", "storage_type": "local", "memory": 3, "vcpu": 3, "gpu": 2, "system_storage": 2, "storage": 2, "storage_medium": "ssd", "storage_geometry": "star", "storage_bandwidth": 2, "networking": "low", "network_bandwidth": 5 } ] } ]';

		// return JSON.parse(response);
	},

	get_ssh_keys: function() {
		return this._get("keys/");
	},

	create_ssh_key: function(ssh_key) {
		return this._post("keys/"+ssh_key.name, ssh_key);
	},

	delete_ssh_key: function(key_name) {
		return this._delete("keys/"+key_name);
	},
};
