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

NUMBER_OF_CALLS = 0;

RESPONSES = {
	"@class" : "cloudos.launcher.service.LauncherTaskResult",
	"success" : false,
	"events" : [ {
		"taskId" : "b0e111b8-6ad9-4e46-9cba-11961c2892c2",
		"messageKey" : "success",
		"success" : false,
		"cloudOsUuid" : "48f51122-9958-4484-a770-d2d02bee0209",
		"timestamp" : 1442873958846
	} ],
	"cloudOs" : {
		"uuid" : "48f51122-9958-4484-a770-d2d02bee0209",
		"name" : "2k0rbhklx99jeoamjoqp",
		"adminUuid" : "d20ee654-daad-4216-a56e-08aef435ae04",
		"instanceType" : "t1.micro",
		"state" : "initial",
		"lastStateChange" : 0,
		"ucid" : "68a1a235-ba41-4c7b-81d2-eac4f030beae",
		"launch" : "f03b1e3f-354c-430b-9a1b-b3ee05aa9943",
		"cloud" : "0e00ff37-2833-4b91-960d-6353e69f0b89",
		"csRegion" : {
			"name" : "us-east-1",
			"country" : "US",
			"region" : "N. Virginia",
			"vendor" : "AwsCloudType"
		},
		"allApps" : [ "base", "auth", "apache", "postgresql", "mysql", "java", "git", "email", "kestrel", "cloudos" ]
	}
};

API = {

	API_TOKEN: '__launcher_session',
	API_PREFIX: 'api/',

	_get: function(url) {
		url = API.API_PREFIX + url;
		return new Ember.RSVP.Promise(function(resolve, reject) {
			Ember.$.ajax({
				type: 'GET',
				url: url,
				// async: false,
				beforeSend: add_api_auth,
				success: function(response) {
					resolve(response);
				},
				error: function(reason) {
					reject(reason);
				}
			});
		});
	},

	_update: function (method, url, data) {
		url = API.API_PREFIX + url;
		return new Ember.RSVP.Promise(function(resolve, reject) {
			Ember.$.ajax({
				type:method,
				url: url,
				// async: false,
				data: JSON.stringify(data),
				contentType: 'application/json',
				beforeSend: add_api_auth,
				success: function(response) {
					resolve(response);
				},
				error: function(reason) {
					reject(reason);
				}
			});
		});
	},

	_login: function (url, data) {
		var result = null;
		url = API.API_PREFIX + url;

		Ember.$.ajax({
			type: "POST",
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
		url = API.API_PREFIX + url;

		return new Ember.RSVP.Promise(function(resolve, reject) {
			Ember.$.ajax({
				type: 'DELETE',
				url: url,
				// async: false,
				beforeSend: add_api_auth,
				success: function(response) {

					console.log("DEL");
					resolve(response);
				},
				error: function(reason) {
					console.log("ERR", reason);
					reject(reason);
				}
			});
		});
	},

	login: function(username, password) {
		var response = this._login("auth/" + username, password);

		response.data = response.isSuccess() ? response.data : "Wrong password";

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
		// return this._post("instances/"+cloudsteadData.name, cloudsteadData);
		return new Ember.RSVP.Promise(function(resolve){
			resolve('cloudstead created');
		});
	},

	launch_cloudstead: function(cloudsteadName) {
		// return this._post("instances/"+cloudsteadName+"/launch");

		var rand = Math.random()*100000000000000000;

		var response = {
			uuid: "taskid_"+rand,
		};

		return new Ember.RSVP.Promise(function(resolve){
			resolve(response);
		});
	},

	delete_cloudstead: function(cloudsteadName) {
		// return this._delete("instances/"+cloudsteadName);
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

	loadAvailableVendors: function() {
		return API.get_cloud_types().then(function(cloudTypeNames) {

			var promises = [];

			cloudTypeNames.forEach(function(cloudTypeName) {
				promises.push(API.get_cloud_type(cloudTypeName));
			});

			return Promise.all(promises);
		}).then(function(cloudTypes){
			return cloudTypes.map(function(cloudType){
				return cloudType.providerName;
			});
		});
	},

	loadCloudTypes: function() {
		return API.get_cloud_types().then(function(cloudTypeNames) {

			var promises = [];

			cloudTypeNames.forEach(function(cloudTypeName) {
				promises.push(API.get_cloud_type(cloudTypeName));
			});

			return Promise.all(promises);
		}).then(function(cloudTypes){
			return cloudTypes;
		});
	},

	get_task: function(taskId) {
		var msg = "";
		var success = false;
		console.log(NUMBER_OF_CALLS);
		switch(NUMBER_OF_CALLS){
			case 0:
				msg = "setup.cheffing";
				break;
			case 1:
				msg = "setup.cheffing.percent_done_25";
				break;
			case 2:
				msg = "setup.cheffing.percent_done_55";
				break;
			case 3:
				msg = "setup.cheffing.percent_done_75";
				break;
			case 4:
				msg = "setup.cheffing.percent_done_85";
				break;
			case 5:
				msg = "setup.cheffing.percent_done_100";
				break;
			case 6:
				msg = "setup.instanceLookup";
				break;
			case 7:
				msg = "setup.startingMasterInstance";
				break;
			case 8:
				msg = "setup.success";
				success = true;
				break;
			case 9:
				msg = "";
				RESPONSES.evets = [];
				RESPONSES.success = false;
				NUMBER_OF_CALLS = 0;
				break;
			default:
				break;
		}

		RESPONSES.events.push({messageKey: msg});
		RESPONSES.success = success;
		NUMBER_OF_CALLS += 1;

		return new Ember.RSVP.Promise(function(resolve){
			resolve(RESPONSES);
		});
	}

};
