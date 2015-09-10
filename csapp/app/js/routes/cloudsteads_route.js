App.CloudsteadsRoute = App.ProtectedRoute.extend({
	model: function() {
		return API.get_cloudsteads();
	},
});
