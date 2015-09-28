App.EditCloudsteadRoute = App.BaseCloudsteadRoute.extend({
	controllerName: 'add_cloudstead',

	model: function(params) {
		return API.get_cloudstead(params.cloud_name);
	},
	renderTemplate: function() {
		this.render('add_cloudstead');
	},

	setupController: function(controller, model) {
		this._super(controller, model);

		this.loadRelatedData(controller, model);

		controller.set("isEdit", true);
	},
});
