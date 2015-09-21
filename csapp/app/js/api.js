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
		var response = this._post("auth/" + username, password);

		response.data = response.isSuccess() ? response.data : "Wrong password";

		// var response = {
		// 	status: "success",
		// 	statusCode: 200,
		// 	data: "valid-token",
		// 	jqXHR: {},
		// 	isSuccess: function() {
		// 		return true;
		// 	}
		// };
		return response;
	},


	get_clouds: function() {
		return this._get("clouds");
	},

	get_cloud: function(cloudName) {
		return this._get("clouds/"+cloudName);
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

	get_cloudstead: function(cloudsteadName) {
		return this._get("instances/"+cloudsteadName);
	},

	update_cloudstead: function(cloudsteadData) {
		return this._post("instances/"+cloudsteadData.name, cloudsteadData);
	},

	delete_cloudstead: function(cloudsteadName) {
		return this._delete("instances/"+cloudsteadName);
	},

	get_cloud_types: function() {
		return this._get("cloud_types");
	},

	get_cloud_type: function(cloudTypeName) {
		return this._get("cloud_types/"+cloudTypeName);
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
