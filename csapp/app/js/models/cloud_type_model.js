App.CloudTypeModel = Ember.Object.extend({
});

App.CloudTypeModel.reopenClass({
		availableVendors: ["Amazon EC2", "Rackspace", "Digitalocean"],

		createNewEmpty: function() {
				return App.CloudModel.create({
						name: "",
						id: "",
						options: [],
						providerName: "",
						regions: [],
						smallestInstanceType: {},
						cloudClassName: "",
						optionsMap: {},
						instanceTypes: [],
				});
		}
});
