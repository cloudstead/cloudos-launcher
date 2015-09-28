App.AddCloudRoute = App.ProtectedRoute.extend({
	model: function() {
		return API.loadAvailableVendors();
	},

	setupController: function(controller, model) {
		this._super( controller, model);
		newModel = App.CloudModel.createWithVendors(model);
		controller.set("model", newModel);
		controller.set("isEdit", false);
	},
});
