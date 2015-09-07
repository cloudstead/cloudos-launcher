API_HOST = "http://107.170.244.149:18080/";

API = {
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
				console.log('setup error: status='+status+', error='+error+', url='+url);
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
			url: API_HOST+url,
			async: false,
			contentType: 'application/json',
			data: JSON.stringify(data),
			// beforeSend: add_api_auth,
			success: function (response, status, jqXHR) {
				result = response;
			},
			error: function (jqXHR, status, error) {
				console.log('setup error: status='+status+', error='+error+', url='+url + "dddd => ");
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

	_post: function(url, data) { return API._update('POST', url, data); },
	_put:  function(url, data) { return API._update('PUT', url, data); },

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
		// return API._post('/api/auth/'+username, password);
		var loginResponse = {
			status: 404,
			data: "bad_credentials"
		};
		if (username === "bojan" && password === "bojan123") {
			loginResponse = {
				status: 200,
				data: "valid-token"
			}
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
};
