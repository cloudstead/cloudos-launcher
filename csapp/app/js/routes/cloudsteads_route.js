App.CloudsteadsRoute = Ember.Route.extend({
	model: function() {
		return API.get_cloudsteads();
	},
});
