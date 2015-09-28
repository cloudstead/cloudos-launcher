App.CloudsRoute = App.ProtectedRoute.extend({
	model: function() {
		return API.get_clouds();
	},

	afterModel: function(model) {
		if (Ember.isEmpty(model)) {
			this.transitionTo("add_cloud");
		}
	},

	setupController: function(controller, model) {
		controller.set("model", App.CloudModel.createFromArray(model));
	}
});
