App.CloudsteadsController = App.BaseArrayController.extend({
	actions: {
		removeConfig: function () {
		},

		doAddCloudstead: function() {
			this.send("doTransitionTo", "add_cloudstead");
		},
	}
});
