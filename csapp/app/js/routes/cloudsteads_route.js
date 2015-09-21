App.CloudsteadsRoute = App.ProtectedRoute.extend({
	model: function() {
		return App.CloudsteadModel.getAll();
	},

	afterModel: function(model) {
		if (Ember.isEmpty(model)) {
			this.transitionTo("add_cloudstead");
		}
	}
});
