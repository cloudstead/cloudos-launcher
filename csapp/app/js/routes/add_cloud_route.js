App.AddCloudRoute = App.ProtectedRoute.extend({
	model: function() {
		return App.CloudModel.createNewEmpty();
	},
	setupController: function(controller, model) {
		this._super( controller, model);
		controller.set("isEdit", false);
	},
});
