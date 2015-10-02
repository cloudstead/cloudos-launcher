App.EditCloudRoute = App.ProtectedRoute.extend({
	controllerName: 'add_cloud',

	model: function(params) {
		return App.CloudModel.getForEdit(params.cloud_name);
	},

	setupController: function(controller, model) {
		this._super( controller, model);

		controller.set("isEdit", true);
	},

	renderTemplate: function() {
		this.render('add_cloud');
	},
});
