App.CloudsRoute = Ember.Route.extend({
	model: function() {
		return API.get_clouds();
	},
});
