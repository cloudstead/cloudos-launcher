App.CloudModel = Ember.Object.extend({
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
});
