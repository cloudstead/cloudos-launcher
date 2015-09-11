App.AddCloudController = App.BaseObjectController.extend({
	allVendors: function() {
		return App.CloudModel.availableVendors;
	}.property(),
	actions: {
		doCreate: function () {
			console.log("CLOUD: ", this.get("model.vendor"));
		},

		doCancel: function() {
			this.doTransitionToPreviuosRoute();
		}
	},
});
