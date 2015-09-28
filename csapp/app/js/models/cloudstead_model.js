App.CloudsteadModel = Ember.Object.extend({
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
			name:  this.get("name"),
			vendor:  this.get("vendor"),
			accessKey:  this.get("accessKey"),
			secretKey:  this.get("secretKey"),
		};
	},

	update: function() {
		return API.update_cloudstead(this.toObjectForPost());
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

	get: function(cloudsteadName) {
		var response = API.get_cloudstead(cloudsteadName);
		return App.CloudModel.createNewFromData(response.data);
	},

	getAll: function() {
		var response = API.get_cloudsteads();

		// var dataArray = [];

		// if (response.isSuccess()) {
		// 	dataArray = response.data;
		// } else {
		// 	$.notify("Error fetching configs", { position: "bottom-right", autoHideDelay: 10000, className: 'error' });
		// }

		return App.CloudsteadModel.createFromArray(response);
	},

	findById: function(uuid) {
		var allConfigs = App.CloudsteadModel.getAll();
		var configToFind = allConfigs.find(function(config) {
			return config.uuid === uuid;
		});

		return configToFind;
	}
});
