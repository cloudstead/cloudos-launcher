App.AddCloudsteadRoute = App.ProtectedRoute.extend({
	model: function() {
		return App.CloudsteadModel.createNewEmpty();
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
		console.log("Cloud Types: ", controller.get("cloudTypes"));

		controller.set("sshKeys", App.SshKeyModel.getAll());
	},

	actions: {
		doTransitionToAddCloud: function() {
			this.transitionTo("add_cloud");
		},

		doTransitionToAddLaunch: function() {
			this.transitionTo("addlaunch");
		},

		doTransitionToCloudsteads: function() {
			this.transitionTo("cloudsteads");
		}
	}
});
