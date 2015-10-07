App.CloudsteadModel = Ember.Object.extend({
	launchTaskKey: "",

	toObject: function() {
		return {
			uuid: this.get("uuid"),
			name:  this.get("name"),
			vendor:  this.get("vendor"),
			accessKey:  this.get("accessKey"),
			secretKey:  this.get("secretKey"),
			account:  this.get("account"),
			optionalJson:  this.get("optionalJson"),
		};
	},

	toObjectForPost: function() {
		return {
			privateKey: "test",
			user: "test",
			region: "test",
			cloud: "test",
			instanceType: "test",
			instanceId: "test",
			sshKey: "test",
			launchConfig: "test",
			additionalApps: "test",
			keyPassphrase: "test",
			name: "test",
		};
	},

	update: function() {
		return API.update_cloudstead(this.toObjectForPost());
	},

	doLaunch: function() {
		return API.launch_cloudstead(this.get("name"));
	},

	destroy: function() {
		return API.delete_cloudstead(this.get("name"));
	},
});

App.CloudsteadModel.reopenClass({
	createNewEmpty: function() {
		return App.CloudsteadModel.create({
			uuid : "",
			name : "",
			adminUuid : "",
			instanceType : "",
			state : "",
			lastStateChange : 0,
			ucid : "",
			launch : "",
			cloud : "",
			csRegion : {
				name : "",
				country : "",
				region : "",
				vendor : ""
			},
			allApps : [],
		});
	},

	createNewFromData: function(dataObject) {
		return App.CloudsteadModel.create(dataObject);
	},

	createFromArray: function(cloudsteadDataArray) {
		var retArray = [];
		cloudsteadDataArray.forEach(function(cloudsteadData) {
			retArray.push(App.CloudsteadModel.createNewFromData(cloudsteadData));
		});

		return retArray;
	},

	createWithVendors: function(vendors) {
		return App.CloudModel.create({
			uuid: "",
			name: "",
			vendor: "",
			accessKey: "",
			secretKey: "",
			account: "",
			optionalJson: "",
			cloudTypes: vendors
		});
	},

	createNewForAdd: function() {
		var getCloudTypes = API.loadCloudTypes();
		var getClouds = API.get_clouds();
		var getSshKeys = API.get_ssh_keys();

		return Promise.all([getCloudTypes,getClouds,getSshKeys]).then(function(responses){
			console.log("RESPONSES: ", responses);
			var cloudTypes = responses[0];
			var clouds = responses[1];
			var sshKeys = responses[2];

			return App.CloudsteadModel.create({
				uuid : "",
				name : "",
				adminUuid : "",
				instanceType : "",
				state : "",
				lastStateChange : 0,
				ucid : "",
				launch : "",
				cloud : "",
				csRegion : {
					name : "",
					country : "",
					region : "",
					vendor : ""
				},
				allApps : [],
				cloudTypes: cloudTypes,
				clouds: clouds,
				sshKeys: sshKeys,
			});
		});
	},

	getForEdit: function(cloudsteadName) {
		var getCloudstead = API.get_cloudstead(cloudsteadName);

		var getCloudTypes = API.loadCloudTypes();
		var getClouds = API.get_clouds();
		var getSshKeys = API.get_ssh_keys();

		return Promise.all([getCloudTypes,getClouds,getSshKeys, getCloudstead]).then(function(responses){
			console.log("RESPONSES: ", responses);
			var cloudTypes = responses[0];
			var clouds = responses[1];
			var sshKeys = responses[2];
			var cloudstead = responses[3];

			return App.CloudsteadModel.create(cloudstead, {
				cloudTypes: cloudTypes,
				clouds: clouds,
				sshKeys: sshKeys,
			});
		});
	},

	getCloudstead: function(cloudsteadName) {
		var getCloud = API.get_cloud(cloudName);
		var getVendors = API.loadAvailableVendors();

		return Promise.all([getCloud, getVendors]).then(function(responses) {
			var cloud = responses[0];
			var vendors = responses[1];
			return App.CloudModel.create(cloud, {availableVendors: vendors});
		});
	},

	getAll: function() {
		return API.get_cloudsteads().then(function(cloudsteads){
			return App.CloudModel.createFromArray(cloudsteads);
		});
	},
});
