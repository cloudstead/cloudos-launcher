App.ConfigsRoute = App.ProtectedRoute.extend({
	model: function() {
		return App.ConfigModel.getAll();
	},

	afterModel: function(model) {
		if (Ember.isEmpty(model)) {
			this.transitionTo("addlaunch");
		}
	}
});
