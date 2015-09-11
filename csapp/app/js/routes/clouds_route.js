App.CloudsRoute = App.ProtectedRoute.extend({
	model: function() {
		var response = API.get_clouds();

		var dataArray = [];

		if (response.isSuccess()) {
			dataArray = response.data;
		} else {
			$.notify("Error fetching clouds", { position: "bottom-right", autoHideDelay: 10000, className: 'error' });
		}

		return App.CloudModel.createFromArray(dataArray);
	},
});
