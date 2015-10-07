App.AddCloudRoute = App.ProtectedRoute.extend({
	model: function() {
		return App.CloudModel.createWithVendors();
	},

	setupController: function(controller, model) {
		this._super( controller, model);

		controller.set("isEdit", false);
	},
});
