App.CloudsRoute = App.ProtectedRoute.extend({
	model: function() {
		return API.get_clouds();
	},
});
