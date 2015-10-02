App.EditCloudsteadRoute = App.ProtectedRoute.extend({
	controllerName: 'add_cloudstead',

	model: function(params) {
		return App.CloudsteadModel.getForEdit(params.cloud_name);
	},

	renderTemplate: function() {
		this.render('add_cloudstead');
	},
});
