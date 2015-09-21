App.EditCloudsteadRoute = App.ProtectedRoute.extend({
	controllerName: 'add_cloudstead',

	model: function(params) {
		return App.CloudsteadModel.get(params.cloudstead_name);
	},
	renderTemplate: function() {
		this.render('add_cloudstead');
	},

	setupController: function(controller, model) {
		this._super(controller, model);
		var cloudTypeNames = API.get_cloud_types().data;
		var cloudTypes = [];

		cloudTypeNames.forEach(function(cloudTypeName) {
			cloudTypes.push(API.get_cloud_type(cloudTypeName).data);
		});
		controller.set("cloudTypes", cloudTypes);

		controller.set("clouds", App.CloudModel.getAll());

		controller.set("sshKeys", App.SshKeyModel.getAll());

		controller.set("isEdit", true);
	},
});
