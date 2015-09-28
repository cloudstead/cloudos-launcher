App.CloudsteadsRoute = App.ProtectedRoute.extend({
	model: function() {
		return API.get_cloudsteads();
	},

	afterModel: function(model) {
		if (Ember.isEmpty(model)) {
			this.transitionTo("add_cloudstead");
		}
	},

	setupController: function(controller, model) {
		controller.set("model", App.CloudsteadModel.createFromArray(model));
	}
});
