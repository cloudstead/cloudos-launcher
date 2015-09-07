App.ConfigsRoute = Ember.Route.extend({
	model: function() {
		return API.get_configs();
	},
});
