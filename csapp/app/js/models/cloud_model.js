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

	createWithVendors: function() {
		return API.loadAvailableVendors().then(function(vendors) {
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
		return API.get_clouds().then(function(clouds){
			return App.CloudModel.createFromArray(clouds);
		});
	},

	getForEdit: function(cloudName) {
		var getCloud = API.get_cloud(cloudName);
		var getVendors = API.loadAvailableVendors();

		return Promise.all([getCloud, getVendors]).then(function(responses) {
			var cloud = responses[0];
			var vendors = responses[1];
			return App.CloudModel.create(cloud, {availableVendors: vendors});
		});
	},
});
