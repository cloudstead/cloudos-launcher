App.CloudsController = App.BaseArrayController.extend({
	providerToDelete: null,
	actions: {
		doAddCloud: function() {
			this.send("doTransitionTo", "add_cloud");
		},

		doDelete: function(cloud) {
			var self = this;
			cloud.destroy().then(function(response) {
				self.handleDeleteSuccess(response, cloud);
			}, function(reason){
				self.handleDeleteFailure(reason, cloud);
			});
		},
	},

	handleDeleteSuccess: function(response, cloud) {
		this.get('content').removeObject(cloud);
	},

	handleDeleteFailure: function(reason, cloud) {
		if (reason.status === 200) {
			this.handleDeleteSuccess(reason, cloud);
		} else if (reason.status === 403) {
			self.send("handleForbiddenResponse");
		}
	},
});
