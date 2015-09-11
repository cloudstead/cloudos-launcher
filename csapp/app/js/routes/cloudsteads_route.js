App.CloudsteadsRoute = App.ProtectedRoute.extend({
	model: function() {
		return App.CloudsteadModel.getAll();
	},
});
