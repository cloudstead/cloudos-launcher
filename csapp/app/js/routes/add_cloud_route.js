App.AddCloudRoute = App.ProtectedRoute.extend({
	model: function() {
		return App.CloudModel.createNewEmpty();
	},
});
