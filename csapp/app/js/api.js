API_HOST = "http://107.170.244.149:18080/";

function add_api_auth (xhr) {
	var token = LaucherStorage.getToken();
	if (!Ember.IsNone(token)) {
		xhr.setRequestHeader(Api.API_TOKEN, token);
	}
}

API = {

	API_TOKEN: '__launcher_session',
	_get: function (url) {
		var results = null;
		Ember.$.ajax({
			type: 'GET',
			url: url,
			async: false,
			beforeSend: add_api_auth,
			success: function (data, status, jqXHR) {
				results = data;
			},
			error: function (jqXHR, status, error) {
				$.notify(Em.I18n.translations['errors'].generalServerError, { position: "bottom-right", autoHideDelay: 10000, className: 'error' });
				results = {
					status: status,
					statusCode: jqXHR.status,
					jqXHR: jqXHR,
					errorMessage: error
				};
			}
		});
		return results;
	},

	_update: function (method, url, data) {
		var result = null;
		Ember.$.ajax({
			type: method,
			url: url,
			async: false,
			contentType: 'application/json',
			data: JSON.stringify(data),
			beforeSend: add_api_auth,
			success: function (response, status, jqXHR) {
				result = response;
			},
			error: function (jqXHR, status, error) {
				$.notify(Em.I18n.translations['errors'].generalServerError, { position: "bottom-right", autoHideDelay: 10000, className: 'error' });
				result = {
					status: status,
					statusCode: jqXHR.status,
					jqXHR: jqXHR,
					errorMessage: error
				};
			}
		});
		return result;
	},

	_post: function(url, data) { return Api._update('POST', url, data); },
	_put:  function(url, data) { return Api._update('PUT', url, data); },

	_delete: function (path) {
		var ok = false;
		Ember.$.ajax({
			type: 'DELETE',
			url: path,
			async: false,
			beforeSend: add_api_auth,
			'success': function (accounts, status, jqXHR) {
				ok = true;
			},
			'error': function (jqXHR, status, error) {
				console.log('error deleting '+path+': '+error);
				$.notify(Em.I18n.translations['errors'].generalServerError, { position: "bottom-right", autoHideDelay: 10000, className: 'error' });
			}
		});
		return ok;
	},

	login: function(username, password) {
		// this.post("api/auth/" + username, password);

		var loginResponse = {
			status: 404,
			data: "bad_credentials"
		};

		if (username === "bojan" && password === "bojan123") {
			loginResponse = {
				status: 200,
				data: "valid-token"
			};
		}

		return loginResponse;
	},


	get_clouds: function() {
		var response = [];
		for(i = 0; i< 5; i++){
			response.push({
				id: i,
				name: "server " + i,
				provider: "provider " + i,
			});
		}
		return response;
	},

	delete_provider: function() {

	},


	get_configs: function() {
		var response = [];
		for(i = 0; i< 5; i++){
			response.push({
				id: i,
				name: "config " + i,
			});
		}
		return response;
	},

	delete_config: function() {

	},


	get_cloudsteads: function() {
		var response = [];
		for(i = 0; i< 5; i++){
			response.push({
				id: i,
				name: "cloudstead " + i,
				cloud: "cloudstead cloud " + i,
				launch_config: "cloudstead launch config " + i,
				status: "cloudstead status " + i,
			});
		}
		return response;
	},

	delete_cloudstead: function() {

	},

	get_cloud_types: function() {
		var response = '[ { "name": "cloud 1", "id": "1", "options":[ { "name": "cloud option 1", "value": "1", "type":"CHOICE", "defaultValue": "One", "required": true, "choices":[ "choice 1", "choice 2" ] } ], "providerName": "Rackspace", "regions":[ { "name": "SE Europe", "country": "Serbia", "region": "Europe", "vendor": "Rackspace" }, { "name": "East US", "country": "USA", "region": "N. America", "vendor": "Rackspace" }, { "name": "West US", "country": "USA", "region": "N. America", "vendor": "Rackspace" } ], "smallestInstanceType": { "name": "small", "storage_type": "local", "memory": 512, "vcpu": 4, "gpu": 2, "system_storage": 1, "storage": 3, "storage_medium": "ssd", "storage_geometry": "star", "storage_bandwidth": 200, "networking": "low", "network_bandwidth": 4 }, "cloudClassName": "cloudos_class", "optionsMap":{ "string": { "name": "option", "value": "value", "type": "CHOICE", "defaultValue": "default", "required": true, "choices":[ "choice 1", "choice 2" ] } }, "instanceTypes":[ { "name": "Instance type 1", "storage_type": "local", "memory": 3, "vcpu": 3, "gpu": 2, "system_storage": 2, "storage": 2, "storage_medium": "ssd", "storage_geometry": "star", "storage_bandwidth": 2, "networking": "low", "network_bandwidth": 5 }, { "name": "Instance type 2", "storage_type": "local", "memory": 3, "vcpu": 3, "gpu": 2, "system_storage": 2, "storage": 2, "storage_medium": "ssd", "storage_geometry": "star", "storage_bandwidth": 2, "networking": "low", "network_bandwidth": 5 }, { "name": "Instance type 3", "storage_type": "local", "memory": 3, "vcpu": 3, "gpu": 2, "system_storage": 2, "storage": 2, "storage_medium": "ssd", "storage_geometry": "star", "storage_bandwidth": 2, "networking": "low", "network_bandwidth": 5 } ] } ]';

		return JSON.parse(response);
	},

	get_ssh_keys: function() {
		var response = '[{"uuid":"123","name":"key1","publicKey":"kakakakakalalqw","account":"acc1"},{"uuid":"123","name":"key2","publicKey":"kakakakakalalqw","account":"acc1"},{"uuid":"123","name":"key3","publicKey":"kakakakakalalqw","account":"acc1"}]';
		return JSON.parse(response);
	},

	post_new_ssh_key: function(ssh_key_model) {
		var requestData = ssh_key_model.toJSObject();
	},
};
