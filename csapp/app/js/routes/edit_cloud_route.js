App.EditCloudRoute = App.ProtectedRoute.extend({
	controllerName: 'add_cloud',

	model: function(params) {
		return App.CloudModel.get(params.cloud_name);
	},
	renderTemplate: function() {
		this.render('add_cloud');
	},
	setupController: function(controller, model) {
		this._super( controller, model);
		controller.set("isEdit", true);
	},
});
