App.AddCloudsteadRoute = App.ProtectedRoute.extend({
	model: function() {
		console.log("MODEL");
		return App.CloudsteadModel.createNewEmpty();
	},

	setupController: function(controller, model) {
		this._super(controller, model);
		console.log("AAAAAAAAAAAAAAAAAA");
		controller.set("cloudTypes", API.get_cloud_types());
		console.log("Cloud Types: ", controller.get("cloudTypes"));
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
