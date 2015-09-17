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
		var response = API.update_cloud(this.toObjectForPost());

		return response.isSuccess();
	},

	destroy: function() {
		var response = API.delete_cloud(this.get("name"));
		console.log("DELETE RESPONSE: ", response.isSuccess());
		return response.isSuccess();
	},
});

App.CloudModel.reopenClass({
	availableVendors: ["Amazon EC2", "Rackspace", "Digitalocean"],

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

	getAll: function(cloudDataArray) {
		var response = API.get_clouds();

		var dataArray = [];

		if (response.isSuccess()) {
			dataArray = response.data;
		} else {
			$.notify("Error fetching clouds", { position: "bottom-right", autoHideDelay: 10000, className: 'error' });
		}

		return App.CloudModel.createFromArray(dataArray);
	},

	findById: function(uuid) {
		var allClouds = App.CloudModel.getAll();
		var cloudToFind = allClouds.find(function(cloud) {
			return cloud.uuid === uuid;
		});

		return cloudToFind;
	}
});
