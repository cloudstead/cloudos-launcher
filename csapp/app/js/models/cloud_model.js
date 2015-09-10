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
	}
});
