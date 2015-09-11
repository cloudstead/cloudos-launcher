App.AddCloudController = App.BaseObjectController.extend({
	allVendors: function() {
		return App.CloudModel.availableVendors;
	}.property(),
	actions: {
		doCreate: function () {
			var cloud = this.get("model");
			if (cloud.update()) {
				this.doTransitionToPreviuosRoute();
			}
		},

		doCancel: function() {
			this.doTransitionToPreviuosRoute();
		}
	},
});
