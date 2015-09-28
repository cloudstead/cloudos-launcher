App.CloudsteadsController = App.BaseArrayController.extend({
	actions: {
		doAddCloudstead: function() {
			this.send("doTransitionTo", "add_cloudstead");
		},

		doDelete: function(cloudstead) {
			var self = this;
			cloudstead.destroy().then(function(response) {
				self.handleDeleteSuccess(response, cloudstead);
			}, function(reason){
				self.handleDeleteFailure(reason, cloudstead);
			});
		},
	},

	handleDeleteSuccess: function(response, cloudstead) {
		this.get('content').removeObject(cloudstead);
	},

	handleDeleteFailure: function(reason, cloudstead) {
		if (reason.status === 200) {
			this.handleDeleteSuccess(reason, cloudstead);
		} else if (reason.status === 403) {
			this.send("handleForbiddenResponse");
		}
	},
});
