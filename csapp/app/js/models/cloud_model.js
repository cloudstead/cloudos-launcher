App.CloudModel = Ember.Object.extend({
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
		return API.update_cloud(this.toObjectForPost());
	},

	destroy: function() {
		return API.delete_cloud(this.get("name"));
	},

	availableVendors: [],

});

App.CloudModel.reopenClass({

	createNewEmpty: function() {
		return App.CloudModel.create({
			uuid: "",
			name: "",
			vendor: "",
			accessKey: "",
			secretKey: "",
			account: "",
			optionalJson: "",
		});
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
			availableVendors: vendors
		});
	},

	createNewFromData: function(dataObject) {
		return App.CloudModel.create(dataObject);
	},

	createFromArray: function(cloudDataArray) {
		var retArray = [];
		cloudDataArray.forEach(function(cloudData) {
			retArray.push(App.CloudModel.createNewFromData(cloudData));
		});

		return retArray;
	},

	getAll: function() {
		var response = API.get_clouds();

		// var dataArray = [];

		// if (response.isSuccess()) {
		// 	dataArray = response.data;
		// } else {
		// 	$.notify("Error fetching clouds", { position: "bottom-right", autoHideDelay: 10000, className: 'error' });
		// }

		return App.CloudModel.createFromArray(response);
	},

	get: function(cloudName) {
		var response = API.get_cloud(cloudName);
		return App.CloudModel.createNewFromData(response.data);
	},

	findById: function(uuid) {
		var allClouds = App.CloudModel.getAll();
		var cloudToFind = allClouds.find(function(cloud) {
			return cloud.uuid === uuid;
		});

		return cloudToFind;
	},
});
