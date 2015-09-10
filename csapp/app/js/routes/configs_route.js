App.ConfigsRoute = App.ProtectedRoute.extend({
	model: function() {
		return API.get_configs();
	},
});
