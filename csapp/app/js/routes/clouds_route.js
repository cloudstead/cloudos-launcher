App.CloudsRoute = App.ProtectedRoute.extend({
	model: function() {
		return App.CloudModel.getAll();
	},

	afterModel: function(model) {
		if (Ember.isEmpty(model)) {
			this.transitionTo("add_cloud");
		}
	},
});
