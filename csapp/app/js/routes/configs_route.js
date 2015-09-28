App.ConfigsRoute = App.ProtectedRoute.extend({
	model: function() {
		return API.get_configs();
	},

	afterModel: function(model) {
		if (Ember.isEmpty(model)) {
			this.transitionTo("addlaunch");
		}
	},

	setupController: function(controller, model) {
		controller.set("model", App.ConfigModel.createFromArray(model));
	}
});
